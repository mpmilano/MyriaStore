import operations.Get;
import operations.Put;
import handles.access.ReadWrite;
import consistency.Linearizable;
import demo.CustomOp;
import demo.LinearizableStore;
import remote.Handle;
import remote.ObjectManager;
import remote.ObjectManager_impl;


public class Main {
	public static void main(String[] args){
		ObjectManager om = new ObjectManager_impl();
		LinearizableStore ln = new LinearizableStore();
		
		Handle<Integer, ReadWrite, Linearizable/**/, Linearizable,LinearizableStore> o = 
				om.newObject(new Integer(3), ln);
		
		//o += 4;
		new Put<>(o,new Get<>(o).execute() + 4).execute();
		
		//print o
		System.out.println(new Get<>(o).execute());
		new CustomOp(o).execute();
	}

}
