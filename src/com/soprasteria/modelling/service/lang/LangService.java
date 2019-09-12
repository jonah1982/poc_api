package com.soprasteria.modelling.service.lang;

import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.soprasteria.modelling.service.ServiceContext;

public class LangService {
	private ApplicationContext context;
	private MongoDAO dao;

	public LangService(ServiceContext context) {
		super();
		this.context = context;
		dao = new MongoDAO(this.context, context.getBaseMongo());
	}
	
	public String translate(String textContext, String textId, String lang) throws Exception {
		LangDomain domain = new LangDomain(textContext, textId);
		dao.findOne(domain);
		
		return domain.getText(lang);
	}
	
	public void addTranslation(String textContext, String textId, String originText, String lang, String translatedText) throws Exception {
		LangDomain domain = new LangDomain(textContext, textId);
		if(dao.exist(domain)) {
			dao.findOne(domain);
		}
		domain.setText(originText);
		domain.addText(lang, translatedText);
		dao.set(domain);
	}
	
	public void addTranslation(String textContext, String originText, String lang, String translatedText) throws Exception {
		this.addTranslation(textContext, originText, originText, lang, translatedText);
	}
	
	public String translateComplex(String textContext, String textId, String originText, String lang) throws Exception {
		if(lang == null) return originText;
		String text = translate(textContext, originText, lang);
		
		if(text != null) return text;
		
		// else use Bing translate
		context.log("no translation available in db, use Bing to translate.");
		if(!BingTranslatorSoapManager.isValid(context, lang)) throw new Exception("Invalid language code "+lang);
		
		try {
			String result = BingTranslatorSoapManager.autoTranslate(context, originText, lang);
			
			addTranslation(textContext, originText, lang, result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originText;
	}
}
