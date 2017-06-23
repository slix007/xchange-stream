package info.bitrich.xchangestream.okex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkExTradeResult {

    private BigDecimal amount;
    private BigDecimal contractId;
    private String contractName;
    private String contractType;
    private Date createDate;
    private String createDateStr;
    private BigDecimal dealAmount;
    private BigDecimal fee;
    private Integer leverRate;
    private Long orderId;
    private BigDecimal price;
    private BigDecimal priceAvg;
    private Integer status;
    private Integer systemType;
    private Integer type;
    private Integer unitAmount;
    private Integer userId;


    public OkExTradeResult(@JsonProperty("amount") BigDecimal amount,
                           @JsonProperty("contract_id") BigDecimal contractId,
                           @JsonProperty("contract_name") String contractName,
                           @JsonProperty("contract_type") String contractType,
                           @JsonProperty("create_date") Date createDate,
                           @JsonProperty("create_date_str") String createDateStr,
                           @JsonProperty("deal_amount") BigDecimal dealAmount,
                           @JsonProperty("fee") BigDecimal fee,
                           @JsonProperty("lever_rate") Integer leverRate,
                           @JsonProperty("orderid") Long orderId,
                           @JsonProperty("price") BigDecimal price,
                           @JsonProperty("price_avg") BigDecimal priceAvg,
                           @JsonProperty("status") Integer status,
                           @JsonProperty("system_type") Integer systemType,
                           @JsonProperty("type") Integer type,
                           @JsonProperty("unit_amount") Integer unitAmount,
                           @JsonProperty("user_id") Integer userId) {
        this.amount = amount;
        this.contractId = contractId;
        this.contractName = contractName;
        this.contractType = contractType;
        this.createDate = createDate;
        this.createDateStr = createDateStr;
        this.dealAmount = dealAmount;
        this.fee = fee;
        this.leverRate = leverRate;
        this.orderId = orderId;
        this.price = price;
        this.priceAvg = priceAvg;
        this.status = status;
        this.systemType = systemType;
        this.type = type;
        this.unitAmount = unitAmount;
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getContractId() {
        return contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public String getContractType() {
        return contractType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public Integer getLeverRate() {
        return leverRate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPriceAvg() {
        return priceAvg;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public Integer getType() {
        return type;
    }

    public Integer getUnitAmount() {
        return unitAmount;
    }

    public Integer getUserId() {
        return userId;
    }
}
