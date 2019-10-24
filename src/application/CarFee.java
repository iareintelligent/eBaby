package application;

public class CarFee implements CloseProcessor {
    private final Auction auction;

    public CarFee(Auction auction) {
        this.auction = auction;
    }

    public static CarFee getInstance(Auction auction) {
        return new CarFee(auction);
    }

    @Override
    public void process() {
        if (auction.item.category() == ItemCategory.CAR && auction.highestBid().amount < 50000) {
            auction.setBuyerAmount(auction.highestBid().amount + 1000);
        }
    }
}
