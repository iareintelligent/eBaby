package application;

public class User {
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userName;
    private String password;
    private boolean loggedIn = false;
    private boolean seller = false;


    public User(String firstName, String lastName, String userEmail, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;


    }
        public String firstName() {
            return this.firstName;
        }
        public String lastName() {
            return this.lastName;
        }

        public String userEmail() {
            return this.userEmail;
        }
        public String userName() {
            return this.userName;
        }
        public String password() {
            return this.password;
        }


    public boolean loggedIn() {
        return this.loggedIn;
    }

    public void loggedIn(boolean loggedIn) {
        this.loggedIn=loggedIn;
    }

    public boolean seller() {
        return seller;
    }

    public void seller(boolean seller) {
        this.seller=seller;
    }
}
