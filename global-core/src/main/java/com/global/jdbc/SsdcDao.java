package com.global.jdbc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by ThuyetLV
 */
@NoRepositoryBean
public interface SsdcDao<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    List<T> findAll();

    List<T> findAll(Sort sort);

    List<T> findAll(Iterable<ID> ids);

    <S extends T> S save(S entity);

    <S extends T> List<S> save(Iterable<S> entities);

    <S extends T> S insert(S entity);

    <S extends T> S update(S entity);

    List<T> searchWithGroupBy(String groupBy);

    List<T> searchWithGroupBy(String groupBy, String whereExp, Object... queryParams);

    List<T> search(String whereExp, Object... queryParams);

    List<T> search(String whereExp, Sort sort, Object... queryParams);

    Page<T> search(String whereExp, Pageable page, Object... queryParams);

    long count();

    long count(String whereExp);

    long count(String whereExp, Object... queryParams);

    public JdbcTemplate getJdbcTemplate();
}
