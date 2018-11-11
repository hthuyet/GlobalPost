/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services.impl;

import com.global.service.model.Bill;
import com.global.service.model.BillForm;
import com.global.service.model.BillReceive;
import com.global.service.model.BillResponse;
import com.global.service.model.BillSend;
import com.global.service.repository.BillReceiveRepo;
import com.global.service.repository.BillRepo;
import com.global.service.repository.BillSendRepo;
import com.global.service.services.BillService;
import com.global.service.utils.GlobalConstants;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
public class BillServiceImpl implements BillService {

    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BillRepo billRepo;

    @Autowired
    public BillSendRepo billSendRepo;

    @Autowired
    public BillReceiveRepo billReceiveRepo;

    private static final String SQL_FIND_BY_QUERY = "SELECT d.`id`,d.`bill_no`,d.`bill_type`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,d.`paid`,d.`created`,d.`updated`,d.`is_cod`,d.`cod_value`,d.`bill_state`,d.`who_pay`,d.`user_create`,d.`branch_create`,d.`current_branch`,\n"
            + "e.`customer_id` as `e_customer_id`,e.`send_name`,e.`send_address`,e.`send_mobile`,e.`send_time`,e.`send_date`,e.`send_by`,\n"
            + "f.`customer_id` as `f_customer_id`,f.`receive_name`,f.`receive_address`,f.`receive_mobile`,f.`receive_time`,f.`receive_date`,f.`receive_by`,\n"
            + "g.`user_name`,\n"
            + "h.`branch_name` as `h_branch_name`,\n"
            + "i.`branch_name` as `i_branch_name`\n"
            + "FROM bill d\n"
            + "LEFT JOIN bill_send e\n"
            + "ON d.`id` = e.`bill_id`\n"
            + "LEFT JOIN bill_receive f\n"
            + "ON d.`id` = f.`bill_id`\n"
            + "LEFT JOIN `user` g\n"
            + "ON d.`user_create` = g.`id`\n"
            + "LEFT JOIN `branch` h\n"
            + "ON d.`branch_create` = h.`id`\n"
            + "LEFT JOIN `branch` i\n"
            + "ON d.`current_branch` = i.`id`\n"
            + "WHERE 1 = 1 AND d.`bill_state` != 5 ";

    private static final String SQL_COUNT_BY_QUERY = "SELECT count(d.`id`)\n"
            + "FROM bill d\n"
            + "LEFT JOIN bill_send e\n"
            + "ON d.`id` = e.`bill_id`\n"
            + "LEFT JOIN bill_receive f\n"
            + "ON d.`id` = f.`bill_id`\n"
            + "LEFT JOIN `user` g\n"
            + "ON d.`user_create` = g.`id`\n"
            + "LEFT JOIN `branch` h\n"
            + "ON d.`branch_create` = h.`id`\n"
            + "LEFT JOIN `branch` i\n"
            + "ON d.`current_branch` = i.`id`\n"
            + "WHERE 1 = 1 AND d.`bill_state` != 5 ";

    private static final String SQL_FIND_BILL_RECEIVE_BY_ID = "SELECT `id`,`bill_id`,`customer_id`,`receive_name`,`receive_address`,`receive_mobile`,`receive_time`,`receive_date`,`receive_by` FROM `bill_receive` WHERE 1 = 1 ";
    private static final String SQL_FIND_BILL_SEND_BY_ID = "SELECT `id`,`bill_id`,`customer_id`,`send_name`,`send_address`,`send_mobile`,`send_time`,`send_date`,`send_by` WHERE 1 = 1 ";

    private static final String SQL_DELETE_BILL_IN_BILL_SEND = "DELETE `bill_send` where `bill_id` = ?";
    private static final String SQL_DELETE_BILL_IN_BILL_RECEIVE = "DELETE `bill_receive` where `bill_id` = ?";
    private static final String SQL_DELETE_BILL = "DELETE `bill` where `id` = ?";
    private static final String SQL_CHANGE_BILL_STATE = "UPDATE `bill` SET `state` = ? where `id` = ?";

