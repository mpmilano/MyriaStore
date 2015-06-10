package simplecausal;
import remote.*;
import util.*;

public class SimpleNameManager implements NameManager<SafeInteger> {
	public static final SimpleNameManager inst =
		new SimpleNameManager();

	private SimpleNameManager(){}

	@Override
	public SafeInteger ofString(final String s){
		return SafeInteger.ofString(s);
	}

	@Override
	public SafeInteger concat(SafeInteger a, SafeInteger b){
		return a.concat(b);
	}

	@Override
	public String toString(SafeInteger a){
		return a.toString();
	}

	@Override
	public boolean taggedWith(SafeInteger a, SafeInteger b){
		return a.taggedWith(b);
	}
}
