package com.global.service.services.impl;

import com.global.exception.ObjExitsException;
import com.global.service.model.Customer;
import com.global.service.repository.CustomerRepo;
import com.global.service.services.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public CustomerRepo customerRepo;

  private static final String SQL_GET = "SELECT `id`,`code`,`name`,`tax_code`,`address`,`tax_address`,`mobile`,`email`,`note`,`created`,`updated`,user_id FROM customer c WHERE 1=1 ";
  private static final String SQL_COUNT = "SELECT count(id) FROM customer c WHERE 1=1 ";

  @Override
  public List findByQuery(String search, int offset, int limit) {
    List rtn = null;
    String sql = SQL_GET;
    if (StringUtils.isNoneBlank(search)) {
      sql += " AND (c.code LIKE ? OR c.name LIKE ? OR c.address LIKE ? OR c.mobile LIKE ? OR c.email LIKE ?) ";
    }

    sql += " ORDER BY c.id ";

    if (limit > 0) {
      sql += " LIMIT ?,?";
    }

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(search)) {
      query.setParameter(i++, "%" + search.toUpperCase() + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
    }

    if (limit > 0) {
      query.setParameter(i++, offset);
      query.setParameter(i++, limit);
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
  private Customer convertToObject(Object[] objects) {
    Customer obj = new Customer();
    int i = 0;
    obj.setId(Long.parseLong(String.valueOf(objects[i++])));
    obj.setCode(String.valueOf(objects[i++]));
    obj.setName(String.valueOf(objects[i++]));
    obj.setTaxCode(String.valueOf(objects[i++]));
    obj.setAddress(String.valueOf(objects[i++]));
    obj.setTaxAddress(String.valueOf(objects[i++]));
    obj.setMobile(String.valueOf(objects[i++]));
    obj.setEmail(String.valueOf(objects[i++]));
    obj.setNote(String.valueOf(objects[i++]));
    String tmp = String.valueOf(objects[i++]);
    if(tmp != null && !StringUtils.isBlank(tmp) && StringUtils.isNumeric(tmp)){
      obj.setUserId(Long.parseLong(tmp));
    }
    return obj;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="countByQuery">
  @Override
  public BigInteger countByQuery(String search) {
    String sql = SQL_COUNT;
    if (StringUtils.isNoneBlank(search)) {
      sql += " AND (c.code LIKE ? OR c.name LIKE ? OR c.address LIKE ? OR c.mobile LIKE ? OR c.email LIKE ?) ";
    }

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(search)) {
      query.setParameter(i++, "%" + search.toUpperCase() + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
    }

    BigInteger count = (BigInteger) query.getSingleResult();
    return count;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="save">
  @Override
  public Customer save(Customer entity) {
    entity.setCode(entity.getCode().toUpperCase());

    boolean checkExits = false;
    if (entity.getId() != null && entity.getId() > 0) {
      Customer db = customerRepo.findOne(entity.getId());
      if (db.getCode() == null && entity.getCode() != null) {
        checkExits = true;
      } else if (db.getCode() != null && entity.getCode() == null) {
        checkExits = true;
      } else {
        if (!db.getCode().toUpperCase().equalsIgnoreCase(entity.getCode().toUpperCase())) {
          //Thay doi gia tri code
          checkExits = true;
        }
      }
    } else {
      checkExits = true;
    }

    if (checkExits) {
      //Can check exits
      checkExits = checkExits(entity);
      if (checkExits) {
        throw new ObjExitsException("Customer code is exits!");
      }
    }


    entity = customerRepo.save(entity);
    return entity;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Delete">
  @Override
  public Boolean delete(Long id) {
    customerRepo.delete(id);
    return true;
  }//</editor-fold>

  private static final String SQL_CHECK_EXITS = "SELECT count(id) FROM customer WHERE code=? AND id !=?";

  @Override
  public Boolean checkExits(Customer entity) {
    String sql = SQL_CHECK_EXITS;
    Query query = em.createNativeQuery(sql);
    int i = 1;
    query.setParameter(i++, entity.getCode().toUpperCase());
    query.setParameter(i++, entity.getId());
    return ((BigInteger) query.getSingleResult()).intValue() > 0;
  }
}
