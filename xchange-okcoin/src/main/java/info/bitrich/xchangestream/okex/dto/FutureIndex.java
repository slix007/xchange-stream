package info.bitrich.xchangestream.okex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Sergey Shurmin on 6/24/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FutureIndex {
    private BigDecimal index;
    private Date timestamp;

    public FutureIndex(@JsonProperty("futureIndex") BigDecimal index,
                       @JsonProperty("timestamp") Date timestamp) {
        this.index = index;
        this.timestamp = timestamp;
    }

    public BigDecimal getIndex() {
        return index;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
