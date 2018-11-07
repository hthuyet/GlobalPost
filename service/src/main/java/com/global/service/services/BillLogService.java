/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services;

import com.global.service.model.BillLog;
import java.util.List;

/**
 *
 * @author HungNT
 */
public interface BillLogService {

    public abstract List findByBillCode(String code);

    public abstract BillLog insertBillLong(BillLog billLog);
}
