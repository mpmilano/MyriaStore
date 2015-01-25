
import java.util.concurrent.CopyOnWriteArraySet;

import javax.management.ImmutableDescriptor;

import operations.*;
import handles.access.ReadWrite;
import consistency.Linearizable;
import demo.CustomOp;
import demo.LinearizableStore;
import demo.GetFieldValue;
import remote.Handle;


public class Main {
	public static void main(String[] args){
		LinearizableStore ln = new LinearizableStore();
		
		ImmutableDescriptor tmp = new ImmutableDescriptor(new String("a=3"));
		
		Handle<ImmutableDescriptor, ReadWrite, Linearizable/**/, Linearizable,LinearizableStore> o = 
				ln.newObject(tmp);
		
		//o += 4;
		new Put<>(o,new ImmutableDescriptor(new Get<>(o).execute().getFieldNames()[0] + "4=3")).execute();
		
		//print o
		System.out.println(new Get<>(o).execute());
		new CustomOp<>(o).execute();
		System.out.println(new GetFieldValue<>(o, "a4").execute());
		System.out.println(new Compare<>(ln.newCObject("a"), ln.newCObject("b")).execute());
		
		Handle<CopyOnWriteArraySet<String>,ReadWrite,Linearizable,Linearizable,LinearizableStore> h2 = 
				ln.newDumbObject(new CopyOnWriteArraySet<String>());
		
		Handle<String,ReadWrite,Linearizable,Linearizable,LinearizableStore> s2 = 
				ln.newDumbObject(new String("foo!"));		
		new Insert<>(h2, s2).execute();
		new Print<>(s2).execute();
		
	}
	

}
