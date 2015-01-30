#include "FSStore.h"
package fsstore;
import remote.*;
import access.*;
import operations.*;
import java.io.*;
	
public class FSStore extends FSS_t

	//need to enumerate for all types supported.... templates-time!
	implements Get<FSStore.FSObject>

{
	
	public class FSObject extends FSS_t.RemoteObject {

		private final File location;
		private final Class<?> storedclass;
		
		public FSObject(String location, Object initialValue) throws IOException {
			@SuppressWarnings("unchecked")
				Class<?> class1 = (Class<?>) initialValue.getClass();
			this.storedclass = class1;
			this.location = new File(location);
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

		@Override
		public FSStore getStore(){
			return FSStore.this;
		}

		@Override
		public Class<?> getUnderlyingClass(){
			return storedclass;
		}

		
	}

	/*
	@Override
	public <R_(T)> Handle<T, FSS_, FSObject<T>, ReadWrite, consistency.Lin> newObject(R_g(T) init, String name){
		try {
			return new Handle<>(new FSObject<R_g(T)>(name, init));
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}
	*/
	

	@Override
	@SuppressWarnings("unchecked")
	public Object getObj(FSObject o){
		try {
			return (new ObjectInputStream(new FileInputStream(o.location))).readObject();
		}
		catch (IOException e){
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
	
