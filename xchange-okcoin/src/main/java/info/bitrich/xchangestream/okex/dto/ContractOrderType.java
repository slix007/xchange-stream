package info.bitrich.xchangestream.okex.dto;

/**
 * Created by Sergey Shurmin on 6/20/17.
 */
public enum ContractOrderType {
    OPEN_LONG_POSITION_BUY("1"),
    OPEN_SHORT_POSITION_SELL("2"),
    LIQUIDATE_LONG_POSITION("3"),
    LIQUIDATE_SHORT_POSITION("4");

    private String value;

    ContractOrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
