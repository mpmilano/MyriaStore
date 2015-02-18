package blog;
import remote.*;
import java.io.Serializable;
import java.util.Collection;
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
	Handle<? extends Collection<Comment>, Causal, ReadWrite, ? ,?> comments = null;
	public BlogEntry(Store<Lin,?,?,?> s, String entry){
		this.text = s.newObject(entry);
	}

	public void addComment(int time, String str){
		new InsertOp<>(comments,new Comment(time, str));
	}
}

public class Blog{

	Handle<? extends Collection<BlogEntry>, Lin, ReadWrite, ? , ?> entries;

	void postNewEntry(Store<Lin,?,?,?> s, String text){
		(new InsertOp<?,?,?,?,?,?>(entries,new BlogEntry(s,text))).execute();
	}

}
