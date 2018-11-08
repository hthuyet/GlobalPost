package com.global.service.repository;

import com.global.service.model.BillSend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillSendRepo extends CrudRepository<BillSend, Long> {
}
