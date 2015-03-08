package transactions;

import remote.*;
import operations.*;

public class Seq implements Transaction{

	Transaction first;
	Transaction second;
	
	public Seq(Transaction first, Transaction second){
		this.first = first;
		this.second = second;
	}

	@Override
	void run(){
		first.ro.getStore().beginTransaction();
		second.ro.getStore().beginTransaction();
		first.execute();
		second.execute();
		second.ro.getStore().endTransaction();
		first.ro.getStore().endTransaction();
	}

}
