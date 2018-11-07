/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services;

import com.global.service.model.BillResponse;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author HungNT
 */
@Service
public interface BillService {

    public abstract List<BillResponse> findByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit);

    public abstract BigInteger countByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile);

    public abstract BillResponse getById(Long billId);
}
