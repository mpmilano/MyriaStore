package remote;

import java.io.Serializable;
import util.*;

public abstract class RemoteObject<T extends Serializable, K> implements Serializable{

	public abstract T get();

	public abstract void put(T t);

	public abstract K name();

	public abstract Store getStore();


	//type conversions for the win!
	@SuppressWarnings("unchecked")
	public static <T extends Serializable, FSDir, FSDirT extends ParameterizedOn<T> & HasGenericForm<FSDir>>
		FSDirT reify(FSDir fs, RemoteObject<SerializableCollection<T>,?> fs2){
		if (fs == fs2) return (FSDirT) fs;
		else throw new ClassCastException("Attempt to reify to wrong type");
	}

}
