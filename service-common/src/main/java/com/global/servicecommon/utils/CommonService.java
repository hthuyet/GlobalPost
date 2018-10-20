/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.servicecommon.utils;

import com.google.common.base.Strings;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * Created by ThuyetLV
 */
@Component
public class CommonService {

    public String generateSearchLikeInput(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(input);
        sb.append("%");
        return sb.toString();
    }

    public boolean validateNotNullString(String input) {
        return !Strings.isNullOrEmpty(input);
    }

    public boolean validateAllowValue(String input, List<String> valueAllows) {
        if (valueAllows == null || valueAllows.isEmpty()) {
            return false;
        }
        return valueAllows.contains(input);
    }

    public boolean validateLengthInput(String input, int lower, int higher) {
        if (validateNotNullString(input)) {
            if (input.length() >= lower && input.length() <= higher) {
                return true;
            }
        }
        return false;
    }
}
