package com.global.service.services.impl;

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

  private static final String SQL_GET = "SELECT `id`,`code`,`name`,`tax_code`,`address`,`tax_address`,`mobile`,`email`,`note`,`created`,`updated` FROM customer c WHERE 1=1 ";
  private static final String SQL_COUNT = "SELECT count(id) FROM customer c WHERE 1=1 ";

  @Override
  public List findByQuery(String name, int offset, int limit) {
    List rtn = null;
    String sql = SQL_GET;
    if (StringUtils.isNoneBlank(name)) {
      sql += " AND c.name LIKE ? ";
    }

    sql += " ORDER BY c.id ";

    if (limit > 0) {
      sql += " LIMIT ?,?";
    }

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(name)) {
      query.setParameter(i++, "%" + name + "%");
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
    return obj;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="countByQuery">
  @Override
  public BigInteger countByQuery(String name) {
    String sql = SQL_COUNT;
    if (StringUtils.isNoneBlank(name)) {
      sql += " AND c.name LIKE ? ";
    }

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(name)) {
      query.setParameter(i++, "%" + name + "%");
    }

    BigInteger count = (BigInteger) query.getSingleResult();
    return count;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="save">
  @Override
  public Customer save(Customer entity) {
    entity = customerRepo.save(entity);
    return entity;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Delete">
  @Override
  public Boolean delete(Long id) {
    customerRepo.delete(id);
    return true;
  }//</editor-fold>
}
