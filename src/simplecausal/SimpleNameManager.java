package simplecausal;
import remote.*;
import util.*;

public class SimpleNameManager implements NameManager<SafeInteger> {
	public static final SimpleNameManager inst =
		new SimpleNameManager();

	@Override
	public SafeInteger ofString(String s){
		return SafeInteger.wrap(s.hashCode());
	}

	@Override
	public SafeInteger concat(SafeInteger a, SafeInteger b){
		return SafeInteger.wrap(a.i + b.i);
	}
}
