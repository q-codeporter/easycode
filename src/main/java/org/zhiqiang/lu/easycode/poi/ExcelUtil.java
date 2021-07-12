package org.zhiqiang.lu.easycode.poi;

import java.io.*;
import com.aspose.cells.*; //引入aspose-cells-8.5.2.jar包

import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {
  /**
   * 找到需要插入的行数，并新建一个POI的row对象
   * 
   * @param sheet
   * @param rowIndex
   * @return
   */
  public static void insertRow(Sheet sheet, Integer rowIndex) {
    if (sheet.getRow(rowIndex) != null) {
      int lastRowNo = sheet.getLastRowNum();
      sheet.shiftRows(rowIndex, lastRowNo, 1);
    }
    sheet.createRow(rowIndex);
  }

  /**
   * 找到需要插入的行数，并新建一个POI的row对象
   * 
   * @param sheet
   * @param rowIndex
   * @return
   */
  public static void insertRows(Sheet sheet, Integer rowStart, Integer rowEnd) {
    for (; rowStart <= rowEnd; rowStart++) {
      if (sheet.getRow(rowStart) != null) {
        int lastRowNo = sheet.getLastRowNum();
        sheet.shiftRows(rowStart, lastRowNo, 1);
      }
      sheet.createRow(rowStart);
    }
  }

  public static void toPdf(String path, String path_out) {
    if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
      return;
    }
    try {
      System.out.println("操作系统名称：" + System.getProperty("os.name")); // 操作系统名称
      File pdfFile = new File(path_out);// 输出路径
      Workbook wb = new Workbook(path);// 原始excel路径
      FileOutputStream fileOS = new FileOutputStream(pdfFile);
      wb.save(fileOS, SaveFormat.PDF);
      fileOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static boolean getLicense() {
    boolean result = false;
    try {
      InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream("license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
      License aposeLic = new License();
      aposeLic.setLicense(is);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void main(String[] args) {
    ExcelUtil.toPdf("D:/xls/cs.xlsm", "D:/xls/cs.pdf");
  }
}