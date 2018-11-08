package com.global.service.repository;

import com.global.service.model.BillReceive;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillReceiveRepo extends CrudRepository<BillReceive, Long> {
}
