package simplecausal;
import remote.*;
import util.*;

public class SimpleNameManager implements NameManager<Integer> {
	public static final SimpleNameManager inst =
		new SimpleNameManager();

	@Override
	public Integer ofString(String s){
		return s.hashCode();
	}

	@Override
	public Integer concat(Integer a, Integer b){
		return a + b;
	}
}
