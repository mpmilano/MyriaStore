package remote;

import java.util.*;
import java.io.*;
import util.*;

public class IndirectStore<Model extends consistency.Top, S, R> extends Store<Model, RemoteObject, S, R, IndirectStore<Model, S, R>> {

	private Store<Model, ?, S, R, ?> real;
	
	public IndirectStore(Store<Model, ?, S, R, ?> real) {
		this.real = real;
	}

	@Override
	public R this_replica(){
		return real.this_replica();
	}
	
	@Override
	protected <T extends Serializable> RemoteObject newObject(S arg) throws Exception{
		return real.newObject(arg);
	}

	@Override
	protected <T extends Serializable> RemoteObject newObject(S arg, T init) throws Exception{
		return real.newObject(arg, init);
	}

	@Override
	protected S genArg() {return real.genArg(); }

	@Override
	public void registerOnWrite(Function<S,Void> r){
		real.registerOnWrite(r);
	}

	@Override
	public void registerOnRead(Function<S,Void> r){
		real.registerOnRead(r);
	}
	
}
