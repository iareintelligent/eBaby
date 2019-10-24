package application;

public class StandardTwoPercentSellerFee implements CloseProcessor {

    private final Auction auction;

    public StandardTwoPercentSellerFee(Auction auction) {
        this.auction = auction;
    }

    public static StandardTwoPercentSellerFee getInstance(Auction auction){
       return new StandardTwoPercentSellerFee(auction);
    }
    @Override
    public void process() {
        auction.setSellerAmount(auction.highestBid().amount * .98);
        auction.setBuyerAmount(auction.highestBid().amount);
    }
}
