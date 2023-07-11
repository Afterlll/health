package com.itheima.health;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestPOI {
    /**
     * 从Excel文件读取数据
     * @throws IOException
     */
    @Test
    public void test01() throws IOException {
        // 有空行没有关系
        // 创建工作簿, 注意：必须使用参数为InputStream的构造器，否则close()时会出现: OpenXML4JRuntimeException
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("D:\\1.xlsx"));

        // 获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheetAt0 = workbook.getSheet("Sheet3");

        // 遍历工作表获得行对象
        for (Row row : sheetAt0) {
            // 遍历行对象获取单元格对象
            for (Cell cell : row) {
                // 获得单元格中的值
//                double value = cell.getNumericCellValue();
                String value = cell.getStringCellValue();
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        workbook.close();
    }

    /**
     * 从Excel文件读取数据
     * 这种方式在行数据中间如果出现空的话会报空指针异常
     * @throws IOException
     * @throws InvalidFormatException
     */
    @Test
    public void test02() throws IOException, InvalidFormatException {
        // 有空行会出现空指针异常
        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("D:\\1.xlsx"));

        // 获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheetAt0 = workbook.getSheetAt(1);

        // 获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheetAt0.getLastRowNum();
        // System.out.println("最后一行的行号 = " + lastRowNum);
        for (int i = 0; i <= lastRowNum; i++) {
            // 根据行号获取行对象
            XSSFRow row = sheetAt0.getRow(i);

            // 获取当前行的最后一列的列号，列号从1开始
            short lastCellNum = row.getLastCellNum();
            // System.out.println("\t最后一列的行号 = " + lastCellNum);
            for (short j = 0; j < lastCellNum; j++) {
                String value = row.getCell(j).getStringCellValue();
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        workbook.close();
    }

    /**
     * 向Excel文件写入数据
     * @throws IOException
     */
    @Test
    public void test03() throws IOException {
        // 在内存中创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 创建工作簿
        XSSFSheet sheet = workbook.createSheet("health");

        // 创建一行
        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue("编号");
        row1.createCell(1).setCellValue("名称");
        row1.createCell(2).setCellValue("年龄");

        // 创建一行
        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue(1);
        row2.createCell(1).setCellValue("张三");
        row2.createCell(2).setCellValue(11);

        // 创建一行
        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue(2);
        row3.createCell(1).setCellValue("李四");
        row3.createCell(2).setCellValue(22);

        // 把数据写入到文件中
        FileOutputStream fos = new FileOutputStream("D:\\2.xlsx");
        workbook.write(fos);

        fos.close();
        workbook.close();
    }
}