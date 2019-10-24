package application;

import services.PostOffice;

public class CloseProcessorNoSaleNotifier implements CloseProcessor {
    private final Auction auction;
    private final PostOffice postOffice;

    public CloseProcessorNoSaleNotifier(Auction auction) {
        this.auction = auction;
        this.postOffice = PostOffice.getInstance();
    }

    @Override
    public void process() {
        postOffice.sendEMail(auction.seller.userEmail(),"Sorry, your auction for " + auction.item.description() + " did not have any bidders");
    }
}
