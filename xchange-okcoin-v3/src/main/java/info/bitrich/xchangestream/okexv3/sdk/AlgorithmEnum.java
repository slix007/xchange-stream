package info.bitrich.xchangestream.okexv3.sdk;

public enum AlgorithmEnum {
    HMAC_SHA256("HmacSHA256"),
    MD5("MD5");

    private String algorithm;

    private AlgorithmEnum(String algorithm) {
        this.algorithm = algorithm;
    }

    public String algorithm() {
        return this.algorithm;
    }
}