package com.soprasteria.modelling.framework.util;

public class Validator {

	public static void validateEmail(String email) throws Exception {
		if(Tool.isEmpty(email)) throw new Exception("Email cant be null.");
	}
}
