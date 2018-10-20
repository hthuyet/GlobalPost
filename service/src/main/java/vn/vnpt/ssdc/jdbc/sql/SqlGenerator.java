package vn.vnpt.ssdc.jdbc.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.vnpt.ssdc.jdbc.TableInfo;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by vietnq on 10/25/16.
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
