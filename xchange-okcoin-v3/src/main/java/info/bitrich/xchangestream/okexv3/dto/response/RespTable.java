package info.bitrich.xchangestream.okexv3.dto.response;

import java.util.List;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class RespTable {

    private String table;
    private String action; // only
    private List<Object> data;

//    public RespEvent(@JsonProperty("event") String event, @JsonProperty("channel") String channel) {
//        this.event = event;
//        this.channel = channel;
//        this.table = null;
//        this.action = null;
//        this.data = null;
//    }
//
//    public RespEvent(@JsonProperty("table") String table, @JsonProperty("data") List<Object> data) {
//        this.event = null;
//        this.channel = null;
//        this.table = table;
//        this.action = null;
//        this.data = data;
//    }
//
//    public RespEvent(@JsonProperty("table") String table,
//            @JsonProperty("action") String action,
//            @JsonProperty("data") List<Object> data) {
//        this.event = null;
//        this.channel = null;
//        this.table = table;
//        this.action = action;
//        this.data = data;
//    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getTable() {
        return table;
    }

    public String getAction() {
        return action;
    }

    public List<Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RespTable{" +
                "table='" + table + '\'' +
                ", action='" + action + '\'' +
                ", data=" + data +
                '}';
    }
}
