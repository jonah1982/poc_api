package com.soprasteria.modelling.report.framework;

import java.io.File;

public interface DataModelReportITF {
	public File getDirectory() throws Exception;
	public File getFile() throws Exception;
	public String getTitle();
	public File generate() throws Exception;
}
