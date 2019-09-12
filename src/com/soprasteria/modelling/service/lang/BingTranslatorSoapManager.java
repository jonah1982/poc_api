package com.soprasteria.modelling.service.lang;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.soprasteria.modelling.framework.context.ApplicationContext;

public class BingTranslatorSoapManager {
	public static List<String> languages;

	private static final String AppId = "32BA0BF1609128EF1E37D5F09C8814B4B1D8EF2B";
	private static final String Url = "http://api.microsofttranslator.com/V2/SOAP.svc";
	private static final String Namespace = "http://api.microsofttranslator.com/V2";

	public class Language {
		public String code = null;
		public String name = null;
		public ArrayList<Language> spokenLangs = new ArrayList<Language>();
	}

	private static HttpTransportSE transport = new HttpTransportSE(Url);
	private static SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER11);
	
	public static List<String> getLanguages(ApplicationContext context) {
		if(languages == null) {
			languages = getLanguagesForTranslate(context);
		}
		
		return languages;
	}
	
	public static boolean isValid(ApplicationContext context, String lang) {
		if(getLanguages(context).contains(lang)) return true;
		else return false;
	}

	public static List<String> getLanguagesForTranslate(ApplicationContext context) {
		List<String> langs = new ArrayList<>();
		String method = "GetLanguagesForTranslate";
		String soapAction = "http://api.microsofttranslator.com/V2/LanguageService/GetLanguagesForTranslate";
		try {
			SoapObject request = new SoapObject(Namespace, method);
			request.addProperty("appId", AppId);
			envelope.setOutputSoapObject(request);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			transport.debug = true;
			transport.call(soapAction, envelope);
			Object result = envelope.getResponse();
			if (result == null) {
				context.print("Error get response: null");
				return langs;
			} else {
				SoapObject so = (SoapObject) result;
				context.print("response count  "
						+ so.getPropertyCount());
				if (so.getPropertyCount() < 1) {
					return langs;
				}
				for (int i = 0; i < so.getPropertyCount(); ++i) {
					context.print("prop [" + i + "],  = "
							+ so.getProperty(i).toString());
					langs.add(so.getProperty(i).toString());
				}
				context.print("response is " + result.toString());
				return langs;
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.print("error: " + e.toString());
			context.print("\r\n\r\n\r\n");
			context.print("Soap request is: " + transport.requestDump);
			context.print("\r\n\r\n\r\n");
			context.print("Soap response is: " + transport.responseDump);
			context.print("\r\n\r\n\r\n");
			return langs;
		}
	}

	public static String[] getLanguagesForSpeak(ApplicationContext context) {
		String method = "GetLanguagesForSpeak";
		String soapAction = "http://api.microsofttranslator.com/V2/LanguageService/GetLanguagesForSpeak";
		try {
			SoapObject request = new SoapObject(Namespace, method);
			request.addProperty("appId", AppId);
			envelope.setOutputSoapObject(request);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			transport.debug = true;
			transport.call(soapAction, envelope);
			Object result = envelope.getResponse();
			if (result == null) {
				context.print("Error get response: null");
				return null;
			} else {
				SoapObject so = (SoapObject) result;
				context.print("response count  "
						+ so.getPropertyCount());
				if (so.getPropertyCount() < 1) {
					return null;
				}
				String ret[] = new String[so.getPropertyCount()];
				for (int i = 0; i < so.getPropertyCount(); ++i) {
					context.print("prop [" + i + "],  = "
							+ so.getProperty(i).toString());
					ret[i] = so.getProperty(i).toString();
				}
				context.print("response is " + result.toString());
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.print("error: " + e.toString());
			context.print("\r\n\r\n\r\n");
			context.print("Soap request is: " + transport.requestDump);
			context.print("\r\n\r\n\r\n");
			context.print("Soap response is: " + transport.responseDump);
			context.print("\r\n\r\n\r\n");
			return null;
		}
	}
	
	public static String translate(ApplicationContext context, String input, String from, String to) throws Exception {
		String res = "";
		
		String method = "Translate";
		String soapAction = "http://api.microsofttranslator.com/V2/LanguageService/"+method;
		try {
			SoapObject request = new SoapObject(Namespace, method);
			request.addProperty("appId", AppId);
			request.addProperty("text", input);
			request.addProperty("from", from);
			request.addProperty("to", to);
			envelope.setOutputSoapObject(request);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			transport.debug = true;
			transport.call(soapAction, envelope);
			Object result = envelope.getResponse();
			context.print("translated to \"" + to + "\" is " + result);
			
			if (result == null) {
				context.print("Error get response: null");
			} else {
				SoapPrimitive sp = (SoapPrimitive) result;
				res = sp.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.print("error: " + e.toString());
			context.print("\r\n\r\n\r\n");
			context.print("Soap request is: " + transport.requestDump);
			context.print("\r\n\r\n\r\n");
			context.print("Soap response is: " + transport.responseDump);
			context.print("\r\n\r\n\r\n");
			throw new Exception("Error translating");
		}
		
		return res;
	}
	
	public static String detect(ApplicationContext context, String input) throws Exception {
		String lang = "en";
		
		String method = "Detect";
		String soapAction = "http://api.microsofttranslator.com/V2/LanguageService/"+method;
		try {
			SoapObject request = new SoapObject(Namespace, method);
			request.addProperty("appId", AppId);
			request.addProperty("text", input);
			envelope.setOutputSoapObject(request);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			transport.debug = true;
			transport.call(soapAction, envelope);
			Object result = envelope.getResponse();
			context.print("detected lang is \"" + result + "\"");
			
			if (result == null) {
				context.print("Error get response: null");
			} else {
				SoapPrimitive sp = (SoapPrimitive) result;
				lang = sp.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.print("error: " + e.toString());
			context.print("\r\n\r\n\r\n");
			context.print("Soap request is: " + transport.requestDump);
			context.print("\r\n\r\n\r\n");
			context.print("Soap response is: " + transport.responseDump);
			context.print("\r\n\r\n\r\n");
			throw new Exception("Error detecting language");
		}
		
		return lang;
	}
	
	public static String autoTranslate(ApplicationContext context, String input, String to) throws Exception {
		context.print("translating input text \"" + input + "\" to \"" + to + "\"");
		String from = detect(context, input);
		return translate(context, input, from, to);
	}
	
	public static void main(String args[]) throws Exception {
		ApplicationContext context = new ApplicationContext();
		String str = "location";
		List<String> langs = getLanguagesForTranslate(context);
		
		System.out.println("valid language: "+isValid(context, "ko"));
		for (int i = 0; i < langs.size(); i++) {
			System.out.println("lang code: "+langs.get(i));
//			String lang = detect(context, str);
//			translate(context, str, lang, languages[i]);
			try {
				autoTranslate(context, str, langs.get(i));
			} catch (Exception e) {
				context.print("MAIN: autoTranslate exception");
			}
		}
		try {
			System.out.println();
			autoTranslate(context, "end", "ja");
		} catch (Exception e) {
		}
		
		String lang = detect(context, str);
		translate(context, str, lang, "zh-CHS");
		translate(context, str, lang, "en");
	}
}