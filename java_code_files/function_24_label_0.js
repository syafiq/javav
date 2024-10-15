    void renderWithException() throws Exception
    {
        //  Mocks
        Exception exception = new XWikiVelocityException("exception");
        when(this.velocityManager.getVelocityEngine()).thenThrow(exception);

        // Test
        IconException caughtException = null;
        try {
            this.velocityRenderer.render("myCode", null);
        } catch (IconException e) {
            caughtException = e;
        }

        // Verify
        assertNotNull(caughtException);
        assertEquals("Failed to render the icon.", caughtException.getMessage());
        assertEquals(exception, caughtException.getCause());
    }