package info.bitrich.xchangestream.okex.dto;

/**
 * Created by Sergey Shurmin on 6/20/17.
 */
public enum ContractType {
    THIS_WEEK("this_week"),
    NEXT_WEEK("next_week"),
    QUARTER("quarter");

    private String value;

    ContractType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
