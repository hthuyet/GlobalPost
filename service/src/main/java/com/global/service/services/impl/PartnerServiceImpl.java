package com.global.service.services.impl;

import com.global.service.model.Partner;
import com.global.service.repository.PartnerRepo;
import com.global.service.services.PartnerService;
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
public class PartnerServiceImpl implements PartnerService {
  private static final Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public PartnerRepo partnerRepo;

  private static final String SQL_GET = "SELECT `id`,`name` FROM partner p WHERE 1=1 ";
  private static final String SQL_COUNT = "SELECT count(id) FROM partner p WHERE 1=1 ";

  @Override
  public List findByQuery(String name, int offset, int limit){
    List rtn = null;
    String sql = SQL_GET;
    if (StringUtils.isNoneBlank(name)) {
      sql += " AND p.name LIKE ? ";
    }

    sql += " ORDER BY p.id DESC LIMIT ?,?";

    Query query = em.createNativeQuery(sql);

    int i = 1;
    if (StringUtils.isNoneBlank(name)) {
      query.setParameter(i++, "%" + name + "%");
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

  //<editor-fold defaultstate="collapsed" desc="convertToObject">
  private Partner convertToObject(Object[] objects) {
    Partner obj = new Partner();
    int i = 0;
    obj.setId(Long.parseLong(String.valueOf(objects[i++])));
    obj.setName(String.valueOf(objects[i++]));
    return obj;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="countByQuery">
  @Override
  public BigInteger countByQuery(String name) {
    String sql = SQL_COUNT;
    if (StringUtils.isNoneBlank(name)) {
      sql += " AND p.name LIKE ? ";
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
  public Partner save(Partner entity) {
    entity = partnerRepo.save(entity);
    return entity;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Delete">
  @Override
  public Boolean delete(Long id) {
    partnerRepo.delete(id);
    return true;
  }//</editor-fold>
}
