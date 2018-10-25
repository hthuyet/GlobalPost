package com.global.servicebase.services;

import com.global.core.SsdcCrudService;
import com.global.jdbc.factories.RepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.global.servicebase.model.EmailTemplate;

import java.util.List;

@Service
public class EmailTemplateService extends SsdcCrudService<String, EmailTemplate> {

    @Autowired
    public EmailTemplateService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(EmailTemplate.class);
    }

    public List<EmailTemplate> searchEmail(String limit, String indexPage) {
        Page<EmailTemplate> all = this.repository.findAll(new PageRequest(Integer.parseInt(indexPage), Integer.parseInt(limit)));
        return all.getContent();
    }
}
