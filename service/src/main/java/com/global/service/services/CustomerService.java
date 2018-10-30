package com.global.service.services;

import com.global.service.model.Customer;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public abstract interface CustomerService {
  public abstract List findByQuery(String name, int offset, int limit);

  public abstract BigInteger countByQuery(String name);
  public abstract Customer save(Customer entity);
  public abstract Boolean delete(Long id);
}
