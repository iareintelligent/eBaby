package application;

import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {
    String firstName = "First";
    String lastName = "Last";
    String userEmail = "first.las@email.com";
    String userName = "coolUserName";
    String password = "abc123";
    @Test
    public void testRetrieveParametersAfterConstruction(){


        User testUser = new User(firstName,lastName,userEmail,userName,password);

        assertEquals(firstName,testUser.firstName());
        assertEquals(lastName,testUser.lastName());
        assertEquals(userEmail,testUser.userEmail());
        assertEquals(userName,testUser.userName());
        assertEquals(password,testUser.password());


    }

    @Test
    public void testUserCanBeChangedToASeller(){
        User testUser = new User(firstName,lastName,userEmail,userName,password);
        assertFalse(testUser.seller());
        testUser.seller(true);
        assertTrue(testUser.seller());

    }
}
