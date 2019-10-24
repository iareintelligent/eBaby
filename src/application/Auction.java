package application;

import java.time.LocalDateTime;
import java.util.List;

import services.PostOffice;

public class Auction {

    public static final String expensiveLog = "expensiveStuff.txt";
    public static final String carLog = "cars.txt";

    User seller;
    Item item;
    double startingPrice;
    LocalDateTime startTime;
    LocalDateTime endTime;
    private AuctionStatus status;
    private Bid highestBid;
    private PostOffice postOffice;
    private double sellerAmount;
    private double buyerAmount;

    public Auction(User seller, Item item, double startingPrice, LocalDateTime startTime, LocalDateTime endTime) {
        if(!seller.loggedIn() || !seller.seller()){
            throw new AuctionInstantiationException();
        }

        this.seller = seller;
        this.item = item;
        this.startingPrice = startingPrice;
        this.startTime = checkStartTime(startTime);
        this.endTime = checkEndTime(endTime);
        this.status = AuctionStatus.CREATED;
        this.postOffice = PostOffice.getInstance();
    }

    public double getSellerAmount() {
        return sellerAmount;
    }

    public void setSellerAmount(double sellerAmount) {
        this.sellerAmount = sellerAmount;
    }

    public double getBuyerAmount() {
        return buyerAmount;
    }

    public void setBuyerAmount(double buyerAmount) {
        this.buyerAmount = buyerAmount;
    }

    private LocalDateTime checkStartTime(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isAfter(now.plusSeconds(1))) {
            return startTime;
        }
        throw new AuctionInstantiationException();
    }
    private LocalDateTime checkEndTime(LocalDateTime endTime) {

        if (endTime.isAfter(startTime.plusSeconds(1))) {
            return endTime;
        }
        throw new AuctionInstantiationException();
    }


    public AuctionStatus status() {
        return this.status;
    }

    public void onStart() {
        this.status = AuctionStatus.STARTED;
    }

    public void submitBid(Bid newBid) {
        if(!newBid.bidder.loggedIn()){
            throw new UserLoginException();
        }
        if (newBid.amount < this.startingPrice  || (highestBid!=null && newBid.amount <= highestBid.amount)) {
            throw new BidAmountException();
        }
        if(this.status!=AuctionStatus.STARTED){
            throw new AuctionStatusException();
        }
        if(newBid.bidder == this.seller) {
            throw new DontSellYourselfShortException();
        }

        this.highestBid = newBid;
    }

    public Bid highestBid() {
        return this.highestBid;
    }

    public void onClose() {
        if (this.status != AuctionStatus.STARTED) {
            throw new AuctionStatusException();
        }
        this.status = AuctionStatus.CLOSED;



        List<CloseProcessor> processors = ClosedAuctionFactory.getProcessor(this);

        for(CloseProcessor processor:processors) {
            processor.process();
        }

    }

    public PostOffice postOffice() {
        return postOffice;
    }
}
