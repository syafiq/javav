    public void renderWhenEvaluateReturnsFalse() throws Exception
    {
        //  Mocks
        VelocityEngine engine = mock(VelocityEngine.class);
        when(velocityManager.getVelocityEngine()).thenReturn(engine);
        when(engine.evaluate(any(VelocityContext.class), any(Writer.class), any(),
            eq("myCode"))).thenReturn(false);

        // Test
        IconException caughtException = null;
        try {
            mocker.getComponentUnderTest().render("myCode");
        } catch(IconException e) {
            caughtException = e;
        }

        // Verify
        assertNotNull(caughtException);
        assertEquals("Failed to render the icon. See the Velocity runtime log.", caughtException.getMessage());

        verify(engine).startedUsingMacroNamespace("IconVelocityRenderer_" + Thread.currentThread().getId());
        verify(engine).stoppedUsingMacroNamespace("IconVelocityRenderer_" + Thread.currentThread().getId());
    }