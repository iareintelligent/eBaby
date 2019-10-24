package application;

import org.junit.*;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

import services.AuctionLogger;
import services.PostOffice;

public class AuctionTest {
    String firstName = "First";
    String lastName = "Last";
    String userEmail = "first.las@email.com";
    String userName = "coolUserName";
    String strangerName = "whoWantsCandy";
    String password = "abc123";
    Item item = new Item("plumbus", ItemCategory.OTHER);
    Item itemLuxuryCar = new Item("tesla", ItemCategory.CAR);
    Item itemCar = new Item("hooptie", ItemCategory.CAR);
    String logFile = "logfile.txt";
    Item itemDownloadableSoftware = new Item("this is software", ItemCategory.DOWNLOADABLE_SOFTWARE);
    LocalDateTime startTime= LocalDateTime.now().plusSeconds(1);
    LocalDateTime endTime = LocalDateTime.now().plusMinutes(10);
    Double startingPrice = 100.00;

    User testUser = new User(firstName,lastName,userEmail,userName,password);
    User stranger = new User(firstName, lastName, userEmail, strangerName, password);

    User bidder = new User("Bidder", lastName, "bidder@bidding.com", "bidderPerson", password);
    User seller = testUser;

    Users knownUsers = new Users(testUser);

    @Before
    public void setup(){
        startTime = LocalDateTime.now().plusSeconds(2);
        endTime = startTime.plusMinutes(10);
        knownUsers.register(bidder);
//        AuctionLogger.getInstance().clearLog(Auction.expensiveLog);
//        AuctionLogger.getInstance().clearLog(Auction.carLog);
    }

    @Test(expected = AuctionInstantiationException.class)
    public void unauthentSellerCantStartAuction() {
        Auction failedAuction = new Auction(testUser, item, startingPrice, startTime, endTime);
    }


        @Test(expected = AuctionInstantiationException.class)
    public void authenticatedNonSellerCantStartAuction() {
        knownUsers.login(testUser.userName(),testUser.password());
        Auction failedAuction = new Auction(testUser,item,startingPrice,startTime,endTime);

    }

    @Test
    public void authenticatedSellerCanStartAuction() {
        knownUsers.login(testUser.userName(),testUser.password());
        knownUsers.annointSeller(testUser.userName());
        Auction testAuction = new Auction(knownUsers.getByUserName(testUser.userName()), item, startingPrice, startTime, endTime);

        assertNotNull(testAuction);
        assertEquals(AuctionStatus.CREATED,testAuction.status());
    }

    @Test(expected = AuctionInstantiationException.class)
    public void userCantStartAuctionInThePast() {
        LocalDateTime now = LocalDateTime.now();
        knownUsers.login(testUser.userName(),testUser.password());
        knownUsers.annointSeller(testUser.userName());

        Auction inASec = new Auction(testUser, item, startingPrice, now.minusSeconds(1), endTime);
    }

    @Test(expected = AuctionInstantiationException.class)
    public void auctionLastsLongerThanZeroSeconds() {
        LocalDateTime now = LocalDateTime.now();
        knownUsers.login(testUser.userName(),testUser.password());
        knownUsers.annointSeller(testUser.userName());

        Auction inNoTime = new Auction(testUser, item, startingPrice, now, now);
    }


    @Test
    public void testStartAuctionToAcceptBids(){

        knownUsers.login(userName,password);
        knownUsers.annointSeller(userName);

        Auction testAuction = new Auction(testUser, item, startingPrice, startTime, endTime);

        testAuction.onStart();

        assertEquals(AuctionStatus.STARTED,testAuction.status());
    }

    @Test
    public void happyPathBid() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        assertEquals(firstBid,testAuction.highestBid());
    }

    //not Authenticated bidder is rejected
    @Test(expected = UserLoginException.class)
    public void testBidderThatIsNotAuthenticatedCantBid(){
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

    }
    //Bid < startingPrice its rejected
    @Test(expected = BidAmountException.class)
    public void testLowballingTheInitialBid() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice - 5, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);
    }
    //Bid <= highestBid it is rejected.
    @Test(expected = BidAmountException.class)
    public void bidIsLessThanHighestBidIsRejected() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.submitBid(firstBid);


    }
    //Bid when status != STARTED is rejected
    @Test
    public void anAuctionThatIsNotStartedWillRejectBids() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);


        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        try {
            testAuction.submitBid(firstBid);
            fail("I expected an exception because the auction was not started.");
        }catch (AuctionStatusException e){
            //This is expected.
        }
        assertNull(testAuction.highestBid());

//        testAuction.onStart();
//
//        testAuction.submitBid(firstBid);
//
//        testAuction.onClose();// This would be the only way to get to CLOSED.
//
//        Bid higherBid = new Bid(bidder,firstBid.amount+0.01,firstBid.time.plusSeconds(3));
//        try {
//            testAuction.submitBid(higherBid);
//        }catch(Exception e){
//
//        }




    }
    //Bid bidder == seller is rejected
    @Test(expected = DontSellYourselfShortException.class)
    public void dontSellYourselfShort() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(seller, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);
    }

    //Auction closing:
    @Test
    public void auctionClosedHappyPath () {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);

        testAuction.onStart();

        testAuction.onClose();

        assertTrue(testAuction.status() == AuctionStatus.CLOSED);

    }
    @Test(expected = AuctionStatusException.class)
    public void auctionCanOnlyCloseIfStartedTest () {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onClose();
    }

