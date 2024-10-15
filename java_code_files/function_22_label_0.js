    void renderTest() throws Exception
    {
        // Mocks
        VelocityEngine engine = mock(VelocityEngine.class);
        when(this.velocityManager.getVelocityEngine()).thenReturn(engine);
        when(engine.evaluate(any(VelocityContext.class), any(Writer.class), any(), eq("myCode"))).thenAnswer(
            invocation -> {
                // Get the writer
                Writer writer = (Writer) invocation.getArguments()[1];
                writer.write("Rendered code");
                return true;
            });

        // Test
        assertEquals("Rendered code", this.velocityRenderer.render("myCode", null));

        // Verify
        verify(engine).startedUsingMacroNamespace("IconVelocityRenderer_" + Thread.currentThread().getId());
        verify(engine).stoppedUsingMacroNamespace("IconVelocityRenderer_" + Thread.currentThread().getId());
    }