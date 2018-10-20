package com.global.servicecommon.jdbc.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;
import com.global.servicecommon.jdbc.TableInfo;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.collectionToDelimitedString;
import static com.global.servicecommon.utils.StringUtils.repeat;

/**
 * Created by ThuyetLV
 */
public class DefaultSqlGenerator implements SqlGenerator {

    static final String AND = " AND ",
            OR = " OR ",
            COMMA = ", ",
            PARAM = " = ?";

    public boolean isCompatible(DatabaseMetaData metadata) throws SQLException {
        return true;
    }

    public String count(TableInfo table) {
        return format("SELECT count(*) FROM %s", table.fromClause);
    }

    public String count(TableInfo table, String whereExp) {
        return format("SELECT count(*) FROM %s WHERE %s", table.fromClause, whereExp);
    }

    public String deleteAll(TableInfo table) {
        return format("DELETE FROM %s", table.name);
    }

    public String deleteById(TableInfo table) {
        return deleteByIds(table, 1);
    }

    public String deleteByIds(TableInfo table, int idsCount) {
        return deleteAll(table) + " WHERE " + idsPredicate(table, idsCount);
    }

    public String existsById(TableInfo table) {
        return format("SELECT 1 FROM %s WHERE %s", table.name, idPredicate(table));
    }

    public String insert(TableInfo table) {

        return format("INSERT INTO %s (%s) VALUES (%s)",
                table.name,
                collectionToDelimitedString(table.columns, COMMA),
                repeat("?", COMMA, table.columns.size()));
    }

    public String selectAll(TableInfo table) {
        return format("SELECT %s FROM %s", table.selectClause, table.fromClause);
    }

    public String selectAll(TableInfo table, Pageable page) {
        Sort sort = page.getSort() != null ? page.getSort() : sortById(table);

        return format("SELECT t2__.* FROM ( "
                + "SELECT row_number() OVER (ORDER BY %s) AS rn__, t1__.* FROM ( %s ) t1__ "
                + ") t2__ WHERE t2__.rn__ BETWEEN %s AND %s",
                orderByExpression(sort), selectAll(table),
                page.getOffset() + 1, page.getOffset() + page.getPageSize());
    }

    public String selectAll(TableInfo table, Sort sort) {
        return selectAll(table) + (sort != null ? orderByClause(sort) : "");
    }

    public String selectById(TableInfo table) {
        return selectByIds(table, 1);
    }

    public String selectByIds(TableInfo table, int idsCount) {
        return idsCount > 0
                ? selectAll(table) + " WHERE " + idsPredicate(table, idsCount)
                : selectAll(table);
    }

    public String update(TableInfo table) {

        return format("UPDATE %s SET %s WHERE %s",
                table.name,
                formatParameters(table.columns, COMMA),
                idPredicate(table));
    }

    public String search(TableInfo table, String whereExp, Sort sort) {
        return search(table, whereExp) + (sort != null ? orderByClause(sort) : "");
    }

    public String search(TableInfo table, String whereExp, Pageable page) {
        Sort sort = page.getSort() != null ? page.getSort() : sortById(table);

        return format("SELECT t2__.* FROM ( "
                + "SELECT row_number() OVER (ORDER BY %s) AS rn__, t1__.* FROM ( %s ) t1__ "
                + ") t2__ WHERE t2__.rn__ BETWEEN %s AND %s",
                orderByExpression(sort), search(table, whereExp),
                page.getOffset() + 1, page.getOffset() + page.getPageSize());
    }

    public String search(TableInfo table, String whereExp) {
        return format("SELECT %s FROM %s WHERE %s",
                table.selectClause,
                table.fromClause,
                whereExp);
    }

    public String searchWithGroupBy(TableInfo table, String groupBy) {
        return format("SELECT %s FROM %s GROUP BY %s",
                "*, count(*) as number_of_rows",
                table.fromClause,
                groupBy);
    }

    public String searchWithGroupBy(TableInfo table, String whereExp, String groupBy) {
        return format("SELECT %s FROM %s WHERE %s GROUP BY %s",
                "*, count(*) as number_of_rows",
                table.fromClause,
                whereExp,
                groupBy);
    }

    protected String orderByClause(Sort sort) {
        return " ORDER BY " + orderByExpression(sort);
    }

    protected String orderByExpression(Sort sort) {
        StringBuilder sb = new StringBuilder();

        for (Iterator<Order> it = sort.iterator(); it.hasNext();) {
            Order order = it.next();
            sb.append(order.getProperty()).append(' ').append(order.getDirection());

            if (it.hasNext()) {
                sb.append(COMMA);
            }
        }
        return sb.toString();
    }

    protected Sort sortById(TableInfo table) {
        return new Sort(Direction.ASC, table.pkColumns);
    }

    private String idPredicate(TableInfo table) {
        return formatParameters(table.pkColumns, AND);
    }

    private String idsPredicate(TableInfo table, int idsCount) {
        Assert.isTrue(idsCount > 0, "idsCount must be greater than zero");

        List<String> idColumnNames = table.pkColumns;

        if (idsCount == 1) {
            return idPredicate(table);

        } else if (idColumnNames.size() > 1) {
            return repeat("(" + formatParameters(idColumnNames, AND) + ")", OR, idsCount);

        } else {
            return idColumnNames.get(0) + " IN (" + repeat("?", COMMA, idsCount) + ")";
        }
    }

    private String formatParameters(Collection<String> columns, String delimiter) {
        return collectionToDelimitedString(columns, delimiter, "", PARAM);
    }
}
