package com.global.service.services.impl;

import com.global.service.model.Branch;
import com.global.service.repository.BranchRepo;
import com.global.service.services.BranchService;
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
public class BranchServiceImpl implements BranchService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BranchRepo branchRepo;

    private static final String SQL_GET = "SELECT `id`,`branch_name`,`branch_address`,`branch_hotline` FROM branch d WHERE 1=1 ";
    private static final String SQL_COUNT = "SELECT count(id) FROM branch d WHERE 1=1 ";
    private static final String SQL_UPDATE_USER_WHEN_UPDATE_BRANCH = "UPDATE `user` set `branch_name` = ? where `branch_id` = ?";
    private static final String SQL_COUNT_USER_BY_BRANCH = "SELECT count(`id`) FROM `user` d WHERE 1 = 1 ";

    @Override
    public List findByQuery(String name, int offset, int limit) {
        List rtn = null;
        String sql = SQL_GET;
        if (StringUtils.isNoneBlank(name)) {
            sql += " AND d.branch_name LIKE ? ";
        }

        sql += " ORDER BY d.id DESC LIMIT ?,?";

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
    private Branch convertToObject(Object[] objects) {
        Branch obj = new Branch();
        int i = 0;
        obj.setId(Long.parseLong(String.valueOf(objects[i++])));
        obj.setName(String.valueOf(objects[i++]));
        obj.setAddress(String.valueOf(objects[i++]));
        obj.setHotline(String.valueOf(objects[i++]));
        return obj;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="countUserByBranch">
    @Override
    public BigInteger countUserByBranch(Long id) {
        String sql = SQL_COUNT_USER_BY_BRANCH;
        if (id != 0) {
            sql += " AND d.branch_id = ? ";
        }
        Query query = em.createNativeQuery(sql);

        int i = 1;
        if (id != 0) {
            query.setParameter(i++, id);
        }

        BigInteger count = (BigInteger) query.getSingleResult();
        return count;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="countByQuery">
    @Override
    public BigInteger countByQuery(String name) {
        String sql = SQL_COUNT;
        if (StringUtils.isNoneBlank(name)) {
            sql += " AND d.branch_name LIKE ? ";
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
    public Branch save(Branch entity) {
        entity = branchRepo.save(entity);
        return entity;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="delete">
    @Override
    public Boolean delete(Long id) {
        branchRepo.delete(id);
        return true;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="saveBranchOfUser">
    @Override
    public int saveBranchOfUser(String name, long id) {
        try {
            String sql = SQL_UPDATE_USER_WHEN_UPDATE_BRANCH;
            Query query = em.createNativeQuery(sql);
            int i = 1;
            query.setParameter(i++, name);
            query.setParameter(i++, id);
            return query.executeUpdate();
        } catch (Exception ex) {
            return 0;
        }
    }//</editor-fold>
}
