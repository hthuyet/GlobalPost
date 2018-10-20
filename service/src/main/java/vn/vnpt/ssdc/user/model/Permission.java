package vn.vnpt.ssdc.user.model;

import vn.vnpt.ssdc.jdbc.SsdcEntity;

import java.util.Set;

/**
 * Created by Lamborgini on 5/4/2017.
 */
public class Permission extends SsdcEntity<Long> {

    public String name;
    public String groupName;
    public String description;
    public Set<String> operationIds;

}
