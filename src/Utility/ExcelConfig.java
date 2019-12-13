package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;

public class ExcelConfig {
    
    
    public static String listFilesForFolder(final File folder) {
        String a = "";
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                a = fileEntry.getName();                                                 
            }
        }
        return a;
    }
    
    //the same procedure as ReadExcel but with hardcoded filePath of config file
    public static String ReadExcelConfig(String ColumnName)
    {
        return Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", ColumnName);
    }


    public static String ReadExcel(String filePath, String ColumnName)

    {
        String data = "";
        final File folder = new File("test");    
        String env = listFilesForFolder(folder).substring(0, listFilesForFolder(folder).length() - 4);
        
        try {

            FileInputStream file = new FileInputStream(new File(filePath));

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            // XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFSheet sheet = workbook.getSheet("Sheet1");
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;
            int columnIndex = 0;
            int columnIndex1 = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getRowIndex() == 0) {
                        if (cell.getStringCellValue().equals(ColumnName)) {
                            columnIndex = cell.getColumnIndex();
                        }

                        if (cell.getStringCellValue().equals("Environment")) {
                            columnIndex1 = cell.getColumnIndex();
                        }
                    }

                    if (cell.getColumnIndex() == columnIndex1) {
                        if (cell.getStringCellValue().toUpperCase().equals(env.toUpperCase())) {
                            rowIndex = cell.getRowIndex();
                        }
                    }

                    if (columnIndex != 0 && rowIndex != 0) {
                        data = sheet.getRow(rowIndex).getCell(columnIndex).getStringCellValue().toString();
                        rowIndex = 0;
                        columnIndex = 0;
                    }
                }
            }
            file.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }
    
   
    public static String ReadExcel(String filePath, String Speccolumn, String RowValue, String ColumnName)

    {
        String data = "";
        try {

            FileInputStream file = new FileInputStream(new File(filePath));

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            // XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFSheet sheet = workbook.getSheet("Sheet1");
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;
            int columnIndex = 0;
            int columnIndex1 = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getRowIndex() == 0) {
                        if (cell.getStringCellValue().equals(ColumnName)) {
                            columnIndex = cell.getColumnIndex();
                        }

                        if (cell.getStringCellValue().equals(Speccolumn)) {
                            columnIndex1 = cell.getColumnIndex();
                        }
                    }

                    if (cell.getColumnIndex() == columnIndex1) {
                        if (cell.getStringCellValue().toUpperCase().equals(RowValue.toUpperCase())) {
                            rowIndex = cell.getRowIndex();
                        }
                    }

                    if (columnIndex != 0 && rowIndex != 0) {
                        data = sheet.getRow(rowIndex).getCell(columnIndex).getStringCellValue().toString();
                        rowIndex = 0;
                        columnIndex = 0;
                    }
                }
            }
            file.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }


    @SuppressWarnings("deprecation")
    public static void WriteTestResult(String testName, String state, String screenshot, String iteration, String environment) {
        try {
            FileInputStream file = new FileInputStream(new File("Results/TestResults.xlsx"));

            XSSFWorkbook workbook1 = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook1.getSheet("Sheet1");
            String strDate = new SimpleDateFormat("dd.MM.yyyy:HH:mm").format(Calendar.getInstance().getTime());

            Object[][] testData = {{testName, state, strDate, screenshot, iteration, environment}};

            int rowCount = sheet.getLastRowNum();

            for (Object[] test : testData) {
                Row row = sheet.createRow(++rowCount);

                int columnCount = 0;

                for (Object field : test) {
                    Cell cell = row.createCell(++columnCount);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);

                        XSSFCellStyle style = workbook1.createCellStyle();
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        style.setBorderBottom(CellStyle.BORDER_MEDIUM);
                        style.setBorderRight(CellStyle.BORDER_MEDIUM);

                        if (state.equals("PASSED")) {
                            style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                        }

                        if (state.equals("FAILED")) {
                            style.setFillForegroundColor(IndexedColors.RED.getIndex());
                        }
                        row.getCell(columnCount).setCellStyle(style);

                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                }
            }

            file.close();
            FileOutputStream outputStream = new FileOutputStream("Results/TestResults.xlsx");
            workbook1.write(outputStream);

            workbook1.close();
            outputStream.close();
        } catch (Exception e) {
        }

    }

    public static ArrayList<String> GetListOfTestForExecution(String filePath, String ColumnName, String env)
    {
        ArrayList<String> listForExecution = new ArrayList<String>();
        ArrayList<Integer> listRowIndex = new ArrayList<Integer>();
        try {

            FileInputStream file = new FileInputStream(new File(filePath));

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheet("Sheet1");
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int columnIndex = 0;
            int columnIndex1 = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getRowIndex() == 0) {
                        if (cell.getStringCellValue().equals(ColumnName)) {
                            columnIndex = cell.getColumnIndex();
                        }

                        if (cell.getStringCellValue().toUpperCase().equals(env.toUpperCase())) {
                            columnIndex1 = cell.getColumnIndex();
                        }
                    }

                    if (cell.getColumnIndex() == columnIndex1) {
                        if (cell.getStringCellValue().toUpperCase().equals("ON")) {
                            listRowIndex.add(cell.getRowIndex());
                            listForExecution.add(sheet.getRow(cell.getRowIndex()).getCell(columnIndex).getStringCellValue()
                                    .toString());
                        }
                    }
        
                }
            }
            file.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listForExecution;

    }

}
