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

class BlogEntry implements Serializable{
	Handle<String,Lin, ReadWrite, ?, ?> text;
	Handle<ArrayList<Comment>, Causal, ReadWrite, ? ,?> comments = null;
	public BlogEntry(Store<Lin,?,?,?> s, String entry){
		this.text = s.newObject(entry);
	}

	public void addComment(int time, String str){

	}
}

public class Blog{

	public Handle<ArrayList<BlogEntry>, Lin, ReadWrite, ? , ?> entries;

	public Blog(Handle<ArrayList<BlogEntry>, Lin, ReadWrite, ? , ?> entries){
		this.entries = entries;
	}

	void postNewEntry(Store<Lin,?,?,?> s, InsertFactory<?,?,?> ifact, String text){
		//entries.insert(text)
		(ifact.build2(entries,new BlogEntry(s,text))).execute();
	}

}
