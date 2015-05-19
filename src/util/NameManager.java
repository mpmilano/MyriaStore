package util;

public interface NameManager<SType> {

	//only constraint here is consistent mapping.
	//incorporating the string directly would be nice
	//for debugging though, if it's possible to do.
	public abstract SType ofString(String s);

	public abstract SType concat(SType a, SType b);

	public abstract String toString(SType a);

}
