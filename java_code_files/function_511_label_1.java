		public int write(byte[] buffer, int offset) {
			System.arraycopy(bytes, 0, buffer, offset, bytes.length);
			return bytes.length;
		}