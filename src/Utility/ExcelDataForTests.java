package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExcelDataForTests {

   static  List<String> listOfColumns = new ArrayList<>();

   public static List<String> GetAllColumns(String filepath, String sheetName) throws IOException{
       listOfColumns.clear();
       
       FileInputStream file = new FileInputStream(filepath);
       
       @SuppressWarnings("resource")
              XSSFWorkbook workbook = new XSSFWorkbook(file);
       XSSFSheet sheet = workbook.getSheet(sheetName);
       Row HeaderRow = sheet.getRow(0);
              
       //for(int i=1; i < sheet.getPhysicalNumberOfRows(); i++)
       //{
          Row currentRow = sheet.getRow(1);
          for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
          {
              listOfColumns.add(HeaderRow.getCell(j).getStringCellValue());
          }
       //}
       return listOfColumns;
   }
      
      
        @SuppressWarnings("deprecation")
        public static List<HashMap<String,String>> getDataFromExcel(String filepath, String sheetName)        
           {
              List<HashMap<String,String>> mydata = new ArrayList<>();
              try
              {
                 FileInputStream file = new FileInputStream(filepath);
                 
                 @SuppressWarnings("resource")
                        XSSFWorkbook workbook = new XSSFWorkbook(file);
                 XSSFSheet sheet = workbook.getSheet(sheetName);
                 Row HeaderRow = sheet.getRow(0);
                 
                 for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
                 {
                    Row currentRow = sheet.getRow(i);
                    HashMap<String,String> currentHash = new HashMap<String,String>();
                    for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
                    {
                       Cell currentCell = currentRow.getCell(j);
                       
                       currentCell.setCellType(Cell.CELL_TYPE_STRING);
                       
                       switch (currentCell.getCellType())
                       {
                       case Cell.CELL_TYPE_STRING:
                           
                           String celija = "";
                           
                           if(currentCell.getStringCellValue().equals("empty"))
                           {
                                   celija = "";
                           }
                           else{
                                   celija = currentCell.getStringCellValue();
                           }
                                   
                          currentHash.put(HeaderRow.getCell(j).getStringCellValue(), celija);                             
                          break;
                       }
                    }
                    
                    mydata.add(currentHash);
                   
                 }
                 file.close();
              }
              catch (Exception e)
              {                  
                 e.printStackTrace();
              }
              return mydata;
           }
        
       
}
