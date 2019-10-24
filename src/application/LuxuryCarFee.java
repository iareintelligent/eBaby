package application;

public class LuxuryCarFee implements CloseProcessor {
    private final Auction auction;

    public LuxuryCarFee(Auction auction) {
        this.auction = auction;
    }

    public static LuxuryCarFee getInstance(Auction auction) {
        return new LuxuryCarFee(auction);
    }

    @Override
    public void process() {
        if (auction.item.category() == ItemCategory.CAR && auction.highestBid().amount >= 50000.) {
            auction.setBuyerAmount(auction.highestBid().amount * 1.04 + 1000);
        }
    }
}
