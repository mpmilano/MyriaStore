	private final CausalType tombstone_word;
	private CausalType tombstone_name(String s){
		return cnm.concat(tombstone_word,
								 cnm.ofString(s));
	}
	
private class Tombstone implements Serializable {
		public final Nonce n;
		public final CausalType name;

		public Tombstone(Nonce n){
			this.n = n;
			this.name = tombstone_name(n);
		}

	}
