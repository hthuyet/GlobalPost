/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services.impl;

import com.global.service.model.BillLog;
import com.global.service.repository.BranchRepo;
import com.global.service.services.BillLogService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author HungNT
 */
public class BillLogServiceImpl implements BillLogService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BranchRepo branchRepo;

    private static final String SQL_GET_BY_BILL_CODE = "SELECT d.`id`,d.`bill_id`,d.`bill_code`,d.`content`,d.`user_id`,e.`user_name`,d.`created`  FROM bill_log d LEFT JOIN `user` e on d.`user_id` = e.`id` WHERE 1 = 1 ";

    @Override
    public List findByBillCode(String code) {
        List rtn = null;
        String sql = SQL_GET_BY_BILL_CODE;
        if (StringUtils.isNoneBlank(code)) {
            sql += " AND d.bill_code = ? ";
        }

        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (StringUtils.isNoneBlank(code)) {
            query.setParameter(i++, code);
        }

        List<Object[]> li = (List<Object[]>) query.getResultList();
        rtn = new ArrayList<>();
        for (Object[] objects : li) {
            //Add to list
            rtn.add(convertToObject(objects));
        }
        return rtn;
    }

    //<editor-fold defaultstate="collapsed" desc="convertToObject">
    private BillLog convertToObject(Object[] objects) {
        BillLog obj = new BillLog();
        int i = 0;
        obj.setId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillCode(String.valueOf(objects[i++]));
        obj.setContent(String.valueOf(objects[i++]));
        obj.setUserId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setUserName(String.valueOf(objects[i++]));
        obj.setCreated(Long.parseLong(String.valueOf(objects[i++])));
        return obj;
    }//</editor-fold>

    @Override
    public int insertBillLong(BillLog billLog) {
        return 0;
    }
}
