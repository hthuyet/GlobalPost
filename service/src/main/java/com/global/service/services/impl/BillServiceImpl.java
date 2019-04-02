/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services.impl;

import com.global.exception.ObjExitsException;
import com.global.service.model.*;
import com.global.service.model.report.EmployeeReport;
import com.global.service.repository.BillReceiveRepo;
import com.global.service.repository.BillRepo;
import com.global.service.repository.BillSendRepo;
import com.global.service.repository.CustomerRepo;
import com.global.service.services.BillService;
import com.global.service.utils.GlobalConstants;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
  public CustomerRepo customerRepo;

  @Autowired
  public BillReceiveRepo billReceiveRepo;

  private static final String SQL_FIND_BY_QUERY = "SELECT d.`id`,d.`bill_no`,d.`bill_type`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,d.`paid`,d.`created`,d.`updated`,d.`is_cod`,d.`cod_value`,d.`bill_state`,d.`who_pay`,d.`user_create`,d.`branch_create`,d.`current_branch`,d.`partner_id`,d.`employee_send`,d.`employee_receive`,\n"
      + "e.`customer_id` as `e_customer_id`,e.`send_name`,e.`send_address`,e.`send_mobile`,e.`send_time`,e.`send_date`,e.`send_by`,\n"
      + "f.`customer_id` as `f_customer_id`,f.`receive_name`,f.`receive_address`,f.`receive_mobile`,f.`receive_time`,f.`receive_date`,f.`receive_by`,\n"
      + "g.`user_name`,\n"
      + "h.`branch_name` as `h_branch_name`,\n"
      + "i.`branch_name` as `i_branch_name`,\n"
      + "j.`part_name`, "
      + "d.`pay_type` "
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
      + "LEFT JOIN `partner` j\n"
      + "ON d.`partner_id` = j.`id`\n"
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
      + "LEFT JOIN `partner` j\n"
      + "ON d.`partner_id` = j.`id`\n"
      + "WHERE 1 = 1 AND d.`bill_state` != 5 ";

  private static final String SQL_FIND_BILL_RECEIVE_BY_ID = "SELECT `id`,`bill_id`,`customer_id`,`receive_name`,`receive_address`,`receive_mobile`,`receive_time`,`receive_date`,`receive_by` FROM `bill_receive` WHERE 1 = 1 ";
  private static final String SQL_FIND_BILL_SEND_BY_ID = "SELECT `id`,`bill_id`,`customer_id`,`send_name`,`send_address`,`send_mobile`,`send_time`,`send_date`,`send_by` FROM `bill_send` WHERE 1 = 1 ";

  private static final String SQL_DELETE_BILL_IN_BILL_SEND = "DELETE FROM `bill_send` where `bill_id` = ?";
  private static final String SQL_DELETE_BILL_IN_BILL_RECEIVE = "DELETE FROM `bill_receive` where `bill_id` = ?";
  private static final String SQL_DELETE_BILL = "DELETE FROM `bill` where `id` = ?";
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

    Object tmp = objects[i++];
    if (tmp != null) {
      obj.setWeight(Long.parseLong(String.valueOf(tmp)));
    }

    tmp = objects[i++];
    if (tmp != null) {
      obj.setCost(Long.parseLong(String.valueOf(tmp)));
    }

    tmp = objects[i++];
    if (tmp != null) {
      obj.setTotalCost(Long.parseLong(String.valueOf(tmp)));
    }
    obj.setContent(String.valueOf(objects[i++]));
    tmp = objects[i++];
    if (tmp != null) {
      obj.setPaid(Integer.parseInt(String.valueOf(tmp)));
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setCreated(Long.parseLong(String.valueOf(tmp)));
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setUpdated(Long.parseLong(String.valueOf(tmp)));
    }
    obj.setIsCod(Integer.parseInt(String.valueOf(objects[i++])));
    tmp = objects[i++];
    if (tmp != null) {
      obj.setCodValue(new BigInteger(String.valueOf(tmp)).longValue());
    }

    tmp = objects[i++];
    if (tmp != null) {
      obj.setBillState(Integer.parseInt(String.valueOf(tmp)));
    }

    tmp = objects[i++];
    if (tmp != null) {
      obj.setWhoPay(Integer.parseInt(String.valueOf(tmp)));
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setUserCreate(Long.parseLong(String.valueOf(tmp)));
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setBranchCreate(Long.parseLong(String.valueOf(tmp)));
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setCurrentBranch(Long.parseLong(String.valueOf(tmp)));
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setPartnerId(new BigInteger(String.valueOf(tmp)).longValue());
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setEmployeeSend(new BigInteger(String.valueOf(tmp)).longValue());
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setEmployeeReceive(new BigInteger(String.valueOf(tmp)).longValue());
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setSendCustomer(new BigInteger(String.valueOf(tmp)).longValue());
    }
    obj.setSendName(String.valueOf(objects[i++]));
    obj.setSendAddress(String.valueOf(objects[i++]));
    obj.setSendMobile(String.valueOf(objects[i++]));
    obj.setSendTime(String.valueOf(objects[i++]));
    obj.setSendDate(String.valueOf(objects[i++]));
    obj.setSendBy(String.valueOf(objects[i++]));
    tmp = objects[i++];
    if (tmp != null) {
      obj.setReceiveCustomer(new BigInteger(String.valueOf(tmp)).longValue());
    }
    obj.setReceiveName(String.valueOf(objects[i++]));
    obj.setReceiveAddress(String.valueOf(objects[i++]));
    obj.setReceiveMobile(String.valueOf(objects[i++]));
    obj.setReceiveTime(String.valueOf(objects[i++]));
    obj.setReceiveDate(String.valueOf(objects[i++]));
    obj.setReceiveBy(String.valueOf(objects[i++]));
    obj.setUserNameCreate(String.valueOf(objects[i++]));
    obj.setBranchNameCreate(String.valueOf(objects[i++]));
    obj.setCurrentBranchName(String.valueOf(objects[i++]));
    obj.setPartnerName(String.valueOf(objects[i++]));
    tmp = objects[i++];
    if (tmp != null) {
      obj.setPayType(Integer.parseInt(String.valueOf(tmp)));
    }
    return obj;
  }

  private BillSend convertToSendObject(Object[] objects) {
    BillSend obj = new BillSend();
    int i = 0;
    obj.setId(Long.parseLong(String.valueOf(objects[i++])));
    obj.setBillId(Long.parseLong(String.valueOf(objects[i++])));
    String customerId = String.valueOf(objects[i++]);
    obj.setCustomerId("".equals(customerId) || "null".equalsIgnoreCase(customerId) ? null : Long.parseLong(customerId));
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
      sql += " AND `bill_id` = ? ";
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
      sql += " AND `bill_id` = ? ";
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

//  private BillResponse saveBill(EntityManager emm,BillResponse obj){
//    String sql = "update bill set bill_no=?, bill_state=?, bill_type=?, branch_create=?, cod_value=?, content=?, cost=?, created=?, current_branch=?, employee_receive=?, employee_send=?, is_cod=?, paid=?, partner_id=?, total_cost=?, updated=?, user_create=?, weight=?, who_pay=? where id=?";
//    Query query = em.createNativeQuery(sql);
//    int i = 1;
//    if (billId != 0) {
//      query.setParameter(i++, billId);
//    }
//    List<Object[]> li = (List<Object[]>) query.getResultList();
//    if (li.size() > 0) {
//      return convertToReceiveObject(li.get(0));
//    }
//    return null;
//  }

  @Override
  public Bill save(BillForm bill) {
    try {
      //Save saveSender
      if (bill.saveSender > 0) {
        Customer customer = new Customer();
        customer.name = bill.sendName;
        customer.mobile = bill.sendMobile;
        customer.address = bill.sendAddress;
        customer = customerRepo.save(customer);

        bill.sendCustomer = customer.id;
      }

      //Save saveReceiver
      if (bill.saveReceiver > 0) {
        Customer rCustomer = new Customer();
        rCustomer.name = bill.receiveName;
        rCustomer.mobile = bill.receiveMobile;
        rCustomer.address = bill.receiveAddress;
        rCustomer = customerRepo.save(rCustomer);
        bill.receiveCustomer = rCustomer.id;
      }

      Long id = bill.id;
      Bill obj = null;
      BillSend objSend = null;
      BillReceive objReceive = null;
      if (id != null && id > 0) {
        obj = billRepo.findOne(id);
        if (obj == null) {
          return null;
        }
        obj.setUpdated(System.currentTimeMillis());
      } else {
        obj = new Bill();
        obj.setCreated(System.currentTimeMillis());

        //Check ton tai code
        BillResponse exit = getByCode(bill.billNo);
        if (exit != null && exit.getId() > 0) {
          throw new ObjExitsException("Bill No exits");
        }

      }
      obj.setBillNo(bill.billNo);
      obj.setBillType(bill.billType);
      obj.setBillState(bill.billState);
      obj.setBranchCreate(bill.branchCreate);
      obj.setCodValue(bill.codValue);
      obj.setContent(bill.content);
      obj.setCost(bill.cost);
      obj.setCurrentBranch(bill.currentBranch);
      obj.setIsCod(bill.isCod);
      obj.setPaid(bill.paid);
      obj.setTotalCost(bill.totalCost);
      obj.setUserCreate(bill.userCreate);
      obj.setWeight(bill.weight);
      obj.setWhoPay(bill.whoPay);
      obj.setBranchCreate(bill.branchCreate);
      obj.setPartnerId(bill.partnerId);
      obj.setEmployeeSend(bill.employeeSend);
      obj.setEmployeeReceive(bill.employeeReceive);
      obj.setPayType(bill.payType);
      obj.setWhoPay(bill.whoPay);
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
      logger.error("ERROR save: ", ex);
      return null;
    }
  }

  private static final String SQL_QUERY_IMPORT = "UPDATE bill set bill_state=?,current_branch=? WHERE id IN (%list%)";
  private static final String SQL_QUERY_EXPORT = "UPDATE bill set bill_state=? WHERE id IN (%list%)";

  @Override
  public Integer exeImport(int state, Long currentBranch, List<Long> lstId) {
    if (lstId == null || lstId.isEmpty())
      return 0;
    String sql = SQL_QUERY_IMPORT;
    sql = sql.replaceAll("%list%", StringUtils.join(lstId, ","));
    Query query = em.createNativeQuery(sql);
    int i = 1;
    query.setParameter(i++, state);
    query.setParameter(i++, currentBranch);
    return query.executeUpdate();
  }

  @Override
  public Integer exeExport(int state, List<Long> lstId) {
    if (lstId == null || lstId.isEmpty())
      return 0;
    String sql = SQL_QUERY_EXPORT;
    sql = sql.replaceAll("%list%", StringUtils.join(lstId, ","));
    Query query = em.createNativeQuery(sql);
    int i = 1;
    query.setParameter(i++, state);
    return query.executeUpdate();
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

  @Override
  public List<BillResponse> findImport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit) {
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

  @Override
  public List<BillResponse> findExport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit) {
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

  @Override
  public BigInteger countImport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile) {
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
  public BigInteger countExport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile) {
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


  //For report
  private static final String SQL_REPORT_BY_EMPLOY = "SELECT d.`id`,d.`bill_no`,e.`send_date`,e.`send_time`,f.`receive_date`,f.`receive_time`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,e.send_address,f.receive_address "
      + "FROM bill d\n"
      + "LEFT JOIN bill_send e\n"
      + "ON d.`id` = e.`bill_id`\n"
      + "LEFT JOIN bill_receive f\n"
      + "ON d.`id` = f.`bill_id`\n"
      + "WHERE 1 = 1 ";

  @Override
  public List<EmployeeReport> reportByEmployee(Long employ, Date startDate, Date endDate) {
    List rtn = null;
    String sql = SQL_REPORT_BY_EMPLOY;
    if (employ != null) {
      sql += " AND (d.employee_send = ? OR d.employee_receive=?) ";
    }
    sql += " ORDER BY d.id ASC";
    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (employ != null) {
      query.setParameter(i++, employ);
      query.setParameter(i++, employ);
    }
    List<Object[]> li = (List<Object[]>) query.getResultList();
    rtn = new ArrayList<>();
    i = 1;
    for (Object[] objects : li) {
      //Add to list
      rtn.add(convertToReportEmploy(i++, objects));
    }
    return rtn;
  }

  private EmployeeReport convertToReportEmploy(int stt, Object[] objects) {
    EmployeeReport obj = new EmployeeReport(stt);
    int i = 1;
    Object tmp = objects[i++];
    if (tmp != null) {
      obj.setBillNo(String.valueOf(tmp));
    }
    String time = "";
    tmp = objects[i++];
    if (tmp != null) {
      time += String.valueOf(tmp) + " ";
    }
    tmp = objects[i++];
    if (tmp != null) {
      time += String.valueOf(tmp);
    }
    tmp = objects[i++];
    if (tmp != null) {
      time += String.valueOf(tmp) + " ";
    }
    tmp = objects[i++];
    if (tmp != null) {
      time += String.valueOf(tmp);
    }
    //Set time
    obj.setTime(time);

    tmp = objects[i++];
    if (tmp != null && String.valueOf(tmp).trim().length() > 0) {
      obj.setWeight(String.valueOf(tmp));
    } else {
      obj.setWeight("0");
    }

    i++; //cost

    tmp = objects[i++];
    if (tmp != null && String.valueOf(tmp).trim().length() > 0) {
      obj.setTotalCost(String.valueOf(tmp));
    } else {
      obj.setTotalCost("");
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setNotes(tmp.toString());
    } else {
      obj.setNotes("");
    }

    tmp = objects[i++];
    if (tmp != null) {
      obj.setSendAddress(tmp.toString());
    } else {
      obj.setSendAddress("");
    }
    tmp = objects[i++];
    if (tmp != null) {
      obj.setReceiverAddress(tmp.toString());
    } else {
      obj.setReceiverAddress("");
    }
    return obj;
  }


  private static final String SQL_REPORT_BY_CUSTOMER = "SELECT d.`id`,d.`bill_no`,e.`send_date`,e.`send_time`,f.`receive_date`,f.`receive_time`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,e.send_address,f.receive_address "
      + "FROM bill d\n"
      + "LEFT JOIN bill_send e\n"
      + "ON d.`id` = e.`bill_id`\n"
      + "LEFT JOIN bill_receive f\n"
      + "ON d.`id` = f.`bill_id`\n"
      + "WHERE 1 = 1 ";

  @Override
  public List<EmployeeReport> reportByCustomer(Long customer, Date startDate, Date endDate) {
    List rtn = null;
    String sql = SQL_REPORT_BY_CUSTOMER;
    if (customer != null) {
      sql += " AND (e.customer_id = ? OR f.customer_id=?) ";
    }
    sql += " ORDER BY d.id ASC";
    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (customer != null) {
      query.setParameter(i++, customer);
      query.setParameter(i++, customer);
    }
    List<Object[]> li = (List<Object[]>) query.getResultList();
    rtn = new ArrayList<>();
    i = 1;
    for (Object[] objects : li) {
      //Add to list
      rtn.add(convertToReportEmploy(i++, objects));
    }
    return rtn;
  }

  private static final String SQL_REPORT_BY_PARTNER = "SELECT d.`id`,d.`bill_no`,e.`send_date`,e.`send_time`,f.`receive_date`,f.`receive_time`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,e.send_address,f.receive_address "
      + "FROM bill d\n"
      + "LEFT JOIN bill_send e\n"
      + "ON d.`id` = e.`bill_id`\n"
      + "LEFT JOIN bill_receive f\n"
      + "ON d.`id` = f.`bill_id`\n"
      + "WHERE 1 = 1 ";

  @Override
  public List<EmployeeReport> reportByPartner(Long partner, Date startDate, Date endDate) {
    List rtn = null;
    String sql = SQL_REPORT_BY_PARTNER;
    if (partner != null) {
      sql += " AND d.partner_id = ? ";
    }
    sql += " ORDER BY d.id ASC";
    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (partner != null) {
      query.setParameter(i++, partner);
    }
    List<Object[]> li = (List<Object[]>) query.getResultList();
    rtn = new ArrayList<>();
    i = 1;
    for (Object[] objects : li) {
      //Add to list
      rtn.add(convertToReportEmploy(i++, objects));
    }
    return rtn;
  }

  private static final String SQL_REPORT_BY_BRANCH = "SELECT d.`id`,d.`bill_no`,e.`send_date`,e.`send_time`,f.`receive_date`,f.`receive_time`,d.`weight`,d.`cost`,d.`total_cost`,d.`content`,e.send_address,f.receive_address "
      + "FROM bill d\n"
      + "LEFT JOIN bill_send e\n"
      + "ON d.`id` = e.`bill_id`\n"
      + "LEFT JOIN bill_receive f\n"
      + "ON d.`id` = f.`bill_id`\n"
      + "WHERE 1 = 1 ";

  @Override
  public List<EmployeeReport> reportByBranch(Long branch, Date startDate, Date endDate) {
    List rtn = null;
    String sql = SQL_REPORT_BY_BRANCH;
    if (branch != null) {
      sql += " AND (d.branch_create = ? OR d.current_branch = ?) ";
    }
    sql += " ORDER BY d.id ASC";
    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (branch != null) {
      query.setParameter(i++, branch);
      query.setParameter(i++, branch);
    }
    List<Object[]> li = (List<Object[]>) query.getResultList();
    rtn = new ArrayList<>();
    i = 1;
    for (Object[] objects : li) {
      //Add to list
      rtn.add(convertToReportEmploy(i++, objects));
    }
    return rtn;
  }
}
