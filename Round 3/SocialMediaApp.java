import java.util.*;

class SocialMediaApp {
	static class User {
		int age;
		String dob;
		String userName;
		String location;
		String occupation;
		List<String> friends;
		List<String> sentRequests;
		List<String> receivedRequests;

		public User(String userName, String dob, int age, String location, String occupation) {
			this.age = age;
			this.dob = dob;
			this.userName = userName;
			this.location = location;
			this.occupation = occupation;
			this.friends = new ArrayList<>();
			this.sentRequests = new ArrayList<>();
			this.receivedRequests = new ArrayList<>();
		}

		public String getUserName() {
			return userName;
		}

		public String getDob() {
			return dob;
		}

		public int getAge() {
			return age;
		}

		public String getLocation() {
			return location;
		}

		public String getOccupation() {
			return occupation;
		}

		public List<String> getFriends() {
			return friends;
		}

		public List<String> getSentRequests() {
			return sentRequests;
		}

		public List<String> getReceivedRequests() {
			return receivedRequests;
		}

		public void addFriend(String friendName) {
			friends.add(friendName);
		}

		public void addSentRequest(String requestName) {
			sentRequests.add(requestName);
		}

		public void addReceivedRequest(String requestName) {
			receivedRequests.add(requestName);
		}
	}
	
    static Scanner in = new Scanner(System.in);
    static Map<String, User> users = new HashMap<>();

    static void signup() {
        System.out.println("Enter User Name");
        String userName = in.next().toUpperCase();

        if (users.containsKey(userName)) {
            System.out.println("User Name already Exists!");
            return;
        }

        System.out.println("Enter DOB: Format(mm-dd-yyyy)");
        String dob = in.next();

        System.out.println("Enter Age");
        int age = in.nextInt();

        System.out.println("Enter location");
        String location = in.next();

        System.out.println("Enter Occupation");
        String occupation = in.next();

        User newUser = new User(userName, dob, age, location, occupation);
        users.put(userName, newUser);

        System.out.println("Successfully User Signed Up");
    }

    static void friendSuggestion() {
        System.out.println("Enter User Name");
        String currentUserName = in.next().toUpperCase();

        if (!users.containsKey(currentUserName)) {
            System.out.println("User Name not Exists!");
            return;
        }

        User currentUser = users.get(currentUserName);
        List<String> friends = currentUser.getFriends();

        System.out.println("\nYour Friends:");
        System.out.println(friends);

        System.out.println("\nFriend Suggestions based on profile match:");
        List<String> suggestions = new ArrayList<>();

        for (String userNames : users.keySet()) {
            if (userNames.equals(currentUserName) || friends.contains(userNames) || currentUser.getSentRequests().contains(userNames)) {
                continue;
            }
            suggestions.add(userNames);
        }

        suggestions.sort((a, b) -> {
            User userA = users.get(a);
            User userB = users.get(b);
            
            int scoreA = 0, scoreB = 0;
            int ageA = Math.abs(userA.getAge() - currentUser.getAge());
            int ageB = Math.abs(userB.getAge() - currentUser.getAge());
            
            if (userA.getDob().substring(0, 2).equals(currentUser.getDob().substring(0, 2)))
                scoreA += 1;
            else
                scoreB += 1;
                
            if (userA.getLocation().equals(currentUser.getLocation()))
                scoreA += 2;
            else
                scoreB += 2;
                
            return Integer.compare(scoreB - ageB, scoreA - ageA);
        });

        for (String suggestion : suggestions) {
            User suggestionUser = users.get(suggestion);
            System.out.println("- " + suggestion + " [Age: " + suggestionUser.getAge() + ", Location: " + suggestionUser.getLocation() + ", Occupation: " + suggestionUser.getOccupation() + "]");
        }
    }

