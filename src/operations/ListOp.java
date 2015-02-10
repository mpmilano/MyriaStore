package operations;

import java.io.Serializable;
import java.util.*;
import remote.*;

public class ListOp<C extends consistency.Top, Obj extends RemoteObject, Sto extends List<Obj> > extends Operation<String[],C> {

	private Handle<? extends Collection<?>,C,? extends access.Read, ?> h;
	
	public ListOp(Handle<? extends Collection<?>,C,? extends access.Read, ?> h ){
		this.h = h;
	}

	public String[] execute(){
		try {
			@SuppressWarnings("unchecked")
			String[] ret = ((Sto) h.ro.getStore()).list((Obj) h.ro);
			return ret;
		}
		catch (ClassCastException e){
			ArrayList<String> ret = new ArrayList<String>();
			Iterator<?> i = h.ro.get().iterator();
			while (i.hasNext()) {
				ret.add(i.next().toString());
			}
			String[] ret2 = new String[ret.size()];
			return ret.toArray(ret2);
		}
	}
}
