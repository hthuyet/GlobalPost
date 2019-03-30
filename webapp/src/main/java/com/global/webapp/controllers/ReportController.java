package com.global.webapp.controllers;

import com.global.webapp.clients.BillClient;
import com.global.webapp.clients.ReportClient;
import com.global.webapp.models.Car;
import com.global.webapp.models.EmployeeReport;
import com.global.webapp.models.Test;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(ReportController.class);

  @Autowired
  protected BillClient billClient;

  @Autowired
  protected ReportClient reportClient;



  @Autowired
  protected HttpSession session;

  @Autowired
  HttpSession httpSession;

  @Autowired
  private ServletContext servletContext;

  @GetMapping("/report")
  public ResponseEntity<Resource> index() {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to report page", "", "");
      List cars = generateTestCarData();

      URL fileResource = this.getClass().getClassLoader().getResource("input_excel_template.xls");

      File file = new File(fileResource.toURI());

      InputStream is = new FileInputStream(file);

      String fileout = "F:\\Project\\Java\\SpringBoot\\GlobalPost\\webapp\\target\\output_excel_template.xls";
      try (OutputStream os = new FileOutputStream(fileout)) {
        Context context = new Context();
        context.putVar("cars", cars);
        JxlsHelper.getInstance().processTemplate(is, os, context);
      }

      File fileOut = new File(fileout);
      Path path = Paths.get(fileOut.getAbsolutePath());
      ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


      return ResponseEntity.ok()
          // Content-Disposition
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileOut.getName())
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .contentLength(file.length())
          .body(resource);

    } catch (Exception ex) {
      logger.error("ERROR report index: ", ex);
      return null;
    }
  }

  public List<Car> generateTestCarData() {
    return Arrays.asList(new Car("BMW", 10000), new Car("Subaru", 12000));
  }

  @PostMapping("/report/exeExport")
//    @PreAuthorize("hasAuthority('GLOBAL:BILL:EXPORT')")
  @ResponseBody
  public ResponseEntity exeExport(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute export bill", "", "");
      String rtn = billClient.exeExport(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }


  @GetMapping("/report2")
  public ResponseEntity<Resource> index2() {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to report page 2", "", "");
      List data = gernerateData();

      URL fileResource = this.getClass().getClassLoader().getResource("Book4.xlsx");

      File file = new File(fileResource.toURI());

      InputStream is = new FileInputStream(file);

//      String fileout = "F:\\Project\\Java\\SpringBoot\\GlobalPost\\webapp\\target\\output_excel_template.xls";
      File tempFile = File.createTempFile("myfile", ".xlsx");
      try (OutputStream os = new FileOutputStream(tempFile)) {
        Context context = new Context();
        context.putVar("items", data);
        JxlsHelper.getInstance().processTemplate(is, os, context);
      }

      File fileOut = new File(tempFile.getPath());
      Path path = Paths.get(fileOut.getAbsolutePath());
      ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


      return ResponseEntity.ok()
          // Content-Disposition
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileOut.getName())
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .contentLength(file.length())
          .body(resource);

    } catch (Exception ex) {
      logger.error("ERROR report index: ", ex);
      return null;
    }
  }

  public List<Test> gernerateData() {
    List<Test> rtn = new ArrayList<>();
    Test test = null;
    for (int i = 1; i < 15; i++) {
      test = new Test(i);
      rtn.add(test);
    }
    return rtn;
  }

  @PostMapping("/report2")
  public void report2(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) {
    try {
      List data = gernerateData();

      URL fileResource = this.getClass().getClassLoader().getResource("Book4.xlsx");

      File file = new File(fileResource.toURI());

      InputStream is = new FileInputStream(file);

//      String fileout = "F:\\Project\\Java\\SpringBoot\\GlobalPost\\webapp\\target\\output_excel_template.xls";
      File tempFile = File.createTempFile("myfile", ".xlsx");
      try (OutputStream os = new FileOutputStream(tempFile)) {
        Context context = new Context();
        context.putVar("items", data);
        JxlsHelper.getInstance().processTemplate(is, os, context);
      }


      FileInputStream inputStream = new FileInputStream(new File(tempFile.getPath()));

      response.setHeader("Content-Disposition", "attachment; filename=\"testExcel.xlsx\"");
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

      ServletOutputStream outputStream = response.getOutputStream();
      IOUtils.copy(inputStream, outputStream);

      outputStream.close();
      inputStream.close();

    } catch (Exception ex) {
      logger.error("ERROR report index: ", ex);
    }
  }


  @PostMapping("/report")
  public ResponseEntity<Resource> report(@RequestBody Map<String, Object> params) {
    try {

      String dataApi = reportClient.report(params);

      if(dataApi != null && dataApi.trim().length() >0) {
//        List data = gernerateData();
        List<EmployeeReport> data = new Gson().fromJson(dataApi,List.class);

        URL fileResource = this.getClass().getClassLoader().getResource("template.xlsx");

        File file = new File(fileResource.toURI());

        InputStream is = new FileInputStream(file);

        File tempFile = File.createTempFile("myfile", ".xlsx");
        try (OutputStream os = new FileOutputStream(tempFile)) {
          Context context = new Context();
          context.putVar("items", data);
          JxlsHelper.getInstance().processTemplate(is, os, context);
        }

        File fileOut = new File(tempFile.getPath());
        Path path = Paths.get(fileOut.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


        return ResponseEntity.ok()
            // Content-Disposition
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileOut.getName())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(fileOut.length())
            .body(resource);

      }
      return null;
    } catch (Exception ex) {
      logger.error("ERROR report index: ", ex);
      return null;
    }
  }
}
