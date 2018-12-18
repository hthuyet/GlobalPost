package com.global.jdbc;

import com.global.jdbc.annotations.ExtendField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static com.global.utils.StringUtils.toSnakeCase;

/**
 * Created by ThuyetLV
 */
public class TableInfo {

    public String name;
    public String selectClause = "*";
    public String fromClause;
    public List<String> pkColumns = singletonList("id");
    public Collection<String> columns;

    public TableInfo() {
    }

    public TableInfo(String name, String selectClause, String fromClause) {
        this.name = name;
        this.selectClause = selectClause;
        this.fromClause = fromClause;
    }

    public TableInfo(String name, String fromClause) {
        this(name, "*", fromClause);
    }

    public TableInfo(String name) {
        this(name, "*", name);
    }

    public TableInfo(Class<?> entityClass) {
        String tableName = entityClass.getSimpleName();
        tableName = toSnakeCase(tableName);
//        if (!tableName.endsWith("s")) {
//            tableName = tableName + "s";
//        }
        this.name = tableName;
        this.selectClause = "*";
        this.fromClause = tableName;
        this.columns = new ArrayList<String>();
        for (Field field : entityClass.getFields()) {
            if (!field.isAnnotationPresent(ExtendField.class)) {
                this.columns.add(toSnakeCase(field.getName()));
            }
        }
    }
}
