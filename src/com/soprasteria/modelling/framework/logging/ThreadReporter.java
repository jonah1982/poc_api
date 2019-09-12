package com.soprasteria.modelling.framework.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ThreadReporter {
	private String msg_prefix = "";
	private String path;
	private BufferedWriter writer;
	private int filecount = 0;
	private File currentFile;
	
	private int linecount = 0;
	
	public ThreadReporter(String msg_prefix, String path) throws IOException {
		this.path = path;
		assignNextLogFile();
	}
	
	public void logMessage(String msg) {
		try {
			writer.write(new Date()+"-"+msg_prefix+"-"+msg+System.getProperty("line.separator"));
			flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void assignNextLogFile() throws IOException {
		File f = new File(path+"("+Thread.currentThread().getName()+")"+"-"+filecount+".log");
		while(f.exists()) {
			filecount ++;
			f = new File(path+"("+Thread.currentThread().getName()+")"+"-"+filecount+".log");
		}
		currentFile = f;
		
		if(writer != null) writer.close();
		writer = new BufferedWriter(new FileWriter(currentFile));
	}
	
	public void flush() {
		try {
			linecount++;
			
			if(linecount > 100){
				writer.flush();
				linecount = 0;
			}
			if((currentFile.length()/(1024*1024)) > 50) assignNextLogFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
