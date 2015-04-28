package util;

import java.security.SecureRandom;
import java.math.*;

public class NonceGenerator{
	private SessionIdentifierGenerator sid = new SessionIdentifierGenerator();
	public static NonceGenerator inst = new NonceGenerator();
	private NonceGenerator(){}

	public static String get(){
		return inst.sid.nextSessionId();
	}
	
	public final class SessionIdentifierGenerator {
		private SecureRandom random = new SecureRandom();
		
		public String nextSessionId() {
			return new BigInteger(130, random).toString(32);
		}
	}
}
