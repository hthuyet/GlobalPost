/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services.impl;

import com.global.service.model.BillResponse;
import com.global.service.repository.BillRepo;
import com.global.service.services.BillService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author HungNT
 */
@Component
@Transactional
public class BillServiceImpl implements BillService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BillRepo billRepo;

    private static final String SQL_FIND_BY_QUERY = "SELECT d.`id`,d.`bill_no`,d.`bill_type`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,d.`paid`,d.`created`,d.`updated`,d.`is_cod`,d.`cod_value`,d.`bill_state`,d.`who_pay`,\n"
            + "e.`customer_id` as `e_customer_id`,e.`send_name`,e.`send_address`,e.`send_mobile`,e.`send_time`,e.`send_date`,e.`send_by`,\n"
            + "f.`customer_id` as `f_customer_id`,f.`receive_name`,f.`receive_address`,f.`receive_mobile`,f.`receive_time`,f.`receive_date`,f.`receive_by`\n"
            + "FROM bill d\n"
            + "LEFT JOIN bill_send e\n"
            + "ON d.`id` = e.`bill_id`\n"
            + "LEFT JOIN bill_receive f\n"
            + "ON d.`id` = f.`bill_id`\n"
            + "WHERE 1 = 1 ";

    private static final String SQL_COUNT_BY_QUERY = "SELECT count(d.`id`)\n"
            + "FROM bill d\n"
            + "LEFT JOIN bill_send e\n"
            + "ON d.`id` = e.`bill_id`\n"
            + "LEFT JOIN bill_receive f\n"
            + "ON d.`id` = f.`bill_id`\n"
            + "WHERE 1 = 1 ";

    @Override
    public List<BillResponse> findByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit) {
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
        if (StringUtils.isNoneBlank(sName)) {
            sql += " AND e.send_name like ? ";
        }
        if (StringUtils.isNoneBlank(sMobile)) {
            sql += " AND e.send_mobile like ? ";
        }
        if (StringUtils.isNoneBlank(rName)) {
            sql += " AND f.receive_name like ? ";
        }
        if (StringUtils.isNoneBlank(rMobile)) {
            sql += " AND f.receive_mobile like ? ";
        }
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
        if (StringUtils.isNoneBlank(sName)) {
            query.setParameter(i++, "%" + sName + "%");
        }
        if (StringUtils.isNoneBlank(sMobile)) {
            query.setParameter(i++, "%" + sMobile + "%");
        }
        if (StringUtils.isNoneBlank(rName)) {
            query.setParameter(i++, "%" + rName + "%");
        }
        if (StringUtils.isNoneBlank(rMobile)) {
            query.setParameter(i++, "%" + rMobile + "%");
        }
        List<Object[]> li = (List<Object[]>) query.getResultList();
        rtn = new ArrayList<>();
        for (Object[] objects : li) {
            //Add to list
            rtn.add(convertToObject(objects));
        }
        return rtn;
    }

    private BillResponse convertToObject(Object[] objects) {
        BillResponse obj = new BillResponse();
        int i = 0;
        obj.setId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillNo(String.valueOf(objects[i++]));
        obj.setBillType(Integer.parseInt(String.valueOf(objects[i++])));
        obj.setWeight(Long.parseLong(String.valueOf(objects[i++])));
        obj.setCost(Long.parseLong(String.valueOf(objects[i++])));
        obj.setTotalCost(Long.parseLong(String.valueOf(objects[i++])));
        obj.setContent(String.valueOf(objects[i++]));
        obj.setPaid(Integer.parseInt(String.valueOf(objects[i++])));
        obj.setCreated(Long.parseLong(String.valueOf(objects[i++])));
        obj.setUpdated(Long.parseLong(String.valueOf(objects[i++])));
        obj.setIsCod(Integer.parseInt(String.valueOf(objects[i++])));
        obj.setCodValue(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillState(Integer.parseInt(String.valueOf(objects[i++])));
        obj.setWhoPay(Integer.parseInt(String.valueOf(objects[i++])));
        obj.setSendCustomer(Long.parseLong(String.valueOf(objects[i++])));
        obj.setSendName(String.valueOf(objects[i++]));
        obj.setSendAddress(String.valueOf(objects[i++]));
        obj.setSendMobile(String.valueOf(objects[i++]));
        obj.setSendTime(String.valueOf(objects[i++]));
        obj.setSendDate(String.valueOf(objects[i++]));
        obj.setSendBy(String.valueOf(objects[i++]));
        obj.setReceiveCustomer(Long.parseLong(String.valueOf(objects[i++])));
        obj.setReceiveName(String.valueOf(objects[i++]));
        obj.setReceiveAddress(String.valueOf(objects[i++]));
        obj.setReceiveMobile(String.valueOf(objects[i++]));
        obj.setReceiveTime(String.valueOf(objects[i++]));
        obj.setReceiveDate(String.valueOf(objects[i++]));
        obj.setReceiveBy(String.valueOf(objects[i++]));
        return obj;
    }

    @Override
    public BigInteger countByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile) {
        String sql = SQL_COUNT_BY_QUERY;
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
        if (StringUtils.isNoneBlank(sName)) {
            sql += " AND e.send_name like ? ";
        }
        if (StringUtils.isNoneBlank(sMobile)) {
            sql += " AND e.send_mobile like ? ";
        }
        if (StringUtils.isNoneBlank(rName)) {
            sql += " AND f.receive_name like ? ";
        }
        if (StringUtils.isNoneBlank(rMobile)) {
            sql += " AND f.receive_mobile like ? ";
        }
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
        if (StringUtils.isNoneBlank(sName)) {
            query.setParameter(i++, "%" + sName + "%");
        }
        if (StringUtils.isNoneBlank(sMobile)) {
            query.setParameter(i++, "%" + sMobile + "%");
        }
        if (StringUtils.isNoneBlank(rName)) {
            query.setParameter(i++, "%" + rName + "%");
        }
        if (StringUtils.isNoneBlank(rMobile)) {
            query.setParameter(i++, "%" + rMobile + "%");
        }
        BigInteger count = (BigInteger) query.getSingleResult();
        return count;
    }

    @Override
    public BillResponse getById(Long billId) {
        String sql = SQL_FIND_BY_QUERY;
        if (billId != 0) {
            sql += " AND d.id = ? ";
        }
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (billId != 0) {
            query.setParameter(i++, billId);
        }
        Object[] obj = (Object[]) query.getSingleResult();
        return convertToObject(obj);
    }
}
