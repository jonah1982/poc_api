package com.soprasteria.modelling.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileReader {
	public static List<String[]> read(File excel, boolean hasHeader, int sheetIndex) throws Exception {
		Workbook wb = null;
		FileInputStream fis = new FileInputStream(excel);
		
		if(FilenameUtils.getExtension(excel.getName()).equalsIgnoreCase("xls")) {
			wb = new HSSFWorkbook(fis);
		} else if (FilenameUtils.getExtension(excel.getName()).equalsIgnoreCase("xlsx")) {
			wb = new XSSFWorkbook(fis);
		} else {
			fis.close();
			throw new Exception("The file extension must be either .xls or .xlsx");
		}
		
		int sheetcount = wb.getNumberOfSheets();
		List<String[]> data = new ArrayList<>();
		
		for(int index = 0; index < sheetcount; index++) {
			if(sheetIndex >= 0 && index != sheetIndex) continue;
			
			Sheet ws = wb.getSheetAt(index);
			System.out.println("reading sheet: "+ws.getSheetName());
			
			if( ws.getRow(0) == null) continue;

			int rowNum = ws.getLastRowNum() + 1;
			int colNum = ws.getRow(3).getLastCellNum();///int colNum = ws.getRow(0).getLastCellNum();

			int i = 0;
			if(hasHeader) i = 1;
			for(; i <rowNum; i++){
				Row row = ws.getRow(i);
 				if(row != null) {
					String [] rowdata = new String[colNum];
					data.add(rowdata);

					for (int j = 0; j < colNum; j++){
						Cell cell = row.getCell(j);
						rowdata[j] = "";
						try {
							rowdata[j] = cell.toString().trim();
						} catch (Exception e) {
//							System.out.println("error to read line "+i+" due to "+e.getMessage());
//							e.printStackTrace();
						}
					}
				}
			}
		}
		fis.close();
		
		return data;
	}
	
	public static List<String[]> read(File excel, boolean hasHeader) throws Exception {
		return read(excel, hasHeader, -1);
	}
}
