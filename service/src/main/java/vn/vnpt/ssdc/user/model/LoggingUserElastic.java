package vn.vnpt.ssdc.user.model;

import com.google.gson.annotations.SerializedName;
import io.searchbox.annotations.JestId;

public class LoggingUserElastic {
    @JestId
    public String _id;
    public String message;
    @SerializedName("@timestamp")
    public String timestamp;

}