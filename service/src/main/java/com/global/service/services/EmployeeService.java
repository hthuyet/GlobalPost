/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services;

import com.global.service.model.Employee;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author HungNT
 */
@Service
public interface EmployeeService {

    public abstract List findByQuery(String search, Long branchId, int offset, int limit);

    public abstract BigInteger countByQuery(String search, Long branchId);

    public abstract Boolean delete(Long id);

    public abstract Employee save(Employee entity);
}
