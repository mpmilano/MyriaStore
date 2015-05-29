package util;

import java.security.SecureRandom;
import java.math.*;

public class NonceGenerator{
	private SessionIdentifierGenerator sid = new SessionIdentifierGenerator();
	private static NonceGenerator inst = new NonceGenerator();
	private NonceGenerator(){}


	private static int i = 0;
	
	public static synchronized String get(){
		//return inst.sid.nextSessionId();
		return i++ + "";
	}
	
	private final class SessionIdentifierGenerator {
		private SecureRandom random = new SecureRandom();
		
		public String nextSessionId() {
			return new BigInteger(130, random).toString(32);
		}
	}
}
