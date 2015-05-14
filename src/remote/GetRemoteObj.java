package remote;

public interface GetRemoteObj<T extends java.io.Serializable, K> {
	public RemoteObject<T, K> getRemoteObj();
}
