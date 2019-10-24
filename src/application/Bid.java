package application;

import java.time.LocalDateTime;
import java.util.Objects;

public class Bid {
    User bidder;
    Double amount;
    LocalDateTime time;

    public Bid(User bidder, Double amount, LocalDateTime time) {
        this.bidder = bidder;
        this.amount = amount;
        this.time = time;
    }


}
