package application;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class ClosedAuctionFactory {
    public static List<CloseProcessor> getProcessor(Auction auction) {
        List<CloseProcessor> processorChain = new ArrayList<CloseProcessor>();
        if(nonNull(auction.highestBid())) {
            //Sale 2%
            processorChain.add(StandardTwoPercentSellerFee.getInstance(auction));
            //If its Other

            processorChain.add(NeitherSoftwareNorCarFee.getInstance(auction));
            //else if it is car
            processorChain.add(LuxuryCarFee.getInstance(auction));
            //if it is over 50k add the lux
            processorChain.add(CarFee.getInstance(auction));
            //add the car shipping

            processorChain.add(new CloseProcessorSaleNotifier(auction));
            processorChain.add(CarAuctionLogger.getInstance(auction));
            processorChain.add(Over10kAuctionLogger.getInstance(auction));
        }else{
            processorChain.add(new CloseProcessorNoSaleNotifier(auction));
        }
        return processorChain;
    }
}
