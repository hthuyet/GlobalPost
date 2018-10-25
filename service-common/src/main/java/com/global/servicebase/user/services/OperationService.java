package com.global.servicebase.user.services;

import com.global.core.SsdcCrudService;
import com.global.jdbc.factories.RepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.global.servicebase.user.model.Operation;

/**
 * Created by ThuyetLV
 */
@Service
public class OperationService extends SsdcCrudService<String, Operation> {

    @Autowired
    public OperationService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(Operation.class);
    }

    public boolean getById(String id) {
        int count = this.repository.search("id=?", id).size();
        if (count == 0) {
            return false;
        }
        return true;
    }

}
