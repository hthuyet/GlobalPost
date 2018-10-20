package vn.vnpt.ssdc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.vnpt.ssdc.jdbc.SsdcEntity;
import vn.vnpt.ssdc.jdbc.SsdcDao;
import vn.vnpt.ssdc.jdbc.SsdcSqlDao;
import vn.vnpt.ssdc.utils.ObjectUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Wraps SsdcDao with cache support using Redis
 *
 * Created by vietnq on 10/26/16.
 */
public class SsdcSqlRepository<T extends SsdcEntity<ID>, ID extends Serializable> implements SsdcDao<T,ID> {

    private static Logger LOGGER = LoggerFactory.getLogger(SsdcSqlRepository.class);

    protected ObjectCache objectCache;

    protected SsdcSqlDao<T,ID> ssdcSqlDao;

    protected Class<?> type;

    public SsdcSqlRepository(ObjectCache objectCache, SsdcSqlDao<T, ID> ssdcSqlDao) {
        this.ssdcSqlDao = ssdcSqlDao;
        this.objectCache = objectCache;
        this.type = ssdcSqlDao.getEntityClass();
    }

    public List<T> findAll() {
        return ssdcSqlDao.findAll();
    }

    public List<T> findAll(Sort sort) {
        return ssdcSqlDao.findAll(sort);
    }

    public Page<T> findAll(Pageable pageable) {
        return ssdcSqlDao.findAll(pageable);
    }

    public List<T> findAll(Iterable<ID> ids) {
        return ssdcSqlDao.findAll(ids);
    }

    public long count() {
        return ssdcSqlDao.count();
    }

    public long count(String whereExp) {
        return ssdcSqlDao.count(whereExp);
    }

    public long count(String whereExp, Object... queryParams) {
        return ssdcSqlDao.count(whereExp, queryParams);
    }

    public void delete(ID id) {
        ssdcSqlDao.delete(id);
        objectCache.remove(id.toString(),type);
    }

    public void delete(T t) {
        ssdcSqlDao.delete(t);
        objectCache.remove(t.id.toString(),type);
    }

    public void delete(Iterable<? extends T> ts) {
        ssdcSqlDao.delete(ts);
    }

    public void deleteAll() {
        ssdcSqlDao.deleteAll();
    }

    public <S extends T> S save(S entity) {
        if(exists(entity.id)) {
            return update(entity);
        }
        return insert(entity);
    }

    public <S extends T> List<S> save(Iterable<S> entities) {
        return ssdcSqlDao.save(entities);
    }

    public T findOne(ID id) {
        try {
            Object object = objectCache.get(id.toString(),type);
            if(object != null) {
                return (T)object;
            }
        } catch(Exception e) {
            LOGGER.error("error when getting object from cache, cachePrefix:{}, key: {}",type.getSimpleName(),id,e);
        }
        return ssdcSqlDao.findOne(id);
    }

    public boolean exists(ID id) {
        if(!ObjectUtils.empty(objectCache.get(id.toString(),type))) {
            return true;
        }
        return ssdcSqlDao.exists(id);
    }

    public <S extends T> S insert(S entity) {
        S createdEntity = ssdcSqlDao.insert(entity);
        objectCache.put(entity.id.toString(),createdEntity, type);
        return createdEntity;
    }

    public <S extends T> S update(S entity) {
        S updatedEntity = ssdcSqlDao.update(entity);
        objectCache.put(entity.id.toString(),updatedEntity, type);
        return updatedEntity;
    }

    public List<T> searchWithGroupBy(String groupBy) {
        return ssdcSqlDao.searchWithGroupBy(groupBy);
    }

    public List<T> searchWithGroupBy(String groupBy, String whereExp, Object... queryParams) {
        return ssdcSqlDao.searchWithGroupBy(groupBy, whereExp, queryParams);
    }

    public List<T> search(String whereExp, Object... queryParams) {
        return ssdcSqlDao.search(whereExp,queryParams);
    }

    public List<T> search(String whereExp, Sort sort, Object... queryParams) {
        return ssdcSqlDao.search(whereExp,sort,queryParams);
    }

    public Page<T> search(String whereExp, Pageable page, Object... queryParams) {
        return ssdcSqlDao.search(whereExp,page,queryParams);
    }
}
