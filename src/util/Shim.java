package util;

public interface Shim<T extends java.io.Serializable> extends java.io.Serializable
{
	public T t();
}
