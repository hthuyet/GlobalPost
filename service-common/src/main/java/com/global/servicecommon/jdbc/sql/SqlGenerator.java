package com.global.servicecommon.jdbc.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.global.servicecommon.jdbc.TableInfo;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Created by ThuyetLV
 */
public interface SqlGenerator {
    boolean isCompatible(DatabaseMetaData metadata) throws SQLException;

    String count(TableInfo table);

    String count (TableInfo table, String whereExp);

    String deleteAll(TableInfo table);

    String deleteById(TableInfo table);

    String deleteByIds(TableInfo table, int idsCount);

    String existsById(TableInfo table);

    String insert(TableInfo table);

    String selectAll(TableInfo table);

    String selectAll(TableInfo table, Pageable page);

    String selectAll(TableInfo table, Sort sort);

    String selectById(TableInfo table);

    String selectByIds(TableInfo table, int idsCount);

    String update(TableInfo table);

    public String search(TableInfo table, String whereExp, Sort sort);

    public String search(TableInfo table, String whereExp, Pageable page);

    public String search(TableInfo table, String whereExp);

    public String searchWithGroupBy(TableInfo table, String whereExp, String groupBy);

    public String searchWithGroupBy(TableInfo table, String groupBy);
}
