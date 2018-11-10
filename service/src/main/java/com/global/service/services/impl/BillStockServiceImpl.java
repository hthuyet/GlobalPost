/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services.impl;

import com.global.service.model.BillStock;
import com.global.service.model.BillStockForm;
import com.global.service.model.BillStockResponse;
import com.global.service.repository.BillStockRepo;
import com.global.service.services.BillStockService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author HungNT
 */
@Component
@Transactional
public class BillStockServiceImpl implements BillStockService {

    private static final Logger logger = LoggerFactory.getLogger(BillStockServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BillStockRepo billStockRepo;

    private static final String SQL_FIND_BY_QUERY = "SELECT d.`id`,d.`bill_no`,d.`state`,d.`customer_id`,e.`customer_name`, d.`created`,d.`used` FROM `bill_stock` d\n"
            + "LEFT JOIN `customer` e\n"
            + "ON d.`customer_id` = e.`id`\n"
            + "WHERE 1 = 1 ";

    private static final String SQL_COUNT_BY_QUERY = "SELECT count(d.`id`)\n"
            + "LEFT JOIN `customer` e\n"
            + "ON d.`customer_id` = e.`id`\n"
            + "WHERE 1 = 1 ";

    @Override
    public List<BillStockResponse> findByQuery(String billNo, int state, Long from, Long to, int offset, int limit) {
        List rtn = null;
        String sql = SQL_FIND_BY_QUERY;
        if (StringUtils.isNoneBlank(billNo)) {
            sql += " AND d.bill_no like ? ";
        }
        if (state > -1) {
            sql += " AND d.state = ? ";
        }
        if (from != 0) {
            sql += " AND d.created > ? ";
        }
        if (to != 0) {
            sql += " AND d.created < ? ";
        }
        sql += " ORDER BY p.id DESC LIMIT ?,?";
        logger.info("SQL: " + sql);
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (StringUtils.isNoneBlank(billNo)) {
            query.setParameter(i++, "%" + billNo + "%");
        }
        if (state > -1) {
            query.setParameter(i++, state);
        }
        if (from != 0) {
            query.setParameter(i++, from);
        }
        if (to != 0) {
            query.setParameter(i++, to);
        }
        query.setParameter(i++, offset);
        query.setParameter(i++, limit);
        List<Object[]> li = (List<Object[]>) query.getResultList();
        rtn = new ArrayList<>();
        for (Object[] objects : li) {
            //Add to list
            rtn.add(convertToObject(objects));
        }
        return rtn;
    }

    private BillStockResponse convertToObject(Object[] objects) {
        BillStockResponse obj = new BillStockResponse();
        int i = 0;
        obj.setId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillNo(String.valueOf(objects[i++]));
        obj.setState(Integer.parseInt(String.valueOf(objects[i++])));
        obj.setCustomerId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setCustomerName(String.valueOf(objects[i++]));
        obj.setCreated(Long.parseLong(String.valueOf(objects[i++])));
        obj.setUsed(Long.parseLong(String.valueOf(objects[i++])));
        return obj;
    }

    @Override
    public BigInteger countByQuery(String billNo, int state, Long from, Long to) {
        String sql = SQL_COUNT_BY_QUERY;
        if (StringUtils.isNoneBlank(billNo)) {
            sql += " AND d.bill_no like ? ";
        }
        if (state > -1) {
            sql += " AND d.bill_state = ? ";
        }
        if (from != 0) {
            sql += " AND d.created > ? ";
        }
        if (to != 0) {
            sql += " AND d.created < ? ";
        }
        logger.info("SQL: " + sql);
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (StringUtils.isNoneBlank(billNo)) {
            query.setParameter(i++, "%" + billNo + "%");
        }
        if (state > -1) {
            query.setParameter(i++, state);
        }
        if (from != 0) {
            query.setParameter(i++, from);
        }
        if (to != 0) {
            query.setParameter(i++, to);
        }
        BigInteger count = (BigInteger) query.getSingleResult();
        return count;
    }

    @Override
    public BillStockResponse getById(Long billId) {
        String sql = SQL_FIND_BY_QUERY;
        if (billId != 0) {
            sql += " AND d.id = ? ";
        }
        logger.info("SQL: " + sql);
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (billId != 0) {
            query.setParameter(i++, billId);
        }
        List<Object[]> li = (List<Object[]>) query.getResultList();
        if (li.size() > 0) {
            return convertToObject(li.get(0));
        }
        return null;
    }

    @Override
    public BillStockResponse getByCode(String code) {
        String sql = SQL_FIND_BY_QUERY;
        if (StringUtils.isNoneBlank(code)) {
            sql += " AND d.bill_no = ? ";
        }
        logger.info("SQL: " + sql);
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (StringUtils.isNoneBlank(code)) {
            query.setParameter(i++, code);
        }
        List<Object[]> li = (List<Object[]>) query.getResultList();
        if (li.size() > 0) {
            return convertToObject(li.get(0));
        }
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            billStockRepo.delete(id);
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public BillStock save(BillStockForm bill) {
        try {
            Long id = bill.id;
            BillStock obj = null;
            if (id != null && id > 0) {
                obj = billStockRepo.findOne(id);
                if (obj == null) {
                    return null;
                }
            } else {
                obj = new BillStock();
            }
            obj.id = bill.id;
            obj.billNo = bill.billNo;
            obj.state = bill.state;
            obj.customerId = bill.customerId;
            obj.created = bill.created;
            obj.used = bill.used;
            obj.billNo = bill.billNo;
            obj = billStockRepo.save(obj);
            return obj;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
}
