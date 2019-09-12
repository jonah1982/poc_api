package com.soprasteria.modelling.framework.mgmt.compresser;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import com.soprasteria.modelling.framework.context.ApplicationContext;

public class ZipFileManager {
	private ApplicationContext context;

	public ZipFileManager(ApplicationContext context) {
		super();
		this.context = context;
	}
	
	public File unzipAll(File input, File destDir, String password) throws ZipException {
		File destDirreal = new File(destDir, input.getName()+"_out");
		
		if (destDirreal.exists()) {
			destDirreal.delete();
		}
		destDirreal.mkdirs();

		ZipFile zipFile = new ZipFile(input);
		if (zipFile.isEncrypted()) {
			zipFile.setPassword(password);
		}
		zipFile.extractAll(destDirreal.getAbsolutePath());
		context.log("unzip successful file "+input+" to "+destDir);
		
		return destDirreal.listFiles()[0];
	}
}
