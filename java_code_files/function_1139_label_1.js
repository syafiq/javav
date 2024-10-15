  private Message maybeAddToCache(Message message) {
    for (RRset set : message.getSectionRRsets(Section.ANSWER)) {
      if ((set.getType() == Type.CNAME || set.getType() == Type.DNAME) && set.size() != 1) {
        throw new InvalidZoneDataException("Multiple CNAME RRs not allowed, see RFC1034 3.6.2");
      }
    }
    Optional.ofNullable(caches.get(message.getQuestion().getDClass()))
        .ifPresent(cache -> cache.addMessage(message));
    return message;
  }