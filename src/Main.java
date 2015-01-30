#include "fsstore/FSStore.h"
import fsstore.*;
import remote.*;
import operations.*;

public class Main {
	public static void main(String[] args) throws java.io.IOException{
		FSStore fs = new FSStore();
		Handle<String, FSS_, FSStore.FSObject, access.ReadWrite, consistency.Lin> h =
			new Handle<>(String.class, fs.new FSObject("/tmp/tmpobj","/tmp/tmpobj"));
		(new GetOp<>(h)).execute();
		
	}
}
