package simplecausal;

import consistency.*;
import operations.*;
import access.*;
import remote.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collection;
import java.util.*;
import java.io.*;
import util.*;

public class SimpleCausal
	extends Store<Causal,
					  SimpleCausal.SimpleRemoteObject<?>,
					  Integer, Integer, SimpleCausal>
	implements HasClock, AccessReplica<Causal,
										   SimpleCausal.SimpleRemoteObject<?>
										   , Integer,
										   Integer,
										   SimpleCausal>, Synq<Integer>
				
{

	private static HashMap<Integer, RCloneable<?>> master
		= new HashMap<>();
	private static HashMap<Integer, SimpleCausal> replicas =
		new HashMap<>();

	private Integer myID = NonceGenerator.get().hashCode();

	public SimpleCausal(){
		throw new UnsupportedOperationException("TODO: finish");
		//TODO: onTick, sync.
	}

	@Override
	public Timestamp currentTime(){
		return new Timestamp(System.currentTimeMillis(), 10);
	}

	@Override
	public SimpleCausal access_replica(Integer i){
		return replicas.get(i);
	}

	@Override
	public boolean sync_req(Integer from, Integer to){
		//TODO - need a sync method in here, for both
		//this and onTick.
		return false;
	}

	@Override
	public Integer this_replica(){
		return myID;
	}

	@Override
	public Integer genArg(){
		return NonceGenerator.get().hashCode();
	}
	
	@Override
	protected <T extends Serializable> SimpleRemoteObject<?> newObject(Integer i){
		
		return new SimpleRemoteObject<CloneFromMergable<MakeMerge<T>>>(i);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> SimpleRemoteObject<?>
		newObject(Integer i, T t){
		return new
			SimpleRemoteObject(i,CloneFromMergable.wrap(MakeMerge.wrap(t)));
	}

	public class SimpleRemoteObject<T extends Serializable &
											  Mergable<T> &
											  RCloneable<T>> 
		implements RemoteObject<T>{

		private HashMap<Integer, RCloneable<?>> local = new HashMap<>();

		public Integer a;
		public T b;

		public SimpleRemoteObject(Integer name){
			@SuppressWarnings("unchecked")
			T t = (T) local.get(name);
			b = t;
			assert(b != null);
			a = name;
		}
		
		public SimpleRemoteObject(Integer name, T t){
			assert(t != null);
			local.put(name,t);
			this.a = name;
			this.b = t;
		}

		public SimpleCausal getStore()
		{ return SimpleCausal.this; }

		@Override
		public void put(T t){
			for (Function<Integer, Void> f : onWrite)
				f.apply(a);
			b = t;
			assert(local.get(a) == this);
		}

		@Override
		public T get(){
			for (Function<Integer, Void> f : onRead)
				f.apply(a);
			return b;
		}
		
	}

	private LinkedList<Function<Integer, Void>> onRead = new LinkedList<>();
	private LinkedList<Function<Integer, Void>> onWrite = new LinkedList<>();

	@Override
	public void registerOnRead(Function<Integer, Void> f){
		onRead.add(f);
	}

	@Override
	public void registerOnWrite(Function<Integer, Void> f){
		onWrite.add(f);
	}
	
}
