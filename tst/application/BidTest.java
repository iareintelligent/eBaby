package application;

import org.junit.*;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class BidTest {

    String firstName = "First";
    String lastName = "Last";
    String userEmail = "first.las@email.com";
    String userName = "coolUserName";
    String password = "abc123";
    User bidder = new User(firstName, lastName, userEmail, userName, password);
    Users knownUser = new Users(bidder);
    Double amount = 10.00;
    LocalDateTime time = LocalDateTime.now();
//    Double largeAmount = 51.00;


    @Before
    public void setup() {
        knownUser.login(bidder.userName(), bidder.password());
    }

    @Test
    public void testRetrieveParametersAfterConstruction(){
        Bid testBid = new Bid(bidder, amount, time);

        assertEquals(bidder, testBid.bidder);
        assertEquals(amount, testBid.amount);
        assertEquals(time, testBid.time);
    }
}
