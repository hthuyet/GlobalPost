/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.services;

import com.global.service.model.Bill;
import com.global.service.model.BillForm;
import com.global.service.model.BillResponse;
import com.global.service.model.report.EmployeeReport;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author HungNT
 */
@Service
public interface BillService {

  public abstract List<BillResponse> findByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit);
  public abstract List<BillResponse> findImport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit);
  public abstract List<BillResponse> findExport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile, int offset, int limit);

  public abstract BigInteger countByQuery(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile);
  public abstract BigInteger countImport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile);
  public abstract BigInteger countExport(String billNo, int state, Long from, Long to, String sName, String sMobile, String rName, String rMobile);

  public abstract BillResponse getById(Long billId);

  public abstract BillResponse getByCode(String code);

  public abstract Boolean delete(Long id);

  public abstract Bill save(BillForm billParameter);

  public abstract Integer exeImport(int type, Long branchId, List<Long> lstId);

  public abstract Integer exeExport(int type, List<Long> lstId);

  //for report
  public abstract List<EmployeeReport> reportByEmployee(Long employ, Date startDate, Date endDate);
  public abstract List<EmployeeReport> reportByCustomer(Long customer, Date startDate, Date endDate);
  public abstract List<EmployeeReport> reportByPartner(Long partner, Date startDate, Date endDate);
  public abstract List<EmployeeReport> reportByBranch(Long branch, Date startDate, Date endDate);
}
