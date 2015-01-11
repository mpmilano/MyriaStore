package remote;

import handles.access.ReadWrite;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import consistency.BaseModel;

//note - I want the "there is no backing store related to this consistency model" error
//to occur at compile-time.  There's no way to do that in this context, so it looks like
//we'll have to have mutually-dependent constructors or something (for model and store).

public class ObjectManager_impl implements ObjectManager {
	
	//I want dependent types here, so that I can say the "?" will be
	//substituted with whatever more specific type BaseModel has.
	private Map<BaseModel,List<BackingStore<?>> > bsl = new HashMap<>();
	
	/* (non-Javadoc)
	 * @see remote.ObjectManager#registerStore(remote.BackingStore)
	 */
	@Override
	public <Model extends BaseModel> boolean registerStore(BackingStore<Model> e){
		if (!bsl.containsKey(e.getModel())) bsl.put(e.getModel(),new LinkedList<BackingStore<?>>());
		bsl.get(e.getModel()).add(e);
		return true;
	}
	/* (non-Javadoc)
	 * @see remote.ObjectManager#deactivateStore(remote.BackingStore)
	 */
	@Override
	public <Model extends BaseModel> void deactivateStore(BackingStore<Model> e){
		bsl.get(e.getModel()).remove(e);
	}
	
	/* (non-Javadoc)
	 * @see remote.ObjectManager#newObject(Model, T)
	 */
	@Override
	public <Model extends BaseModel, T> Handle<T,ReadWrite,Model,Model> newObject(Model m, T t){
		
		for (BackingStore<?> tbs : bsl.get(m)){
			//unless I screw something up very badly, this type is correct.
			@SuppressWarnings("unchecked")
			BackingStore<Model> bs = (BackingStore<Model>) tbs;
			return new Handle<T,ReadWrite,Model,Model>(m,bs,t);
		}
		throw new RuntimeException("No matching Store found! This is clearly a bug!");
	}
}