    static void friendRequest() {
        System.out.println("Enter your User Name");
        String userName = in.next().toUpperCase();

        if (!users.containsKey(userName)) {
            System.out.println("User Name not Exists!");
            return;
        }

        System.out.println("Enter Friend's User Name");
        String friendUserName = in.next().toUpperCase();

        if (!users.containsKey(friendUserName)) {
            System.out.println("Friend User Name not Exists!");
            return;
        }

        if (userName.equals(friendUserName)) {
            System.out.println("Cannot send friend request to yourself.");
            return;
        }

        User user = users.get(userName);
        User friendUser = users.get(friendUserName);

        if (user.getFriends().contains(friendUserName)) {
            System.out.println("You are already friend with " + friendUserName);
            return;
        }
        
        if (user.getSentRequests().contains(friendUserName)) {
            System.out.println("Friend request already sent to " + friendUserName);
            return;
        }

        if (user.getReceivedRequests().contains(friendUserName)) {
            System.out.println("This user has already sent you a request. Go to Manage Friend Request.");
            return;
        }

        user.addSentRequest(friendUserName);
        friendUser.addReceivedRequest(userName);
        System.out.println("Friend request sent to " + friendUserName);
    }
    
    static void manageRequests() {
        System.out.println("Enter your User Name");
        String userName = in.next().toUpperCase();

        if (!users.containsKey(userName)) {
            System.out.println("User Name not Exists!");
            return;
        }

        User user = users.get(userName);
        List<String> receivedRequests = user.getReceivedRequests();

        if (receivedRequests.isEmpty()) {
            System.out.println("You have no pending friend requests.");
            return;
        }

        System.out.println("\nPending Friend Requests:");
        for (int i = 0; i < receivedRequests.size(); i++) 
            System.out.println(i + " - " + receivedRequests.get(i));

        System.out.println("\nEnter the user number to access the request or -1 to go to Main Menu:");
        int choice = in.nextInt();
        if(choice == -1)
            return;
        if (choice < receivedRequests.size()) {
            String requestUserName = receivedRequests.get(choice);
            System.out.println("Accept request from " + requestUserName + " enter: (yes/no)");
            String response = in.next().toLowerCase();

            User requestUser = users.get(requestUserName);
            requestUser.getSentRequests().remove(userName);
            user.getReceivedRequests().remove(requestUserName);
            
            if (response.equals("yes")) {
                user.addFriend(requestUserName);
                requestUser.addFriend(userName);
                System.out.println("Request from " + requestUserName+" acepted.");
            } else
                System.out.println("Request from " + requestUserName + " rejected.");
        }
    }

    static void displayFriends() {
        System.out.println("Enter User Name");
        String userName = in.next().toUpperCase();

        if (!users.containsKey(userName)) {
            System.out.println("User Name not Exists!");
            return;
        }

        User user = users.get(userName);
        List<String> friends = user.getFriends();
        if (friends.isEmpty()) {
            System.out.println("No FriendList.");
        } else {
            System.out.println("\nYour Friends:");
            for (String friend : friends) {
                User friendUser = users.get(friend);
                System.out.println("- " + friendUser.getUserName() 
                                        + "  Age: " + friendUser.getAge() 
                                        + ", DOB: " + friendUser.getDob() 
                                        + ", Location: " + friendUser.getLocation() 
                                        + ", Occupation: " + friendUser.getOccupation());
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nEnter valid choice:");
            System.out.println("1. Signup");
            System.out.println("2. Friend Suggestions");
            System.out.println("3. Send Friend Request");
            System.out.println("4. Manage Friend Request");
            System.out.println("5. View Friends");
            System.out.println("6. Exit");
            int choice = in.nextInt();
            switch (choice) {
                case 1:
                    signup();
                    break;
                case 2:
                    friendSuggestion();
                    break;
                case 3:
                    friendRequest();
                    break;
                case 4:
                    manageRequests();
                    break;
                case 5:
                    displayFriends();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Wrong Choice");
                    break;
            }
        }
    }
}