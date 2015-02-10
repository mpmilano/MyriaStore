package fsstore;

import java.io.*;
import java.util.*;
import remote.*;
import util.*;

public class FSStore extends Store<consistency.Lin, FSStore.FSObject, String> implements operations.List<FSStore.FSDir> {


	public class FSObject<T extends Serializable> implements RemoteObject<T> {
		protected final File location;
		protected final Class<?> storedclass;
		private FSObject(String location, T initialValue) throws IOException {
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
				finally {
					if (oos != null) oos.close();
					if (fos != null) fos.close();
				}
			}
		}

		@Override
		public T get(){
			return getObj(this);
		}
		@Override
		public void put(T o){
			putObj(this,o);
		}

		@Override
		public FSStore getStore() {
			return FSStore.this;
		}
	}

	@Override
	protected <T extends Serializable> FSObject<T> newObject(String name, T initialValue) throws IOException{
		return new FSObject<T>(name, initialValue);
	}

	public class FSDir<T extends Serializable> extends FSObject<SerializableCollection<T>> {
		private FSObject[] files;
		private FSDir(String location) throws IOException {
			super(location,null);
			if (! this.location.isDirectory()) throw new IOException("must be a dir!");
			String[] fls = this.location.list();
			files = new FSObject[fls.length];
			for (int i = 0; i < files.length; ++i) files[i] = new FSObject<T>(fls[i],null);
		}
	}

	public class DirFact<T extends Serializable> extends AltObjFact<SerializableCollection<T>, FSDir<T> > {
		public Handle<SerializableCollection<T>, consistency.Lin, access.ReadWrite, consistency.Lin>
			newObject(String name) throws IOException{
			return buildHandle(new FSDir<T>(name));
		}
	}

	private <T extends Serializable> T getObj(FSObject<T> o){
		try {
			@SuppressWarnings("unchecked")
			T t = (T) (new ObjectInputStream(new FileInputStream(o.location))).readObject();
			return t;
		}
		catch (IOException e){
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	private <T extends Serializable> void putObj(FSObject<T> o, T t){
		try {
			(new ObjectOutputStream(new FileOutputStream(o.location))).writeObject(t);
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	//TODO: oponly
	public String[] list(FSDir fs){
		return fs.location.list();
	}
	
}
