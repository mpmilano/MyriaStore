package logstore;

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


public class LogStore extends Store<Causal, LogStore.LogObject<?>, String, LogStore, LogStore>
	implements Insert<LogStore.LogObject<? extends Collection<? extends Serializable>>, LogStore.LogObject<?>>,
			   AccessReplica<Causal, LogStore.LogObject<?>, String, LogStore, LogStore>
{
	LinkedList<LinkedList<Runnable>> log = new LinkedList<>();

	public static LogStore inst = new LogStore();
	
	private LogStore(){}

	@Override
	protected String genArg(){
		return java.util.UUID.randomUUID().toString();
	}


	@Override
	protected boolean exists(String s){
		return LogObject.cache.get(s) != null;
	}
	
	static class LogObject<T extends Serializable & Mergable<T> > extends RemoteObject<T, String> {

		static Map<String, Object> cache = new HashMap<>();
		
		private T t;
		private final Class<T> cls;
		private String name;

		@Override
		public String name(){return name;}

		private LogObject(String name, Class<T> cls, T t) throws MyriaIOException
		{
			this.cls = cls;
			if (t == null){
				T t_ = cls.cast(cache.get(name));
				t = t_;
				if (t == null) throw new MyriaIOException(new IOException("Can't find this name!"));
			}
			else {
				this.name = name; this.t = t;
				cache.put(name,t);
			}
		}
		
		@Override
		public LogStore getStore() {return inst;}

		@Override
		public void put(final T t){
			inst.log.getLast().add(new Runnable(){
					public void run(){
						LogObject.this.t = t;
					}
				});
			for (util.Function<String, Void> f : inst.onWrite) {
				f.apply(name);
			}
		}
		
		@Override
		public T get(){
			for (util.Function<String, Void> f : inst.onRead){
				f.apply(name);
			}
			return t;
		}
	}

	private java.util.ArrayList<util.Function<String, Void>> onWrite;
	private java.util.ArrayList<util.Function<String, Void>> onRead;
	
	@Override
	public void registerOnRead(util.Function<String, Void> f){
		onRead.add(f);
	}

	@Override
	public void registerOnWrite(util.Function<String, Void> f){
		onWrite.add(f);
	}

	@Override
	protected <T extends Serializable> LogObject<?> newObject(String uuid, T initial) throws MyriaIOException{
		MakeMerge<T> mm = new MakeMerge<>(initial);
		return new LogObject<>(uuid,TypeUtils.getClass(mm),mm);
	}

	@Override
	protected <T extends Serializable> LogObject<?> newObject(String uuid) throws MyriaIOException{
		MakeMerge<T> mm = new MakeMerge<>(null);
		return new LogObject<>(uuid,TypeUtils.getClass(mm),mm);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void insert(final LogObject<? extends Collection<? extends Serializable> > lo1, final LogObject<?> lo2){
		log.getLast().add(new Runnable(){
			public void run(){
				((LogObject)lo1).t = ((LogObject)lo2).t;
			}
			});
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void insert(final LogObject<? extends Collection<? extends Serializable> > lo1, final Serializable lo2){
		log.getLast().add(new Runnable(){
				public void run(){
					((LogObject)lo1).t = lo2;
				}
			});
	}

	@Override
	public InsertFactory<?,?,?> ifact() {return new InsertFactory<>(this); }

	@Override
	public LogStore this_replica(){
		return this;
	}

	@Override
	public LogStore access_replica(LogStore ls){
		return ls;
	}


}
