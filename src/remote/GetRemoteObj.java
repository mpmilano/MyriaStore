package remote;

public interface GetRemoteObj<T extends java.io.Serializable> {
	public RemoteObject<T> getRemoteObj();
}
