package com.global.servicecommon.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.global.servicecommon.user.model.Operation;
import com.global.servicecommon.core.SsdcCrudService;
import com.global.servicecommon.jdbc.factories.RepositoryFactory;

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
