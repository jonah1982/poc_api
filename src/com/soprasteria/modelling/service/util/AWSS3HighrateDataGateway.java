package com.soprasteria.modelling.service.util;

import java.io.File;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.soprasteria.modelling.framework.config.PropertyFileSetting;
import com.soprasteria.modelling.framework.exception.ServiceException;

public class AWSS3HighrateDataGateway {
	public static String SENSOR_PRESSURE_HIGHRATEDATA_BUCKET_PREFIX = "visenti-pressure-data";
	private AWSCredentials credentials = new BasicAWSCredentials(PropertyFileSetting.getSetting("aws.accesskeyid", null), PropertyFileSetting.getSetting("aws.secretaccesskey", null));
	private AmazonS3 s3client;
	
	public AWSS3HighrateDataGateway() {
		s3client = new AmazonS3Client(credentials);
	}
	
	public void upload(String bucket, String folder, File f) throws ServiceException {
		try {
			s3client.putObject(new PutObjectRequest(bucket, folder+"/"+f.getName(), f).withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("Failed to upload file ("+f.getAbsolutePath()+") to AWS ("+bucket+"/"+folder+") due to "+e.getMessage());
		}
	}
	
	public void uploadPressureData(String zone, File f) throws ServiceException {
		upload(SENSOR_PRESSURE_HIGHRATEDATA_BUCKET_PREFIX+"-"+zone, zone, f);
	}
}
