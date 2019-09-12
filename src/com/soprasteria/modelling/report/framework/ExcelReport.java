package com.soprasteria.modelling.report.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.soprasteria.modelling.framework.entity.TableField;
import com.soprasteria.modelling.framework.entity.TableView;
import com.soprasteria.modelling.framework.util.Tool;

public class ExcelReport implements DataModelReportITF {
	public final static String REPORT_DIR = "/nfsshare/report/";
	private TableView data;
	
	@Override
	public File getDirectory() throws Exception {
		File dir = new File(REPORT_DIR);
		if(!dir.exists()) Tool.createFolder(dir);
		if(!dir.canRead()) throw new Exception("The report root directory "+dir+" is not readable.");
		if(!dir.canWrite()) throw new Exception("The report root directory "+dir+" is not writable."); 
		return dir;
	}

	@Override
	public File getFile() throws Exception {
		return new File(getDirectory(), getName()+".xls");
	}
	
	public String getName() {
		return "data";
	}

	@Override
	public String getTitle() {
		return "Report";
	}

	public void setData(TableView data) {
		this.data = data;
	}

	@Override
	public File generate() throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");
        
        generateSheet(sheet);
        
        FileOutputStream fileOut = new FileOutputStream(getFile());
        workbook.write(fileOut);
        fileOut.close();
        
		return getFile();
	}
	
	private void generateSheet(HSSFSheet sheet) {
		if(data.getEntries().isEmpty()) return;
		
		List<TableField> header = data.getEntries().get(0);
		HSSFRow rowhead = sheet.createRow((short)0);
		for (int i = 0; i < header.size(); i++) {
			rowhead.createCell(i).setCellValue(header.get(i).getDisplay_name());
		}
		
		for (int i = 0; i < data.getEntries().size(); i++) {
			List<TableField> entry = data.getEntries().get(i);
			HSSFRow row = sheet.createRow((short)i+1);
			for (int j = 0; j < entry.size(); j++) {
				row.createCell(j).setCellValue(""+header.get(j).getValue());
			}
		}
	}

}
