package com.global.utils;

import org.apache.commons.net.util.SubnetUtils;

/**
 * Created by ThuyetLV
 */
public class IpUtil {

    /**
     * Check if ip address is belong to ip subnet or not
     *
     * @param remoteAddr ex: 1.232.12.3
     * @param ipRange : ex :1.232.12.0/20
     * @return
     */
    public static boolean isIpInRange(String remoteAddr, String ipRange) {
        SubnetUtils utils = new SubnetUtils(ipRange);
        SubnetUtils.SubnetInfo info = utils.getInfo();
        return info.isInRange(remoteAddr);
    }
}
