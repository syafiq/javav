	public void receiveLoop() throws IOException
	{
		byte[] msg = new byte[35004];

		while (true)
		{
			int msglen = tc.receiveMessage(msg, 0, msg.length);

			int type = msg[0] & 0xff;

			if (type == Packets.SSH_MSG_IGNORE)
				continue;

			if (type == Packets.SSH_MSG_DEBUG)
			{
				if (log.isEnabled())
				{
					TypesReader tr = new TypesReader(msg, 0, msglen);
					tr.readByte();
					tr.readBoolean();
					StringBuffer debugMessageBuffer = new StringBuffer();
					debugMessageBuffer.append(tr.readString("UTF-8"));

					for (int i = 0; i < debugMessageBuffer.length(); i++)
					{
						char c = debugMessageBuffer.charAt(i);

						if ((c >= 32) && (c <= 126))
							continue;
						debugMessageBuffer.setCharAt(i, '\uFFFD');
					}

					log.log(50, "DEBUG Message from remote: '" + debugMessageBuffer.toString() + "'");
				}
				continue;
			}

			if (type == Packets.SSH_MSG_UNIMPLEMENTED)
			{
				throw new IOException("Peer sent UNIMPLEMENTED message, that should not happen.");
			}

			if (type == Packets.SSH_MSG_DISCONNECT)
			{
				TypesReader tr = new TypesReader(msg, 0, msglen);
				tr.readByte();
				int reason_code = tr.readUINT32();
				StringBuffer reasonBuffer = new StringBuffer();
				reasonBuffer.append(tr.readString("UTF-8"));

				/*
				 * Do not get fooled by servers that send abnormal long error
				 * messages
				 */

				if (reasonBuffer.length() > 255)
				{
					reasonBuffer.setLength(255);
					reasonBuffer.setCharAt(254, '.');
					reasonBuffer.setCharAt(253, '.');
					reasonBuffer.setCharAt(252, '.');
				}

				/*
				 * Also, check that the server did not send charcaters that may
				 * screw up the receiver -> restrict to reasonable US-ASCII
				 * subset -> "printable characters" (ASCII 32 - 126). Replace
				 * all others with 0xFFFD (UNICODE replacement character).
				 */

				for (int i = 0; i < reasonBuffer.length(); i++)
				{
					char c = reasonBuffer.charAt(i);

					if ((c >= 32) && (c <= 126))
						continue;
					reasonBuffer.setCharAt(i, '\uFFFD');
				}

				throw new IOException("Peer sent DISCONNECT message (reason code " + reason_code + "): "
						+ reasonBuffer.toString());
			}

			/*
			 * Is it a KEX Packet?
			 */

			if ((type == Packets.SSH_MSG_KEXINIT) || (type == Packets.SSH_MSG_NEWKEYS)
					|| ((type >= 30) && (type <= 49)))
			{
				km.handleMessage(msg, msglen);
				continue;
			}

			if (type == Packets.SSH_MSG_USERAUTH_SUCCESS) {
				tc.startCompression();
			}

			if (type == Packets.SSH_MSG_EXT_INFO) {
				// Update most-recently seen ext info (server can send this multiple times)
				extensionInfo = ExtensionInfo.fromPacketExtInfo(
						new PacketExtInfo(msg, 0, msglen));
				continue;
			}

			MessageHandler mh = null;

			for (int i = 0; i < messageHandlers.size(); i++)
			{
				HandlerEntry he = messageHandlers.elementAt(i);
				if ((he.low <= type) && (type <= he.high))
				{
					mh = he.mh;
					break;
				}
			}

			if (mh == null)
				throw new IOException("Unexpected SSH message (type " + type + ")");

			mh.handleMessage(msg, msglen);
		}
	}