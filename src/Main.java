#include "fsstore/FSStore.h"
import fsstore.*;
import remote.*;
import operations.*;

public class Main {
	public static void main(String[] args) throws java.io.IOException{
		FSStore<String> fs = new FSStore<>();
		Handle<String, FSS_(String), FSStore<String>.FSObject<String>, access.ReadWrite, consistency.Lin> h =
			new Handle<>(fs.new FSObject<>("/tmp/tmpobj","/tmp/tmpobj"));
		(new GetOp<>(h)).execute();
		
	}
}
