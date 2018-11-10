/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services;

import com.global.service.model.BillStock;
import com.global.service.model.BillStockForm;
import com.global.service.model.BillStockResponse;
import java.math.BigInteger;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author HungNT
 */
@Service
public interface BillStockService {

    public abstract List<BillStockResponse> findByQuery(String billNo, int state, Long from, Long to, int offset, int limit);

    public abstract BigInteger countByQuery(String billNo, int state, Long from, Long to);

    public abstract BillStockResponse getById(Long billId);

    public abstract BillStockResponse getByCode(String code);

    public abstract Boolean delete(Long id);

    public abstract BillStock save(BillStockForm billStockParameter);
}
