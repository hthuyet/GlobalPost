package com.global.servicecommon.jdbc.factories;

import com.global.servicecommon.jdbc.SsdcEntity;
import com.global.servicecommon.jdbc.SsdcSqlDao;

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
