package application;

import services.AuctionLogger;

public class CarAuctionLogger implements CloseProcessor {
    private final Auction auction;
    private AuctionLogger logger = AuctionLogger.getInstance();

    CarAuctionLogger(Auction auction){
        this.auction = auction;
    }

    public static CarAuctionLogger getInstance(Auction auction){
        return new CarAuctionLogger(auction);
    }
    @Override
    public void process() {
        if(auction.item.category() == ItemCategory.CAR) {
            logger.log(Auction.carLog, auction.toString());
        }

    }
}
