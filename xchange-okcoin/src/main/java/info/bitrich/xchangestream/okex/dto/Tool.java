package info.bitrich.xchangestream.okex.dto;

public enum Tool {
    BTC("btc"), LTC("ltc"), ETH("eth"), ETC("etc"), BCH("bch");
    String name;

    Tool(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
