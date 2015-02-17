package operations;

import java.io.Serializable;
import java.util.*;
import remote.*;

public class InsertOp<T extends Serializable, J extends Collection<T> & Serializable,
								C extends consistency.Top,
								Obj extends RemoteObject<?>,
											Set extends RemoteObject<? extends Collection<?> >,
														Sto extends Insert<Set,Obj> > extends Operation<Void,C> {
	
	private Handle<J,C,? extends access.Write, ?, Sto> set;
	private Handle<T,C,? extends access.Read, ?, Sto> elem;
	
	public InsertOp(Handle<J,C,? extends access.Write, ?, Sto> set,
					Handle<T,C,? extends access.Read, ?, Sto> elem){
		this.set = set;
		this.elem = elem;
	}

	@Override
	public Void execute(){
		try {
			@SuppressWarnings("unchecked")
				Sto sto = ((Sto) set.ro.getStore());
			@SuppressWarnings("unchecked")
				Set st = (Set) set.ro;
			@SuppressWarnings("unchecked")
				Obj obj = (Obj) elem.ro;
			sto.insert(st,obj);
		}
		catch (ClassCastException e){
			J j = set.ro.get();
			j.add(elem.ro.get());
			set.ro.put(j);
		}
		catch (java.io.IOException e){
			System.err.println("something is very wrong!");
			throw new RuntimeException(e);
		}
		return null;
	}
}
