package vn.vnpt.ssdc.jdbc.factories;

import vn.vnpt.ssdc.jdbc.SsdcEntity;
import vn.vnpt.ssdc.jdbc.SsdcSqlDao;

import javax.sql.DataSource;

/**
 * Created by vietnq on 10/25/16.
 */
public class DaoFactory {

    private DataSource dataSource;

    public DaoFactory() {

    }

    public DaoFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SsdcSqlDao create(Class<? extends SsdcEntity> entityClass) {
        return new SsdcSqlDao(dataSource,entityClass);
    }
}
