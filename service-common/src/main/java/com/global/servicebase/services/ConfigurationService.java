package com.global.servicebase.services;

import com.global.core.SsdcCrudService;
import com.global.jdbc.factories.RepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.global.servicebase.model.Configuration;

@Service
public class ConfigurationService extends SsdcCrudService<String, Configuration> {

    @Autowired
    public ConfigurationService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(Configuration.class);
    }
}
