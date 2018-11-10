package com.global.service.repository;

import com.global.service.model.BillStock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillStockRepo extends CrudRepository<BillStock, Long> {
}
