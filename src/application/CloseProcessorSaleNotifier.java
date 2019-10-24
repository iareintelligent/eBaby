package application;

import services.PostOffice;

public class CloseProcessorSaleNotifier implements CloseProcessor {
    Auction endedAuction;
    PostOffice postOffice;

    public CloseProcessorSaleNotifier(Auction endedAuction) {
        this.endedAuction = endedAuction;
        postOffice = PostOffice.getInstance();
    }

    @Override
    public void process() {
        postOffice.sendEMail(endedAuction.seller.userEmail(),"Your " + endedAuction.item.description() + " auction sold to bidder " + endedAuction.highestBid().bidder.userEmail() + " for " + endedAuction.getSellerAmount() + ".");
        postOffice.sendEMail(endedAuction.highestBid().bidder.userEmail(),"Congratulations! You won an auction for a(n) " + endedAuction.item.description() + " from " + endedAuction.seller.userEmail() + " for $" + endedAuction.getBuyerAmount());

    }


}
