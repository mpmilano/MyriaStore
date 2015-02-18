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
	private Handle<T,C,? extends access.Read, ?, Sto> elem = null;
	private T elem2 = null;
	
	public InsertOp(Handle<J,C,? extends access.Write, ?, Sto> set,
					Handle<T,C,? extends access.Read, ?, Sto> elem){
		this.set = set;
		this.elem = elem;
	}

	public InsertOp(Handle<J,C,? extends access.Write, ?, Sto> set, T elem){
		this.set = set;
		this.elem2 = elem2;
	}


	@Override
	public Void execute(){
			try {
				//This try block casts the store and object to
				//those natively supported by the store's insert operation.
				//If it doesn't support such an operation, this will fail
				//and fall back to the everystore catch-block implementation.
				@SuppressWarnings("unchecked")
					Sto sto = ((Sto) set.ro.getStore());
				@SuppressWarnings("unchecked")
					Set st = (Set) set.ro;
				if (elem != null){
					@SuppressWarnings("unchecked")
						Obj obj = (Obj) elem.ro;
					sto.insert(st,obj);
				}
				else {
					sto.insert(st,elem2);
				}
			}
			catch (ClassCastException e){
				J j = set.ro.get();
				if (elem != null) j.add(elem.ro.get());
				else j.add(elem2);
				set.ro.put(j);
			}
			catch (java.io.IOException e){
				System.err.println("something is very wrong!");
				throw new RuntimeException(e);
			}
		return null;
	}
}
