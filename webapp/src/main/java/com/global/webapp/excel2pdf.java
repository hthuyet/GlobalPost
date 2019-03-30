package com.global.webapp;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

public class excel2pdf {
  public static void main(String[] args) throws Exception {

    FileInputStream input_document = new FileInputStream(new File("F:\\Project\\Java\\SpringBoot\\GlobalPost\\webapp\\target\\output_excel_template.xls"));
    // Read workbook into HSSFWorkbook
    HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
    // Read worksheet into HSSFSheet
    HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
    // To iterate over the rows
    Iterator<Row> rowIterator = my_worksheet.iterator();
    //We will create output PDF document objects at this point
    Document iText_xls_2_pdf = new Document();
    PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream("F:\\Project\\Java\\SpringBoot\\GlobalPost\\webapp\\target\\Excel2PDF_Output.pdf"));
    iText_xls_2_pdf.open();
    //we have two columns in the Excel sheet, so we create a PDF table with two columns
    //Note: There are ways to make this dynamic in nature, if you want to.
    PdfPTable my_table = new PdfPTable(2);
    //We will use the object below to dynamically add new data to the table
    PdfPCell table_cell;
    //Loop through rows.
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      Iterator<Cell> cellIterator = row.cellIterator();
      while (cellIterator.hasNext()) {
        Cell cell = cellIterator.next(); //Fetch CELL
        switch (cell.getCellType()) { //Identify CELL type
          //you need to add more code here based on
          //your requirement / transformations
          case Cell.CELL_TYPE_STRING:
            //Push the data from Excel to PDF Cell
            table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
            //feel free to move the code below to suit to your needs
            my_table.addCell(table_cell);
            break;
        }
        //next line
      }

    }
    //Finally add the table to PDF document
    iText_xls_2_pdf.add(my_table);
    iText_xls_2_pdf.close();
    //we created our pdf file..
    input_document.close(); //close xls
  }
}
