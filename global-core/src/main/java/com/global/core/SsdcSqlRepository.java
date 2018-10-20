package com.global.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.global.jdbc.SsdcEntity;
import com.global.jdbc.SsdcDao;
import com.global.jdbc.SsdcSqlDao;
import com.global.utils.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Wraps SsdcDao with cache support using Redis
 *
 * Created by ThuyetLV
 */
public class SsdcSqlRepository<T extends SsdcEntity<ID>, ID extends Serializable> implements SsdcDao<T, ID> {

    private static Logger LOGGER = LoggerFactory.getLogger(SsdcSqlRepository.class);

    protected ObjectCache objectCache;

    protected SsdcSqlDao<T, ID> ssdcSqlDao;

    protected Class<?> type;

    public SsdcSqlRepository(ObjectCache objectCache, SsdcSqlDao<T, ID> ssdcSqlDao) {
        this.ssdcSqlDao = ssdcSqlDao;
        this.objectCache = objectCache;
        this.type = ssdcSqlDao.getEntityClass();
    }

    @Override
    public List<T> findAll() {
        return ssdcSqlDao.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return ssdcSqlDao.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return ssdcSqlDao.findAll(pageable);
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return ssdcSqlDao.findAll(ids);
    }

    @Override
    public long count() {
        return ssdcSqlDao.count();
    }

    @Override
    public long count(String whereExp) {
        return ssdcSqlDao.count(whereExp);
    }

    @Override
    public long count(String whereExp, Object... queryParams) {
        return ssdcSqlDao.count(whereExp, queryParams);
    }

    @Override
    public void delete(ID id) {
        ssdcSqlDao.delete(id);
        objectCache.remove(id.toString(), type);
    }

    @Override
    public void delete(T t) {
        ssdcSqlDao.delete(t);
        objectCache.remove(t.id.toString(), type);
    }

    @Override
    public void delete(Iterable<? extends T> ts) {
        ssdcSqlDao.delete(ts);
    }

    @Override
    public void deleteAll() {
        ssdcSqlDao.deleteAll();
    }

    @Override
    public <S extends T> S save(S entity) {
        if (exists(entity.id)) {
            return update(entity);
        }
        return insert(entity);
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return ssdcSqlDao.save(entities);
    }

    @Override
    public T findOne(ID id) {
        try {
            Object object = objectCache.get(id.toString(), type);
            if (object != null) {
                return (T) object;
            }
        } catch (Exception e) {
            LOGGER.error("error when getting object from cache, cachePrefix:{}, key: {}", type.getSimpleName(), id, e);
        }
        return ssdcSqlDao.findOne(id);
    }

    @Override
    public boolean exists(ID id) {
        if (!ObjectUtils.empty(objectCache.get(id.toString(), type))) {
            return true;
        }
        return ssdcSqlDao.exists(id);
    }

    @Override
    public <S extends T> S insert(S entity) {
        S createdEntity = ssdcSqlDao.insert(entity);
        objectCache.put(entity.id.toString(), createdEntity, type);
        return createdEntity;
    }

    @Override
    public <S extends T> S update(S entity) {
        S updatedEntity = ssdcSqlDao.update(entity);
        objectCache.put(entity.id.toString(), updatedEntity, type);
        return updatedEntity;
    }

    @Override
    public List<T> searchWithGroupBy(String groupBy) {
        return ssdcSqlDao.searchWithGroupBy(groupBy);
    }

    @Override
    public List<T> searchWithGroupBy(String groupBy, String whereExp, Object... queryParams) {
        return ssdcSqlDao.searchWithGroupBy(groupBy, whereExp, queryParams);
    }

    @Override
    public List<T> search(String whereExp, Object... queryParams) {
        return ssdcSqlDao.search(whereExp, queryParams);
    }

    @Override
    public List<T> search(String whereExp, Sort sort, Object... queryParams) {
        return ssdcSqlDao.search(whereExp, sort, queryParams);
    }

    @Override
    public Page<T> search(String whereExp, Pageable page, Object... queryParams) {
        return ssdcSqlDao.search(whereExp, page, queryParams);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return ssdcSqlDao.getJdbcTemplate();
    }
}
