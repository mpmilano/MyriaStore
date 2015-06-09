package remote;

import java.util.*;
import java.io.*;
import util.*;

public class IndirectStore<Model extends consistency.Top, S, R> extends Store<Model, RemoteObject, S, R, IndirectStore<Model, S, R>> {

	private Store<Model, ?, S, R, ?> real;
	
	public IndirectStore(Store<Model, ?, S, R, ?> real) {
		this.real = real;
	}

	public void reset(Store<Model,?,S,R,?> real){
		this.real = real;
	}

	@Override
	public R this_replica(){
		return real.this_replica();
	}

	@Override
	protected boolean exists(S arg){
		return real.objectExists(arg);
	}
	
	@Override
	protected <T extends Serializable> RemoteObject newObject(S arg) throws util.MyriaException{
		return real.newObject(arg);
	}

	@Override
	protected <T extends Serializable> RemoteObject newObject(S arg, T init) throws util.MyriaException{
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

	@Override
	public void registerOnTick(Runnable r){
		real.registerOnTick(r);
	}

	@Override
	public void tick(){
		real.tick();
	}
	
}
