package util;

public class MyriaException extends Exception {
	public MyriaException(Exception e){
		super(e);
	}

	public MyriaException(String s){
		super(s);
	}
}
