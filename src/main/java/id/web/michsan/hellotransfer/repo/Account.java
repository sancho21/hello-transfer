package id.web.michsan.hellotransfer.repo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
public class Account {
    @Getter
    private String number;
    @Getter
    private String holder;
    @Getter
    private BigDecimal balance;
}
