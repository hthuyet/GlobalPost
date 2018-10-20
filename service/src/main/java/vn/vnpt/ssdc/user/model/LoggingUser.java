package vn.vnpt.ssdc.user.model;

import vn.vnpt.ssdc.jdbc.SsdcEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoggingUser extends SsdcEntity<Long> {
    public String session;
    public String username;
    public Map<Integer, LoggingUserAction> actions;

    public LoggingUser() {
        this.actions = new LinkedHashMap<Integer, LoggingUserAction>();
    }

}