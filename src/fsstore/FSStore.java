#include "FSStore.h"
package fsstore;
import remote.*;
import access.*;
import operations.*;
import java.io.*;
	
public class FSStore extends FSS_t implements Get<FSStore.FSObject>, Put<FSStore.FSObject>, Replace<FSStore.FSObject> {
	
	public class FSObject extends FSS_t.RemoteObject {

		private final File location;
		private final Class<?> storedclass;
		
		private FSObject(String location, Object initialValue) throws IOException {
			Class<?> class1 = initialValue.getClass();
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

	@Override
	protected FSObject newObj(String name, Object init) throws IOException{
		return new FSObject(name, init);
	}

	@Override
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

	@Override
	public <R_(T)> void putObj(FSObject o, T t){
		try {
			(new ObjectOutputStream(new FileOutputStream(o.location))).writeObject(t);
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void replace(FSObject dest, FSObject src) throws java.io.IOException{
		java.nio.file.Files.copy(src.location.toPath() , dest.location.toPath() );
	}
}
	
