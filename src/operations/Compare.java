package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public class Compare<T extends BotherSomeSyntax<T>,
M extends BaseModel, O extends BaseModel,
S extends BackingStore<O,S>> extends
BaseNativeOperation2<Integer,  T, T, M, O, S>
{


	public Compare (Handle<T,?,M,O,S> h1, 
			Handle<T,?,M,O,S> h2){
		super(h1,h2);
	}

	public Integer 
	executeOn(BackingStore<O,S>.RemoteObject<T> bs1, 
				BackingStore<O,S>.RemoteObject<T> bs2){
		return null;
	}

	public Integer noop(){
		return 0;
	}

}
