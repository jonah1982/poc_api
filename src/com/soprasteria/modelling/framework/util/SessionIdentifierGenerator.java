package com.soprasteria.modelling.framework.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionIdentifierGenerator {

	private SecureRandom random = new SecureRandom();

	public String nextSessionId()
	{
		return new BigInteger(50, random).toString(32);
	}
	
	public static void main(String[]args) {
		SessionIdentifierGenerator gen = new SessionIdentifierGenerator();
		System.out.println(gen.nextSessionId());
		System.out.println(gen.nextSessionId());
		System.out.println(gen.nextSessionId());
	}
}
