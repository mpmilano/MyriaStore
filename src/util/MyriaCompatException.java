package util;

public class MyriaCompatException extends MyriaException {

	private final Class<?> violated_constraint;
	
	public MyriaCompatException(String s, Class<?> violated_constraint){
		super(s);
		this.violated_constraint = violated_constraint;
	}

	public boolean check(Object o){
		return violated_constraint.isInstance(o);
	}
}
