        private boolean isUniqueLocalAddress(InetAddress address) {
            // ULA is actually defined as fc00::/7 (so both fc00::/8 and fd00::/8). However, only the latter is actually
            // defined right now, so let's be conservative.
            return address instanceof Inet6Address && (address.getAddress()[0] & 0xff) == 0xfd;
        }