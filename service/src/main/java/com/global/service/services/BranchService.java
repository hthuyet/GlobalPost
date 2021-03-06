package com.global.service.services;

import com.global.service.model.Branch;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public abstract interface BranchService {

    public abstract List findByQuery(String search, int offset, int limit);

    public abstract BigInteger countByQuery(String search);
    
    public abstract BigInteger countUserByBranch(Long id);

    public abstract Branch save(Branch entity);

    public abstract Boolean delete(Long id);
    
    public abstract int saveBranchOfUser(String name, long id);
}
