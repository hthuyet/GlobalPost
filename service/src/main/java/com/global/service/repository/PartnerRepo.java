package com.global.service.repository;

import com.global.service.model.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepo extends CrudRepository<Partner, Long> {
}
