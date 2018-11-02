package com.global.service.services;

import com.global.service.model.Branch;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public abstract interface BranchService {

    public abstract List findByQuery(String name, int offset, int limit);

    public abstract BigInteger countByQuery(String name);

    public abstract Branch save(Branch entity);

    public abstract Boolean delete(Long id);
}
