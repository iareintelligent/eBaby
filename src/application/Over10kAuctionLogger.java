package application;

import services.AuctionLogger;

public class Over10kAuctionLogger implements CloseProcessor {
    private final Auction auction;
    private AuctionLogger logger = AuctionLogger.getInstance();

    Over10kAuctionLogger(Auction auction){
        this.auction = auction;
    }

    public static Over10kAuctionLogger getInstance(Auction auction){
        return new Over10kAuctionLogger(auction);
    }
    @Override
    public void process() {
        if(auction.highestBid().amount > 10000.) {
            logger.log(Auction.expensiveLog, auction.toString());

        }

    }
}
