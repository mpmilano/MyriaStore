package remote;

import handles.Handle;
import handles.access.ReadWrite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import consistency.BaseModel;

//note - I want the "there is no backing store related to this consistency model" error
//to occur at compile-time.  There's no way to do that in this context, so it looks like
//we'll have to have mutually-dependent constructors or something (for model and store).

public class ObjectManager {
	
	private Map<BaseModel,Set<BackingStore<?>> > bsl = new HashMap<BaseModel,Set<BackingStore<?>>>();
	
	public <Model extends BaseModel> boolean registerStore(BackingStore<Model> e){
		if (!bsl.containsKey(e.getModel())) bsl.put(e.getModel(),new TreeSet<BackingStore<?>>());
		bsl.get(e.getModel()).add(e);
		return true;
	}
	public <Model extends BaseModel> void deactivateStore(BackingStore<Model> e){
		bsl.remove(e.getModel());
	}
	
	public <Model extends BaseModel, T> Handle<T,ReadWrite,Model,Model> newObject(Model m, T t){
		
		for (BackingStore<?> tbs : bsl.get(m)){
			//unless I screw something up very badly, this type is correct.
			@SuppressWarnings("unchecked")
			BackingStore<Model> bs = (BackingStore<Model>) tbs;
			return new Handle<T,ReadWrite,Model,Model>(bs);
		}
		throw new RuntimeException("No matching Store found! This is clearly a bug!");
	}
}
