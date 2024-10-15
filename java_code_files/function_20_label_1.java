    public void renderWithException() throws Exception
    {
        //  Mocks
        Exception exception = new XWikiVelocityException("exception");
        when(velocityManager.getVelocityEngine()).thenThrow(exception);

        // Test
        IconException caughtException = null;
        try {
            mocker.getComponentUnderTest().render("myCode");
        } catch(IconException e) {
            caughtException = e;
        }

        // Verify
        assertNotNull(caughtException);
        assertEquals("Failed to render the icon.", caughtException.getMessage());
        assertEquals(exception, caughtException.getCause());
    }