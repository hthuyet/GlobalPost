package com.global.servicecommon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.global.servicecommon.model.Configuration;
import com.global.servicecommon.core.SsdcCrudService;
import com.global.servicecommon.jdbc.factories.RepositoryFactory;


@Service
public class ConfigurationService extends SsdcCrudService<String, Configuration> {
    @Autowired
    public ConfigurationService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(Configuration.class);
    }
}
