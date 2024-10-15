		public boolean equals(Object other) {
			return (other != null &&
				this.getClass().equals(other.getClass()) &&
				this.object.equals(((BlobValue)other).object));
		}