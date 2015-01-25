package demo;

import handles.access.ReadOnly;

import javax.management.Descriptor;

import operations.BaseNativeOperation1;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public class GetFieldValue<T extends Descriptor,
O extends BaseModel, M extends O,
S extends BackingStore<O,S>, A extends ReadOnly> extends
BaseNativeOperation1<String, T, O, S, M,A>

{
	
	final String arg;
	
	public GetFieldValue (Handle<T,A,M,O,S> h1, 
			final String arg){
		super(h1);
		this.arg = arg;
	}

	@Override
	public String 
	executeOn(BackingStore<O,S>.RemoteObject<T> bs1){
		return bs1.runOp(this);
	}

	public String noop(){
		return "";
	}

}
