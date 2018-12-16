package com.global.service.services;

import com.global.service.model.Partner;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public abstract interface PartnerService {

    public abstract List findByQuery(String search, int offset, int limit);

    public abstract BigInteger countByQuery(String search);

    public abstract Partner save(Partner entity);

    public abstract Boolean delete(Long id);
}
