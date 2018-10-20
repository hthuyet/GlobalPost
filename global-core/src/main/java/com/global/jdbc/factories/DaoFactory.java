package com.global.jdbc.factories;

import com.global.jdbc.SsdcEntity;
import com.global.jdbc.SsdcSqlDao;

import javax.sql.DataSource;

/**
 * Created by ThuyetLV
 */
public class DaoFactory {

    private DataSource dataSource;

    public DaoFactory() {

    }

    public DaoFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SsdcSqlDao create(Class<? extends SsdcEntity> entityClass) {
        return new SsdcSqlDao(dataSource, entityClass);
    }
}
