    void onEvent(String wiki, Callable<Event> eventCallable, Object source, String newEventSource) throws Exception
    {
        Event event = eventCallable.call();

        // Unregister the listeners of the configuration sources themselves to check that cache invalidation is
        // really triggered independently.
        this.observationManager.removeListener(CURRENT_WIKI_CACHE_ID);
        this.observationManager.removeListener(MAIN_WIKI_CACHE_ID);
        // Register the listener explicitly as it seems that it isn't registered automatically.
        this.observationManager.addListener(this.eventGenerator);

        EventListener allWikiListener = mock();
        when(allWikiListener.getEvents()).thenReturn(List.of(new GeneralMailConfigurationUpdatedEvent()));
        when(allWikiListener.getName()).thenReturn("allWiki");

        EventListener currentWikiListener = mock();
        when(currentWikiListener.getEvents()).thenReturn(List.of(new GeneralMailConfigurationUpdatedEvent(wiki)));
        when(currentWikiListener.getName()).thenReturn("currentWiki");

        EventListener wikiListener = mock();
        when(wikiListener.getEvents()).thenReturn(List.of(new GeneralMailConfigurationUpdatedEvent("wiki")));
        when(wikiListener.getName()).thenReturn("wiki");

        this.observationManager.addListener(allWikiListener);
        this.observationManager.addListener(currentWikiListener);
        this.observationManager.addListener(wikiListener);

        this.observationManager.notify(event, source);

        verify(this.currentWikiCache).removeAll();
        if (MAIN_WIKI.equals(wiki)) {
            verify(this.mainWikiCache).removeAll();
        } else {
            verify(this.mainWikiCache, never()).removeAll();
        }

        if (List.of(MAIN_WIKI, "wiki").contains(wiki)) {
            verify(wikiListener).onEvent(any(), eq(newEventSource), eq(null));
        } else {
            verify(wikiListener, never()).onEvent(any(), any(), any());
        }

        verify(allWikiListener).onEvent(any(), eq(newEventSource), eq(null));
        verify(currentWikiListener).onEvent(any(), eq(newEventSource), eq(null));
    }