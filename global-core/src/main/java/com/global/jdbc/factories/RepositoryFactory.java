package com.global.jdbc.factories;

import com.global.core.ObjectCache;
import com.global.jdbc.SsdcEntity;
import com.global.jdbc.SsdcDao;
import com.global.jdbc.SsdcSqlDao;

import javax.sql.DataSource;

/**
 * Created by ThuyetLV
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