    @Override
    public List<BillResponse> findByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit) {
        List rtn = null;
        String sql = SQL_FIND_BY_QUERY;
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
        sql += " ORDER BY d.id DESC LIMIT ?,?";
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
        obj.setUserCreate(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBranchCreate(Long.parseLong(String.valueOf(objects[i++])));
        obj.setCurrentBranch(Long.parseLong(String.valueOf(objects[i++])));
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
        obj.setUserNameCreate(String.valueOf(objects[i++]));
        obj.setBranchNameCreate(String.valueOf(objects[i++]));
        obj.setCurrentBranchName(String.valueOf(objects[i++]));
        return obj;
    }

    private BillSend convertToSendObject(Object[] objects) {
        BillSend obj = new BillSend();
        int i = 0;
        obj.setId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillId(Long.parseLong(String.valueOf(objects[i++])));
        String customerId = String.valueOf(objects[i++]);
        obj.setCustomerId("".equals(customerId) ? null : Long.parseLong(customerId));
        obj.setSendName(String.valueOf(objects[i++]));
        obj.setSendAddress(String.valueOf(objects[i++]));
        obj.setSendMobile(String.valueOf(objects[i++]));
        obj.setSendTime(String.valueOf(objects[i++]));
        obj.setSendDate(String.valueOf(objects[i++]));
        obj.setSendBy(String.valueOf(objects[i++]));
        return obj;
    }

    private BillReceive convertToReceiveObject(Object[] objects) {
        BillReceive obj = new BillReceive();
        int i = 0;
        obj.setId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setBillId(Long.parseLong(String.valueOf(objects[i++])));
        String customerId = String.valueOf(objects[i++]);
        obj.setCustomerId("".equals(customerId) ? null : Long.parseLong(customerId));
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
            sql += " AND d.bill_state = ? ";
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

    private BillSend getBillSendByBillId(Long billId) {
        String sql = SQL_FIND_BILL_SEND_BY_ID;
        if (billId != 0) {
            sql += " AND `id` = ? ";
        }
        logger.info("SQL: " + sql);
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (billId != 0) {
            query.setParameter(i++, billId);
        }
        List<Object[]> li = (List<Object[]>) query.getResultList();
        if (li.size() > 0) {
            return convertToSendObject(li.get(0));
        }
        return null;
    }

    private BillReceive getBillReceiveByBillId(Long billId) {
        String sql = SQL_FIND_BILL_RECEIVE_BY_ID;
        if (billId != 0) {
            sql += " AND `id` = ? ";
        }
        logger.info("SQL: " + sql);
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (billId != 0) {
            query.setParameter(i++, billId);
        }
        List<Object[]> li = (List<Object[]>) query.getResultList();
        if (li.size() > 0) {
            return convertToReceiveObject(li.get(0));
        }
        return null;
    }

    private int changeBillState(Long id, int newState) {
        try {
            String sql = SQL_CHANGE_BILL_STATE;
            logger.info("SQL: " + sql);
            Query query = em.createNativeQuery(sql);
            int i = 1;
            query.setParameter(i++, newState);
            query.setParameter(i++, id);
            return query.executeUpdate();
        } catch (Exception ex) {
            return 0;
        }
    }

    private void completeDeleteBill(Long id) {
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.createNativeQuery(SQL_DELETE_BILL_IN_BILL_SEND).executeUpdate();
            em.createNativeQuery(SQL_DELETE_BILL_IN_BILL_RECEIVE).executeUpdate();
            em.createNativeQuery(SQL_DELETE_BILL).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }

    @Override
    public Boolean delete(Long id) {
        BillResponse bill = getById(id);
        if (bill != null) {
            switch (bill.getBillState()) {
                case GlobalConstants.BILL_INIT_STATE:
                    return false;
                case GlobalConstants.BILL_IN_PROCESSING_STATE:
                    return false;
                case GlobalConstants.BILL_FINISH_STATE:
                    changeBillState(bill.getId(), GlobalConstants.BILL_HAS_DELETED_STATE);
                    return true;
                case GlobalConstants.BILL_HAS_PROBLEM_STATE:
                    return false;
                case GlobalConstants.BILL_HAS_DELETED_STATE:
                    completeDeleteBill(id);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public Bill save(BillForm bill) {
        try {
            Long id = bill.id;
            Bill obj = null;
            BillSend objSend = null;
            BillReceive objReceive = null;
            if (id != null && id > 0) {
                obj = billRepo.findOne(id);
                if (obj == null) {
                    return null;
                }
            } else {
                obj = new Bill();
            }
            obj.id = bill.id;
            obj.billNo = bill.billNo;
            obj.billType = bill.billType;
            obj.billState = bill.billState;
            obj.branchCreate = bill.branchCreate;
            obj.codValue = bill.codValue;
            obj.content = bill.content;
            obj.cost = bill.cost;
            obj.created = bill.created;
            obj.currentBranch = bill.currentBranch;
            obj.isCod = bill.isCod;
            obj.paid = bill.paid;
            obj.totalCost = bill.totalCost;
            obj.updated = bill.updated;
            obj.userCreate = bill.userCreate;
            obj.weight = bill.weight;
            obj.whoPay = bill.whoPay;
            obj = billRepo.save(obj);
            if (obj != null && obj.getId() > 0) {
                objSend = getBillSendByBillId(obj.getId());
                if (objSend == null) {
                    objSend = new BillSend();
                }
                objSend.billId = obj.getId();
                objSend.customerId = bill.sendCustomer;
                objSend.sendName = bill.sendName;
                objSend.sendAddress = bill.sendAddress;
                objSend.sendMobile = bill.sendMobile;
                objSend.sendTime = bill.sendTime;
                objSend.sendDate = bill.sendDate;
                objSend.sendBy = bill.sendBy;
                billSendRepo.save(objSend);
                objReceive = getBillReceiveByBillId(obj.getId());
                if (objReceive == null) {
                    objReceive = new BillReceive();
                }
                objReceive.billId = obj.getId();
                objReceive.customerId = bill.receiveCustomer;
                objReceive.receiveName = bill.receiveName;
                objReceive.receiveAddress = bill.receiveAddress;
                objReceive.receiveMobile = bill.receiveMobile;
                objReceive.receiveTime = bill.receiveTime;
                objReceive.receiveDate = bill.receiveDate;
                objReceive.receiveBy = bill.receiveBy;
                billReceiveRepo.save(objReceive);
                return obj;
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public BillResponse getByCode(String code) {
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
}
