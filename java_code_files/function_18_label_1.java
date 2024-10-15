    public void renderTest() throws Exception
    {
        // Mocks
        VelocityEngine engine = mock(VelocityEngine.class);
        when(velocityManager.getVelocityEngine()).thenReturn(engine);
        when(engine.evaluate(any(VelocityContext.class), any(Writer.class), any(), eq("myCode"))).thenAnswer(
                new Answer<Object>()
                {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable
                    {
                        // Get the writer
                        Writer writer = (Writer) invocation.getArguments()[1];
                        writer.write("Rendered code");
                        return true;
                    }
                });

        // Test
        assertEquals("Rendered code", mocker.getComponentUnderTest().render("myCode"));

        // Verify
        verify(engine).startedUsingMacroNamespace("IconVelocityRenderer_" + Thread.currentThread().getId());
        verify(engine).stoppedUsingMacroNamespace("IconVelocityRenderer_" + Thread.currentThread().getId());
    }