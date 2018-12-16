package com.global.service.services.impl;

import com.global.service.model.Employee;
import com.global.service.model.EmployeeResponse;
import com.global.service.repository.EmployeeRepo;
import com.global.service.services.EmployeeService;
import org.apache.commons.lang3.StringUtils;
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
public class EmployeeServiceImpl implements EmployeeService {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public EmployeeRepo employeeRepo;

  private static final String SQL_GET = "SELECT d.`id`,d.`full_name`,d.`mobile`,d.`address`,d.`branch_id`,e.`branch_name` FROM `employee` d LEFT JOIN `branch` e ON d.`branch_id` = e.`id` WHERE 1 = 1 ";
  private static final String SQL_COUNT = "SELECT COUNT(d.`id`) FROM `employee` d LEFT JOIN `branch` e ON d.`branch_id` = e.`id` WHERE 1 = 1 ";

  @Override
  public List findByQuery(String search, Long branchId, int offset, int limit) {
    List rtn = null;
    String sql = SQL_GET;
    if (StringUtils.isNoneBlank(search)) {
      sql += " AND (d.full_name LIKE ? OR d.address LIKE ? OR d.mobile LIKE ? OR e.`branch_name` LIKE ?) ";
    }
    if (branchId != null && branchId != 0) {
      sql += " AND d.branch_id = ? ";
    }

    if (limit > 0) {
      sql += " ORDER BY d.id DESC LIMIT ?,?";
    }

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(search)) {
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
    }
    if (branchId != null && branchId != 0) {
      query.setParameter(i++, branchId);
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
  private EmployeeResponse convertToObject(Object[] objects) {
    EmployeeResponse obj = new EmployeeResponse();
    int i = 0;
    obj.setId(Long.parseLong(String.valueOf(objects[i++])));
    obj.setFullName(String.valueOf(objects[i++]));
    obj.setMobile(String.valueOf(objects[i++]));
    obj.setAddress(String.valueOf(objects[i++]));
    obj.setBranchId(Long.parseLong(String.valueOf(objects[i++])));
    obj.setBranchName(String.valueOf(objects[i++]));
    return obj;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="countByQuery">
  @Override
  public BigInteger countByQuery(String search, Long branchId) {
    String sql = SQL_COUNT;
    if (StringUtils.isNoneBlank(search)) {
      sql += " AND (d.full_name LIKE ? OR d.address LIKE ? OR d.mobile LIKE ? OR e.`branch_name` LIKE ?) ";
    }
    if (branchId != null && branchId != 0) {
      sql += " AND d.branch_id = ? ";
    }

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(search)) {
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
      query.setParameter(i++, "%" + search + "%");
    }
    if (branchId != null && branchId != 0) {
      query.setParameter(i++, branchId);
    }
    BigInteger count = (BigInteger) query.getSingleResult();
    return count;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="save">
  @Override
  public Employee save(Employee entity) {
    entity = employeeRepo.save(entity);
    return entity;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="delete">
  @Override
  public Boolean delete(Long id) {
    employeeRepo.delete(id);
    return true;
  }//</editor-fold>
}
