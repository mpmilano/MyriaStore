package fsstore;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import remote.*;
import util.*;
import operations.*;
import java.nio.file.*;
import java.net.*;

public class FSStore extends Store<consistency.Lin, FSStore.FSObject, String,InetAddress, FSStore>
	implements operations.List<FSStore.FSDir>,
			   operations.ForEach<consistency.Lin, FSStore.FSDir<?>, FSStore>,
			   operations.Insert<FSStore.FSDir<?>, FSStore.FSObject<?>>,
			   util.NameManager<String>
{

	//String manipulations!

	public String ofString(String s){ return s;}

	public String concat(String a, String b){return a + b;}

	public String toString(String s){return s;}
	
	
	private FSStore(){}

	public static FSStore inst = new FSStore();

	@Override
	protected String genArg(){
		try {
			return File.createTempFile("linable","store").getAbsolutePath();
		}
		catch( IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	protected boolean exists(String s){
		try{
			new FSObject<>(s, null);
			return true;
		}
		catch (MyriaIOException ex){
			return false;
		}
	}

	static class FSObject<T extends Serializable> implements RemoteObject<T, String> {
		protected final File location;
		protected final Class<?> storedclass;
		private FSObject(String location, T initialValue) throws MyriaIOException {
			Class<?> class1 = (initialValue == null ? Void.class : initialValue.getClass());
			this.storedclass = class1;
			this.location = new File(location);
			if (initialValue != null){
				FileOutputStream fos = null;
				ObjectOutputStream oos = null;
				try {
					this.location.createNewFile();
					fos = new FileOutputStream(this.location);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(initialValue);
				}
				catch (IOException ex){
					throw new MyriaIOException(ex);
				}
				finally {
					try{
						if (oos != null) oos.close();
						if (fos != null) fos.close();
						
					}
					catch (IOException e){
						throw new MyriaIOException(e);
					}

				}
			}
		}
		@Override
		public String name(){
			return location.getPath();
		}

		@Override
		public T get(){
			return inst.getObj(this);
		}
		@Override
		public void put(T o){
			inst.putObj(this,o);
		}

		@Override
		public FSStore getStore() {
			return inst;
		}
	}

	@Override
	protected <T extends Serializable> FSObject<T> newObject(String name, T initialValue) throws MyriaIOException{
		return new FSObject<T>(name, initialValue);
	}

	@Override
	protected <T extends Serializable> FSObject<T> newObject(String name) throws MyriaIOException{
		return new FSObject<T>(name, null);		
	}

	static class FSDir<T extends Serializable> extends FSObject<SerializableCollection<T>> {
		private FSObject<T>[] files;
		@SuppressWarnings("unchecked")
		private FSDir(String location) throws MyriaIOException {
			super(location,null);
			this.location.mkdirs();
			if (! this.location.isDirectory()) throw new MyriaIOException(new IOException("must be a dir!"));
			String[] fls = this.location.list();
			files = new FSObject[fls.length];
			for (int i = 0; i < files.length; ++i) files[i] = new FSObject<T>(location + "/" + fls[i],null);
			System.out.println("constructed!");
		}

		@Override
		public SerializableCollection<T> get(){
			return inst.getObj(this);
		}
		@Override
		public void put(SerializableCollection<T> o){
			inst.putObj(this,o);
		}

	}

	public class DirFact<T extends Serializable> extends AltObjFact<SerializableCollection<T>, access.ReadWrite, FSDir<T> > {
		public Handle<SerializableCollection<T>, consistency.Lin, access.ReadWrite, consistency.Lin, FSStore>
			newObject(String name) throws MyriaIOException{
			return buildHandle(new FSDir<T>(name));
		}
	}

	
	CopyOnWriteArrayList<util.Function<String,Void>> onRead = new CopyOnWriteArrayList<>();
	@Override
	public void registerOnRead(util.Function<String,Void> r){
		onRead.add(r);
	}

	private <T extends Serializable, C extends SerializableCollection<T> > C getObj(FSDir<T> o){
		throw new RuntimeException("whoops, unimplemented!");
	}
	
	private <T extends Serializable> T getObj(FSObject<T> o){
		try {
			for (util.Function<String,Void> f : onRead){
				f.apply(o.location.getCanonicalPath());
			}
			@SuppressWarnings("unchecked")
				T t = (T) (new ObjectInputStream(new FileInputStream(o.location))).readObject();
			return t;
		}
		catch (IOException e){
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	CopyOnWriteArrayList<util.Function<String,Void>> onWrite = new CopyOnWriteArrayList<>();
	@Override
	public void registerOnWrite(util.Function<String,Void> r){
		onWrite.add(r);
	}
	
	private <T extends Serializable> void putObj(FSObject<T> o, T t){
		try {
			(new ObjectOutputStream(new FileOutputStream(o.location))).writeObject(t);
			for (util.Function<String,Void> f : onWrite){
				f.apply(o.location.getCanonicalPath());
			}
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	//TODO: oponly
	public String[] list(FSDir fs){
		System.out.println("using FSStore native operation");
		return fs.location.list();
	}

	private class FSObjFact<T extends Serializable, A extends access.Unknown> extends AltObjFact<T, A, FSObject> {
		public Handle<T, consistency.Lin, A, consistency.Lin, FSStore> build(FSObject<T> fso){
			return buildHandle(fso);
		}
	}

	@Override
	//TODO: oponly
	public <Out, T extends Serializable, A extends access.Unknown>
		void foreach(OperationFactory<Out,T, consistency.Lin, Handle<T,consistency.Lin,A,consistency.Lin, FSStore> > of, FSDir<?> fs){
		System.out.println("native ForEach attempt");
		@SuppressWarnings("unchecked")
			FSDir<T> realfs= (FSDir<T>) fs;
		for (FSObject<T> f : realfs.files){
			Handle<T, consistency.Lin, A, consistency.Lin, FSStore> h = (new FSObjFact<T,A>()).build(f);
			System.out.println("loop...");
			of.build(h).execute();
		}
	}

	@Override
	//TODO: oponly
	public void insert(FSDir<?> set, FSObject<?> e) throws MyriaIOException{
		File nf = (new File(set.location.getAbsolutePath() + "/" + e.location.getName()));
		nf.delete();
		try{
			Files.copy(e.location.toPath(),nf.toPath());
		}
		catch (IOException ex){
			throw new MyriaIOException(ex);
		}

		//TODO: generally speaking, a native-exception which is checked statically should be created for all of these.
	}

	@Override
	//TODO: oponly
	public void insert(FSDir<?> set, Serializable e) throws MyriaIOException{
		insert(set, newObject(genArg(), e));
	}

	@Override
	public InsertFactory<?,?,?> ifact(){
		return new InsertFactory<>(this);
	}

	@Override
	public InetAddress this_replica(){
		try {
			return InetAddress.getLocalHost();
		}
		catch (UnknownHostException e){
			throw new RuntimeException(e);
		}
	}
}
