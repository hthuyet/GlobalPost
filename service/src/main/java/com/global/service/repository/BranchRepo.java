package com.global.service.repository;

import com.global.service.model.Branch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepo extends CrudRepository<Branch, Long> {
}
