package simplecausal;

import consistency.*;
import operations.*;
import access.*;
import remote.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collection;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.io.*;
import util.*;

#define cassert(x,s) assert((new Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

//TODO TODO TODO - CREATION IS A KIND OF WRITE, DUMBASS.  FIX THIS EVERYWHERE.  RIGHT NOW CROSS-STORE IS TAKING CARE OF THINS FOR YOU, BUT ONCE YOU FIX THAT YOU NEED TO FIX IT HERE TOO.

public class SimpleCausal
	extends Store<Causal,
					  SimpleCausal.SimpleRemoteObject<?>,
					  SafeInteger, SafeInteger, SimpleCausal>
	implements HasClock, AccessReplica<Causal,
										   SimpleCausal.SimpleRemoteObject<?>
										   , SafeInteger,
										   SafeInteger,
										   SimpleCausal>,
			   Synq<SafeInteger>, Runnable {
	
	private static ConcurrentNavigableMap<SafeInteger, ImmutableContainer<?>> master
		= new ConcurrentSkipListMap<>();
	private static ConcurrentNavigableMap<SafeInteger, SimpleCausal> replicas =
		new ConcurrentSkipListMap<>();

	private final ConcurrentSkipListMap<SafeInteger, ImmutableContainer<?>> local =
		new ConcurrentSkipListMap<>();
	private final SafeInteger myID =
		SafeInteger.ofString(NonceGenerator.get());

	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public SimpleCausal(){
		replicas.put(myID, this);
		registerOnTick(this);
	}

	@Override
	public void run(){
		try{
			lock.writeLock().lock();
			synchronized(master){
				for (final Map.Entry<SafeInteger, ImmutableContainer<?>> e : local.entrySet()){
					{
						cassert(e != null, "null entry found in local store!");
						cassert(e.getValue() != null, "null entry found in local store value!");
						cassert(e.getKey() != null,"null key found in local store!");
					}
					@SuppressWarnings("unchecked")
						final Mergable<RCloneable<?>> elem
						= (Mergable<RCloneable<?>>) e.getValue().get();
					{
						cassert(elem != null, "ImmutableContainer contains null in local cache!");
					}
					SafeInteger key = e.getKey();
					{
						cassert(master != null, "no master cache!");
					}
					RCloneable<?> merged =
						elem.merge(ImmutableContainer.readOnlyIfExists
								   (master.get(key)));
					
					@SuppressWarnings("unchecked")
						ImmutableContainer<?> boxed_merged =
						new ImmutableContainer(merged);
					master.put(key, boxed_merged);
				}
				for (Map.Entry<SafeInteger, ImmutableContainer<?>> e :
						 master.entrySet()){
					local.put(e.getKey(),e.getValue());
				}
			}
		}
		finally{
			lock.writeLock().unlock();
		}
		/*
		for (Map.Entry<SafeInteger,ImmutableContainer<?> > pair : master.entrySet()){
			if (local.containsKey(pair.getKey())){
				System.out.println("Master At position " + pair.getKey().toString() + ": " + pair.getValue().readOnlyIpromise().toString());
				System.out.println("Local At position " + pair.getKey().toString() + ": " + local.get(pair.getKey()).readOnlyIpromise().toString());
			}
			}//*/

	}

	@Override
	public Timestamp currentTime(){
		return new Timestamp(System.currentTimeMillis(), 10);
	}

	@Override
	public SimpleCausal access_replica(SafeInteger i){
		return replicas.get(i);
	}

	@Override
	public boolean sync_req(SafeInteger from, SafeInteger to){
		//TODO - simulate the need for this.  Right now it's not keenly felt.
		return false;
	}

	@Override
	public SafeInteger this_replica(){
		return myID;
	}

	@Override
	public SafeInteger genArg(){
		return SafeInteger.ofString(NonceGenerator.get());
	}
	
	@Override
	protected <T extends Serializable>
		SimpleRemoteObject<?> newObject(SafeInteger i) {
		return new SimpleRemoteObject(i);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> SimpleRemoteObject<?>
		newObject(SafeInteger i, T t){
		return new
			SimpleRemoteObject(i,CloneFromMergable.wrap(MakeMerge.wrap(t)));
	}

	@Override
	protected boolean exists(SafeInteger i){
		return local.containsKey(i);
	}

	public class SimpleRemoteObject
		<T extends Serializable &
				   Mergable<T> &
				   RCloneable<T>> 
		extends RemoteObject<T, SafeInteger>
		implements ParameterizedOn<T>, HasGenericForm<SimpleRemoteObject<?>>
	{

		public SafeInteger a;

		public SimpleRemoteObject(final SafeInteger name){
			cassert(local.get(name) != null,"Null found upon get for valid name!");
			a = name;
		}
		
		public SimpleRemoteObject(SafeInteger name, final T t){
			try{
				lock.readLock().lock();
				cassert(t != null, "SimpleRemote constructed with null object!");
				this.a = name;
				local.put(name,new ImmutableContainer<>(t));
			}
			finally{
				lock.readLock().unlock();
			}
		}

		@Override
		public SafeInteger name(){
			return a;
		}

		@Override
		public SimpleCausal getStore()
		{ return SimpleCausal.this; }

		@Override
		public void put(T t){
			for (Function<SafeInteger, Void> f : onWrite)
				f.apply(a);
			try{
				lock.readLock().lock();
				local.put(a,new ImmutableContainer<>(t));
			}
			finally{
				lock.readLock().unlock();
			}
		}

		@Override
		public T get(){
			for (Function<SafeInteger, Void> f : onRead){
				f.apply(a);
			}
			//sorry, me
			@SuppressWarnings("unchecked")
			T t = (T) local.get(a).get();
			return t;
		}
		
	}

	private java.util.List<Function<SafeInteger, Void>> onRead = new CopyOnWriteArrayList<>();
	private java.util.List<Function<SafeInteger, Void>> onWrite = new CopyOnWriteArrayList<>();

	@Override
	public void registerOnRead(final Function<SafeInteger, Void> f){
		onRead.add(f);
		cassert(onRead.contains(f),"adding didn't work!");
	}

	@Override
	public void registerOnWrite(Function<SafeInteger, Void> f){
		onWrite.add(f);
	}
	
}
