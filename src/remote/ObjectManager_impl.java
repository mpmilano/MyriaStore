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
	private Map<BaseModel,List<BackingStore<?,?>> > bsl = new HashMap<>();


	@Override
	public <Model extends BaseModel, S extends BackingStore<Model, S>> boolean registerStore(
			S e) {
		if (!bsl.containsKey(e.getModel())) bsl.put(e.getModel(),new LinkedList<BackingStore<?,?>>());
		bsl.get(e.getModel()).add(e);
		return true;

	}
	@Override
	public <Model extends BaseModel, S extends BackingStore<Model, S>> void deactivateStore(
			S e) {
		bsl.get(e.getModel()).remove(e);		
	}

	@Override
	public <Model extends BaseModel, T, Location extends BackingStore<Model, Location>> Handle<T, ReadWrite, Model, Model, Location> newObject(
			Model m, T t, Location bs) {

		//TODO: this is definitely wrong.
		return new Handle<T,ReadWrite,Model,Model,Location>(m,bs,t);
	}
}
