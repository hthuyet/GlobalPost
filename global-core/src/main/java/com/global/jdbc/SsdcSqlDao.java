package com.global.jdbc;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import com.global.jdbc.annotations.ExtendField;
import com.global.jdbc.annotations.Serialized;
import com.global.jdbc.exceptions.EntityNotFoundException;
import com.global.jdbc.sql.SqlGenerator;
import com.global.jdbc.sql.SqlGeneratorFactory;
import com.global.utils.Serializer;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static com.global.utils.IterableUtils.toList;
import static com.global.utils.ObjectUtils.wrapToArray;
import static com.global.utils.StringUtils.toSnakeCase;

/**
* Created by ThuyetLV
 */
public class SsdcSqlDao<T extends SsdcEntity<ID>, ID extends Serializable>
        implements SsdcDao<T, ID>, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsdcSqlDao.class);
    private Class<? extends SsdcEntity> entityClass;
    private Map<Field, String> entityFields = new LinkedHashMap<Field, String>();
    private RowMapper<T> rowMapper;
    private Gson jsonMapper;
    private JdbcTemplate jdbcTemplate;

    private SqlGeneratorFactory sqlGeneratorFactory = SqlGeneratorFactory.getInstance();
    private SqlGenerator sqlGenerator;
    public TableInfo table;

    private boolean initialized;

    public SsdcSqlDao(DataSource dataSource,
            Class<? extends SsdcEntity> entityClass) {

        this.jsonMapper = new Gson();
        this.entityClass = entityClass;
        for (Field field : entityClass.getFields()) {
            entityFields.put(field, toSnakeCase(field.getName()));
        }
        this.rowMapper = rowMapper();
        this.table = new TableInfo(entityClass);
        this.sqlGenerator = sqlGeneratorFactory.getGenerator(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Class<? extends SsdcEntity> getEntityClass() {
        return entityClass;
    }

    public long count() {
        return jdbcTemplate.queryForObject(sqlGenerator.count(table), Long.class);
    }

    public long count(String whereExp) {
        String countQuery = sqlGenerator.count(table, whereExp);
        return jdbcTemplate.queryForObject(countQuery, Long.class);
    }

    public long count(String whereExp, Object... queryParams) {
        String countQuery = sqlGenerator.count(table, whereExp);
        return jdbcTemplate.queryForObject(countQuery, Long.class, queryParams);
    }

    public void delete(ID id) {
        jdbcTemplate.update(sqlGenerator.deleteById(table), wrapToArray(id));
    }

    public void delete(T entity) {
        delete(entity.id);
    }

    public void delete(Iterable<? extends T> entities) {
        List<ID> ids = ids(entities);

        if (!ids.isEmpty()) {
            jdbcTemplate.update(sqlGenerator.deleteByIds(table, ids.size()), flatten(ids));
        }
    }

    public void deleteAll() {
        jdbcTemplate.update(sqlGenerator.deleteAll(table));
    }

    public boolean exists(ID id) {
        return !jdbcTemplate.queryForList(
                sqlGenerator.existsById(table), wrapToArray(id), Integer.class).isEmpty();
    }

    public List<T> findAll() {
        Sort sort = new Sort(Sort.Direction.DESC, "created");
        String tmp = sqlGenerator.selectAll(table, sort);
        List<T> result = jdbcTemplate.query(tmp, rowMapper);
        return result;
    }

    public T findOne(ID id) {
        List<T> entities = jdbcTemplate.query(
                sqlGenerator.selectById(table), wrapToArray(id), rowMapper);

        if (entities.isEmpty()) {
            throw new EntityNotFoundException(table.name, id);
        }
        return entities.get(0);
    }

    public <S extends T> S save(S entity) {
        //TODO improve
        if (exists(entity.id)) {
            return update(entity);
        }
        return insert(entity);
    }

    public <S extends T> List<S> save(Iterable<S> entities) {
        List<S> ret = new ArrayList<S>();
        for (S s : entities) {
            ret.add(save(s));
        }
        return ret;
    }

    public List<T> findAll(Iterable<ID> ids) {
        List<ID> idsList = toList(ids);

        if (idsList.isEmpty()) {
            return Collections.emptyList();
        }
        return jdbcTemplate.query(
                sqlGenerator.selectByIds(table, idsList.size()), rowMapper, flatten(idsList));
    }

    public List<T> findAll(Sort sort) {
        return jdbcTemplate.query(sqlGenerator.selectAll(table, sort), rowMapper);
    }

    public Page<T> findAll(Pageable page) {
        String query = sqlGenerator.selectAll(table, page);

        return new PageImpl<T>(jdbcTemplate.query(query, rowMapper), page, count());
    }

    public <S extends T> S insert(S entity) {
        entity.created = System.currentTimeMillis();
        return entity.id == null
                ? insertWithAutoGeneratedKey(entity)
                : insertWithManuallyAssignedKey(entity);
    }

    public <S extends T> S update(S entity) {
        entity.updated = System.currentTimeMillis();
        String updateQuery = sqlGenerator.update(table);
        Object[] queryParams = columnValues(entity, true).toArray();

        int rowsAffected = jdbcTemplate.update(updateQuery, queryParams);

        if (rowsAffected < 1) {
            throw new EntityNotFoundException(table.name, entity.id);
        }
        if (rowsAffected > 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(updateQuery, 1, rowsAffected);
        }

        return afterUpdate(entity);
    }

    public List<T> searchWithGroupBy(String groupBy) {
        return jdbcTemplate.query(sqlGenerator.searchWithGroupBy(table, groupBy), rowMapper);
    }

    public List<T> searchWithGroupBy(String groupBy, String whereExp, Object... queryParams) {
        return jdbcTemplate.query(sqlGenerator.searchWithGroupBy(table, whereExp, groupBy), queryParams, rowMapper);
    }

    public List<T> search(String whereExp, Object... queryParams) {
        return jdbcTemplate.query(sqlGenerator.search(table, whereExp), queryParams, rowMapper);
    }

    public List<T> search(String whereExp, Sort sort, Object... queryParams) {
        return jdbcTemplate.query(sqlGenerator.search(table, whereExp, sort), queryParams, rowMapper);
    }

    public Page<T> search(String whereExp, Pageable page, Object... queryParams) {
        String query = sqlGenerator.search(table, whereExp, page);
        return new PageImpl<T>(jdbcTemplate.query(query, queryParams, rowMapper), page, count(whereExp, queryParams));
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected SqlGenerator getSqlGenerator() {
        return sqlGenerator;
    }

    protected JdbcTemplate jdbc() {
        return jdbcTemplate;
    }

    protected Map<String, Object> beforeInsert(Map<String, Object> columns, T entity) {
        return columns;
    }

    protected <S extends T> S afterInsert(S entity, Number generatedId) {
        return entity;
    }

    protected Map<String, Object> beforeUpdate(T entity, Map<String, Object> columns) {
        return columns;
    }

    protected <S extends T> S afterUpdate(S entity) {
        return entity;
    }

    private List<ID> ids(Iterable<? extends T> entities) {
        List<ID> ids = new ArrayList<ID>();

        for (T entity : entities) {
            ids.add(entity.id);
        }
        return ids;
    }

    private <S extends T> S insertWithManuallyAssignedKey(S entity) {
        String insertQuery = sqlGenerator.insert(table);
        Object[] queryParams = columnValues(entity, false).toArray();

        jdbcTemplate.update(insertQuery, queryParams);

        return afterInsert(entity, null);
    }

    private <S extends T> S insertWithAutoGeneratedKey(S entity) {
        final String insertQuery = sqlGenerator.insert(table);
        final Object[] queryParams = columnValues(entity, false).toArray();
        final GeneratedKeyHolder key = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertQuery, new String[]{"ID"});
                for (int i = 0; i < queryParams.length; ++i) {
                    ps.setObject(i + 1, queryParams[i]);
                }
                return ps;
            }
        }, key);

        entity.id = (ID) key.getKey();

        return afterInsert(entity, key.getKey());
    }

    private static <ID> Object[] flatten(List<ID> ids) {
        List<Object> result = new ArrayList<Object>();
        for (ID id : ids) {
            result.addAll(asList(wrapToArray(id)));
        }
        return result.toArray();
    }

    private void throwOnChangeAfterInitialization(String propertyName) {
        if (initialized) {
            throw new IllegalStateException(
                    propertyName + " should not be changed after initialization");
        }
    }

    private RowMapper<T> rowMapper() {
        return new RowMapper<T>() {
            public T mapRow(ResultSet rs, int i) throws SQLException {
                T entity = null;

                try {
                    entity = (T) entityClass.newInstance();

                    for (Map.Entry<Field, String> entry : entityFields.entrySet()) {
                        Object value = null;
                        Class<?> type = entry.getKey().getType();

                        boolean checkExistColumn = true;
                        try {
                            rs.getObject(entry.getValue());
                        } catch (java.sql.SQLException e) {
                            checkExistColumn = false;
                        }

                        if (checkExistColumn || !entry.getKey().isAnnotationPresent(ExtendField.class)) {

                            if (entry.getKey().isAnnotationPresent(Serialized.class)) {

                                String jsonString = rs.getString(entry.getValue());

                                if (!rs.wasNull()) {
                                    Serialized serialized = entry.getKey().getAnnotation(Serialized.class);
                                    Serializer serializer = (Serializer) serialized.serializer().newInstance();

                                    value = serializer.deSerialize(entry.getKey().getType(), jsonString);
                                }

                            } else {

                                if (type.equals(Long.class)) {
                                    long val = rs.getLong(entry.getValue());
                                    if (!rs.wasNull()) {
                                        value = val;
                                    }
                                } else if (type.equals(Integer.class)) {
                                    int val = rs.getInt(entry.getValue());
                                    if (!rs.wasNull()) {
                                        value = val;
                                    }
                                } else if (type.equals(Boolean.class)) {
                                    Boolean val = rs.getBoolean(entry.getValue());
                                    if (!rs.wasNull()) {
                                        value = val;
                                    }
                                } else if (type.isEnum()) {
                                    Object val = rs.getObject(entry.getValue());
                                    if (!rs.wasNull()) {
                                        Method m = type.getMethod("valueOf", String.class);
                                        value = m.invoke(entry.getKey().getType(), val);
                                    }

                                } else if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {

                                    ParameterizedType parameterizedType = (ParameterizedType) entry.getKey().getGenericType();

                                    String jsonString = rs.getString(entry.getValue());
                                    if (!("".equals(jsonString) || rs.wasNull())) {
                                        value = jsonMapper.fromJson(jsonString, parameterizedType);
                                    }

                                } else {
                                    Object val = rs.getObject(entry.getValue());
                                    if (!rs.wasNull()) {
                                        value = val;
                                    }
                                }
                            }
                            // Set the value on the entity object
                            entry.getKey().set(entity, value);
                        }

                    } // for-loop
                } catch (InstantiationException ex) {
                    LOGGER.error("Failed instantiate entity class. " + ex.getMessage(), ex);
                } catch (IllegalAccessException ex) {
                    LOGGER.error("Failed instantiate entity class. " + ex.getMessage(), ex);
                } catch (NoSuchMethodException ex) {
                    LOGGER.error("Failed instantiate entity class. " + ex.getMessage(), ex);
                } catch (InvocationTargetException ex) {
                    LOGGER.error("Failed instantiate entity class. " + ex.getMessage(), ex);
                } catch (IOException ex) {
                    LOGGER.error("Failed to deserialize entity class. " + ex.getMessage(), ex);
                }

                return entity;
            }
        };
    }

    public Collection<Object> columnValues(T entity, Boolean isUpdate) {
        List<Object> result = new ArrayList<Object>();
        for (Map.Entry<Field, String> entry : entityFields.entrySet()) {
            Field field = entry.getKey();
            String fieldName = entry.getValue();
            try {
                if (!field.isAnnotationPresent(ExtendField.class)) {
                    if (field.isAnnotationPresent(Serialized.class)) {

                        Serialized serialized = field.getAnnotation(Serialized.class);
                        Serializer serializer = (Serializer) serialized.serializer().newInstance();

                        result.add(serializer.serialize(field.get(entity)));
                    } else if (Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
                        result.add(jsonMapper.toJson(field.get(entity)));
                    } else if (field.getType().isEnum()) {
                        result.add(field.get(entity) == null ? null : field.get(entity).toString());
                    } else {
                        result.add(field.get(entity));
                    }
                }

            } catch (Exception ex) {
                LOGGER.error("Failed to persist entity field " + field.getName() + " for class " + entityClass.getName(), ex);
            }
        }
        if (isUpdate) {
            result.add(entity.id);
        }
        return result;
    }

    public void afterPropertiesSet() throws Exception {
        this.initialized = true;
    }
}
