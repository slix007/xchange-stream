package info.bitrich.xchangestream.okex.dto;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;

import java.math.BigDecimal;

/**
 * Created by Sergey Shurmin on 6/23/17.
 */
public class BalanceEx extends Balance {

    public BalanceEx(Currency currency, BigDecimal total) {
        super(currency, total);
    }

    public BalanceEx(Currency currency, BigDecimal total, BigDecimal available) {
        super(currency, total, available);
    }

    public BalanceEx(Currency currency, BigDecimal total, BigDecimal available, BigDecimal frozen) {
        super(currency, total, available, frozen);
    }

    public BalanceEx(Currency currency, BigDecimal total, BigDecimal available, BigDecimal frozen, BigDecimal borrowed, BigDecimal loaned, BigDecimal withdrawing, BigDecimal depositing) {
        super(currency, total, available, frozen, borrowed, loaned, withdrawing, depositing);
    }

    /**
     * Returns a zero balance.
     *
     * @param currency the balance currency.
     * @return a zero balance.
     */
    public static BalanceEx zero(Currency currency) {

        return new BalanceEx(currency, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO);
    }

    private String raw;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
