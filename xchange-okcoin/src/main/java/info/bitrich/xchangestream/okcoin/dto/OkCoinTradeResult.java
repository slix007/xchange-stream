package info.bitrich.xchangestream.okcoin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkCoinTradeResult {

    private BigDecimal averagePrice;
    private BigDecimal completedTradeAmount;
    private Date createdDate;
    private Long orderId;
    private String sigTradeAmount;
    private String sigTradePrice;
    private Integer status;
    private String symbol;
    private BigDecimal tradeAmount;
    private BigDecimal tradePrice;
    private String tradeType;
    private String tradeUnitPrice;
    private String unTrade;

    public OkCoinTradeResult(@JsonProperty("averagePrice") BigDecimal averagePrice,
                             @JsonProperty("completedTradeAmount") BigDecimal completedTradeAmount,
                             @JsonProperty("createdDate") Date createdDate,
                             @JsonProperty("orderId") Long orderId,
                             @JsonProperty("sigTradeAmount") String sigTradeAmount,
                             @JsonProperty("sigTradePrice") String sigTradePrice,
                             @JsonProperty("status") Integer status,
                             @JsonProperty("symbol") String symbol,
                             @JsonProperty("tradeAmount") BigDecimal tradeAmount,
                             @JsonProperty("tradePrice") BigDecimal tradePrice,
                             @JsonProperty("tradeType") String tradeType,
                             @JsonProperty("tradeUnitPrice") String tradeUnitPrice,
                             @JsonProperty("unTrade") String unTrade) {
        this.averagePrice = averagePrice;
        this.completedTradeAmount = completedTradeAmount;
        this.createdDate = createdDate;
        this.orderId = orderId;
        this.sigTradeAmount = sigTradeAmount;
        this.sigTradePrice = sigTradePrice;
        this.status = status;
        this.symbol = symbol;
        this.tradeAmount = tradeAmount;
        this.tradePrice = tradePrice;
        this.tradeType = tradeType;
        this.tradeUnitPrice = tradeUnitPrice;
        this.unTrade = unTrade;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getCompletedTradeAmount() {
        return completedTradeAmount;
    }

    public void setCompletedTradeAmount(BigDecimal completedTradeAmount) {
        this.completedTradeAmount = completedTradeAmount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSigTradeAmount() {
        return sigTradeAmount;
    }

    public void setSigTradeAmount(String sigTradeAmount) {
        this.sigTradeAmount = sigTradeAmount;
    }

    public String getSigTradePrice() {
        return sigTradePrice;
    }

    public void setSigTradePrice(String sigTradePrice) {
        this.sigTradePrice = sigTradePrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeUnitPrice() {
        return tradeUnitPrice;
    }

    public void setTradeUnitPrice(String tradeUnitPrice) {
        this.tradeUnitPrice = tradeUnitPrice;
    }

    public String getUnTrade() {
        return unTrade;
    }

    public void setUnTrade(String unTrade) {
        this.unTrade = unTrade;
    }

    @Override
    public String toString() {
        return "OkCoinTradeResult{" +
                "averagePrice=" + averagePrice +
                ", completedTradeAmount=" + completedTradeAmount +
                ", createdDate=" + createdDate +
                ", orderId=" + orderId +
                ", sigTradeAmount='" + sigTradeAmount + '\'' +
                ", sigTradePrice='" + sigTradePrice + '\'' +
                ", status=" + status +
                ", symbol='" + symbol + '\'' +
                ", tradeAmount=" + tradeAmount +
                ", tradePrice=" + tradePrice +
                ", tradeType='" + tradeType + '\'' +
                ", tradeUnitPrice='" + tradeUnitPrice + '\'' +
                ", unTrade='" + unTrade + '\'' +
                '}';
    }
}
