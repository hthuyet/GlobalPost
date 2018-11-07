package com.global.service.repository;

import com.global.service.model.BillLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillLogRepo extends CrudRepository<BillLog, Long> {
}
