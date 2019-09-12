package com.soprasteria.modelling.framework.mgmt.compresser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.soprasteria.modelling.framework.context.ApplicationContext;

public class GZipFileManager {
	private ApplicationContext context;

	public GZipFileManager(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void unzip(File input, File output) throws FileNotFoundException, IOException {
		byte[] buffer = new byte[1024];
		GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(input));

		FileOutputStream out = new FileOutputStream(output);

		int len;
		while ((len = gzis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}

		gzis.close();
		out.close();
		context.log("unzip successful file "+input+" to "+output);
	}
}
