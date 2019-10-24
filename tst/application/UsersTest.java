package application;

import org.junit.*;
import static org.junit.Assert.*;

public class UsersTest {

    String firstName = "First";
    String lastName = "Last";
    String userEmail = "first.last@email.com";
    String userName = "coolUserName";
    String password = "abc123";

    User testUser = new User(firstName,lastName,userEmail,userName,password);


    @Test
    public void register() {
        User testUser2 = new User(firstName,lastName,userEmail,userName+"2",password);

        Users registerSpot = new Users();
        registerSpot.register(testUser);
        registerSpot.register(testUser2);

        assertSame(testUser,registerSpot.getByUserName(userName));
        assertSame(testUser2,registerSpot.getByUserName(testUser2.userName()));
    }

    @Test(expected = DuplicateUserNameException.class)
    public void duplicateUserIsRejected(){
        Users registerSpot = new Users();
        registerSpot.register(testUser);
        registerSpot.register(testUser);
    }

    @Test
    public void logInUserSuccess() {
        Users login = new Users(testUser);

        login.login(userName,password);
        assertTrue(login.getByUserName(userName).loggedIn());
    }

    @Test(expected = UserLoginException.class)
    public void testLoginWithInvalidUserName(){
        Users loginStranger = new Users(testUser);

        loginStranger.login("iAmGroot", password);

    }

    @Test(expected = UserLoginException.class)
    public void testLoginWithInvalidPassword(){
        Users login = new Users(testUser);
        login.login(userName, "wrongPassword");
    }

    @Test
    public void testLogout() {
        Users logoutUsers = new Users(testUser);
        logoutUsers.login(userName,password);

        logoutUsers.logout(testUser);

        assertFalse(testUser.loggedIn());
    }

    @Test
    public void testLogoutWithUserThatIsNotPartOfRegistry(){
        Users logoutUsers = new Users(testUser);
        logoutUsers.login(userName,password);

        User userInstanceOutsideRegistry = new User(firstName, lastName, userEmail, userName, password);
        assertTrue(testUser.loggedIn());
        assertFalse(userInstanceOutsideRegistry.loggedIn());

        logoutUsers.logout(userInstanceOutsideRegistry);

        assertFalse(testUser.loggedIn());
    }

    @Test
    public void testAUserCanBeUpdatedToBeASeller(){
        Users newSeller = new Users(testUser);
        assertFalse(testUser.seller());

        newSeller.annointSeller(userName);

        User byUserName = newSeller.getByUserName(userName);
        assertTrue(byUserName.seller());

    }

}
