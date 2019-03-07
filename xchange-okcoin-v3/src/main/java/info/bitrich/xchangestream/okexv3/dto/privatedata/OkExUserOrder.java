package info.bitrich.xchangestream.okexv3.dto.privatedata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkExUserOrder {

    private BigDecimal leverage;
    private String clientOid;
    private BigDecimal size;
    private BigDecimal filledQty;
    private BigDecimal price;
    private BigDecimal fee;
    private BigDecimal contractVal;
    private BigDecimal priceAvg;
    private String type;
    private Integer orderType;
    private String instrumentId;
    private String orderId;
    private Date timestamp;
    private Integer status;

    public OkExUserOrder(
            @JsonProperty("leverage") BigDecimal leverage,
            @JsonProperty("client_oid") String clientOid,
            @JsonProperty("size") BigDecimal size,
            @JsonProperty("filled_qty") BigDecimal filledQty,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("fee") BigDecimal fee,
            @JsonProperty("contract_val") BigDecimal contractVal,
            @JsonProperty("price_avg") BigDecimal priceAvg,
            @JsonProperty("type") String type,
            @JsonProperty("order_type") Integer orderType,
            @JsonProperty("instrument_id") String instrumentId,
            @JsonProperty("order_id") String orderId,
            @JsonProperty("timestamp") Date timestamp,
            @JsonProperty("status") Integer status
    ) {
        this.leverage = leverage;
        this.clientOid = clientOid;
        this.size = size;
        this.filledQty = filledQty;
        this.price = price;
        this.fee = fee;
        this.contractVal = contractVal;
        this.priceAvg = priceAvg;
        this.type = type;
        this.orderType = orderType;
        this.instrumentId = instrumentId;
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.status = status;
    }


    public BigDecimal getLeverage() {
        return leverage;
    }

    public String getClientOid() {
        return clientOid;
    }

    public BigDecimal getSize() {
        return size;
    }

    public BigDecimal getFilledQty() {
        return filledQty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getContractVal() {
        return contractVal;
    }

    public BigDecimal getPriceAvg() {
        return priceAvg;
    }

    public String getType() {
        return type;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "OkExUserOrder{" +
                "leverage=" + leverage +
                ", clientOid='" + clientOid + '\'' +
                ", size=" + size +
                ", filledQty=" + filledQty +
                ", price=" + price +
                ", fee=" + fee +
                ", contractVal=" + contractVal +
                ", priceAvg=" + priceAvg +
                ", type='" + type + '\'' +
                ", orderType=" + orderType +
                ", instrumentId='" + instrumentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
}
