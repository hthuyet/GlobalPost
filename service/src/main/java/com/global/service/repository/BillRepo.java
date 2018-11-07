package com.global.service.repository;

import com.global.service.model.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepo extends CrudRepository<Bill, Long> {
}
