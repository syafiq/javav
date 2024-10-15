    public void initialize() throws InitializationException
    {
        super.initialize();

        this.observation.addListener(new EventListener()
        {
            @Override
            public void onEvent(Event event, Object source, Object data)
            {
                if (event instanceof TemplateEvent) {
                    TemplateEvent templateEvent = (TemplateEvent) event;

                    XWikiVelocityManager.this.velocityEngines.remove(templateEvent.getId());
                }
            }

            @Override
            public String getName()
            {
                return XWikiVelocityManager.class.getName();
            }

            @Override
            public List<Event> getEvents()
            {
                return EVENTS;
            }
        });
    }