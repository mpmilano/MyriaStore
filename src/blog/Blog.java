package blog;
import remote.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;
import operations.*;
import access.*;
import consistency.*;

class Comment implements Serializable {
	public Comment(int time, String comment){
		this.time = time;
		this.comment = comment;
	}
	public final int time;
	public final String comment;
}

public class Blog<S extends Store<Lin,?,?,?,SP> & Insert<?,?>, SP>{

	public Handle<ArrayList<BlogEntry>, Lin, ReadWrite, ? , ?> entries;

	public Blog(Handle<ArrayList<BlogEntry>, Lin, ReadWrite, ? , S> entries){
		this.entries = entries;
	}

	public BlogEntry postNewEntry(S s, String text){
		//entries.insert(text)
		BlogEntry bo = new BlogEntry(s,text);
		(s.ifact().build2(entries,bo)).execute();
		return bo;
	}

}
