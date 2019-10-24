package application;

public class NeitherSoftwareNorCarFee implements CloseProcessor {
    private final Auction auction;

    public NeitherSoftwareNorCarFee(Auction auction) {
        this.auction = auction;
    }

    public static NeitherSoftwareNorCarFee getInstance(Auction auction) {
        return new NeitherSoftwareNorCarFee(auction);
    }

    @Override
    public void process() {
        if (auction.item.category() == ItemCategory.OTHER) {
            auction.setBuyerAmount(auction.highestBid().amount + 10.);
        }
    }
}