//    private
    @Test
    public void noOneWantsYourStuff() {
        String noBidderMessage = "Sorry, your auction for " + item.description() + " did not have any bidders";
        String toEmailAddress = seller.userEmail();
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();
        testAuction.onClose();

        PostOffice postOffice = testAuction.postOffice();
        assertTrue(postOffice.doesLogContain(toEmailAddress,noBidderMessage));

    }

    @Test
    public void testFeesForDownloadableSoftware() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, itemDownloadableSoftware, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        PostOffice postOffice = testAuction.postOffice();

        String sellerNotificationMessage = "Your " + "this is software" + " auction sold to bidder " + bidder.userEmail() + " for " + 98. + ".";
        String highestBidderNotificationMessage = "Congratulations! You won an auction for a(n) " + "this is software" + " from " + seller.userEmail() + " for $" + 100;
        assertTrue(postOffice.doesLogContain(seller.userEmail(),sellerNotificationMessage));
        assertTrue(postOffice.doesLogContain(bidder.userEmail(),highestBidderNotificationMessage));
    }

    @Test
    public void testFeesForNonDownloadableSoftwareOrCar() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        PostOffice postOffice = testAuction.postOffice();

        String sellerNotificationMessage = "Your " + item.description() + " auction sold to bidder " + bidder.userEmail() + " for " + 98. + ".";
        String highestBidderNotificationMessage = "Congratulations! You won an auction for a(n) " + item.description() + " from " + seller.userEmail() + " for $" + 110;
        assertTrue(postOffice.doesLogContain(seller.userEmail(),sellerNotificationMessage));
        assertTrue(postOffice.doesLogContain(bidder.userEmail(),highestBidderNotificationMessage));
    }

    @Test
    public void testFeesForCarUnder50k() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, itemCar, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, startingPrice, startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        PostOffice postOffice = testAuction.postOffice();

        String sellerNotificationMessage = "Your hooptie auction sold to bidder " + bidder.userEmail() + " for " + 98. + ".";
        String highestBidderNotificationMessage = "Congratulations! You won an auction for a(n) hooptie from " + seller.userEmail() + " for $" + 1100;
        assertTrue(postOffice.doesLogContain(seller.userEmail(),sellerNotificationMessage));
        assertTrue(postOffice.doesLogContain(bidder.userEmail(),highestBidderNotificationMessage));
    }

    @Test
    public void testFeesForCar50kOrMore() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, itemLuxuryCar, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, 50000., startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        PostOffice postOffice = testAuction.postOffice();

        String sellerNotificationMessage = "Your tesla auction sold to bidder " + bidder.userEmail() + " for " + 49000. + ".";
        String highestBidderNotificationMessage = "Congratulations! You won an auction for a(n) tesla from " + seller.userEmail() + " for $" + 53000.;
        assertTrue(postOffice.doesLogContain(seller.userEmail(),sellerNotificationMessage));
        assertTrue(postOffice.doesLogContain(bidder.userEmail(),highestBidderNotificationMessage));
        assertTrue(testAuction.status() == AuctionStatus.CLOSED);
    }

    @Test
    public void testLoggingCarSale(){
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, itemCar, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, 9000., startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        assertFalse(AuctionLogger.getInstance().findMessage(Auction.expensiveLog, testAuction.toString()));
        assertTrue(AuctionLogger.getInstance().findMessage(Auction.carLog, testAuction.toString()));

    }

    @Test
    public void testLoggingSaleOver10k() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, 50000., startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        assertTrue(AuctionLogger.getInstance().findMessage(Auction.expensiveLog, testAuction.toString()));
        assertFalse(AuctionLogger.getInstance().findMessage(Auction.carLog, testAuction.toString()));
    }

    @Test
    public void testLoggingCarSaleOver10k() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, itemLuxuryCar, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, 50000., startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        assertTrue(AuctionLogger.getInstance().findMessage(Auction.expensiveLog, testAuction.toString()));
        assertTrue(AuctionLogger.getInstance().findMessage(Auction.carLog, testAuction.toString()));
    }

    @Test
    public void testNoLogging() {
        knownUsers.login(seller.userName(), seller.password());
        knownUsers.annointSeller(seller.userName());

        knownUsers.login(bidder.userName(), bidder.password());

        Auction testAuction = new Auction(seller, item, startingPrice, startTime, endTime);
        testAuction.onStart();

        Bid firstBid = new Bid(bidder, 500., startTime.plusMinutes(2));

        testAuction.submitBid(firstBid);

        testAuction.onClose();

        assertFalse(AuctionLogger.getInstance().findMessage(Auction.expensiveLog, testAuction.toString()));
        assertFalse(AuctionLogger.getInstance().findMessage(Auction.carLog, testAuction.toString()));

    }


    //add $10 shipping to bidder price
    //add $1k shipping fee for item.category == car
    //add 4% luxury fee for item.category == car and item.price > $50k

}
