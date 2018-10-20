package vn.vnpt.ssdc.jdbc.factories;

import vn.vnpt.ssdc.core.ObjectCache;
import vn.vnpt.ssdc.jdbc.SsdcEntity;
import vn.vnpt.ssdc.jdbc.SsdcDao;
import vn.vnpt.ssdc.jdbc.SsdcSqlDao;

import javax.sql.DataSource;

/**
 * Created by vietnq on 10/25/16.
 */
public class RepositoryFactory {

    private DataSource dataSource;

    private ObjectCache objectCache;

    public RepositoryFactory() {

    }

    public RepositoryFactory(DataSource dataSource, ObjectCache objectCache) {
        this.dataSource = dataSource;
        this.objectCache = objectCache;
    }

    public RepositoryFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SsdcDao create(Class<? extends SsdcEntity> entityClass) {
        SsdcSqlDao ssdcSqlDao = new SsdcSqlDao(dataSource, entityClass);
        //TODO use cache
//        return new SsdcSqlRepository(objectCache, ssdcSqlDao);
        return ssdcSqlDao;
    }
}
