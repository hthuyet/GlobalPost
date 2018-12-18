package com.global.core;

import com.global.jdbc.SsdcEntity;
import com.global.jdbc.SsdcDao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ThuyetLV
 */
public class SsdcCrudService<ID extends Serializable, T extends SsdcEntity<ID>> {

    protected SsdcDao<T, ID> repository;

    public T create(T entity) {
        beforeCreate(entity);
        T created = repository.insert(entity);
        afterCreate(created);
        return created;
    }

    public T get(ID id) {
        return repository.findOne(id);
    }

    public T update(ID id, T entity) {
        beforeUpdate(id, entity);
        T oldEntity = get(id);
        T updated = repository.update(entity);
        afterUpdate(oldEntity, updated);
        return updated;
    }

    public void delete(ID id) {
        beforeDelete(id);
        T deleted = get(id);
        repository.delete(id);
        afterDelete(deleted);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public void beforeCreate(T entity) {
    }

    public void afterCreate(T entity) {
    }

    public void beforeUpdate(ID id, T entity) {
    }

    public void afterUpdate(T oldEntity, T newEntity) {
    }

    public void beforeDelete(ID id) {
    }

    public void afterDelete(T entity) {
    }
}
