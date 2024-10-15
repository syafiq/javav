    void applyCSSWhenExistingStyleDefinedUsingShorthandNotation()
    {
        // - Verify that element's style attributes are normalized and that the SPAN's color is set to red.
        // - Verify that the accent in the content is still there.
        //   TODO: right now we output the DOM with DOM4J and use the default of converting entities when using the
        //   XMLWriter. We need to decide if that's correct or if we should call XMLWriter#setResolveEntityRefs(false)
        //   instead.

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>\n"
            + "<title>\n"
            + "  Main.ttt - ttt\n"
            + "</title>\n"
            + "<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\"/>\n"
            + "<meta content=\"en\" name=\"language\"/>\n\n"
            + "</head><body class=\"exportbody\" id=\"body\" pdfcover=\"0\" pdftoc=\"0\">\n\n"
            + "<div id=\"xwikimaincontainer\">\n"
            + "<div id=\"xwikimaincontainerinner\">\n\n"
            + "<div id=\"xwikicontent\">\n"
            + "      <p><span style=\"color: #f00; background-color: #fff; background-image: none; "
            + "background-position: 0% 0%; background-size: auto auto; background-origin: padding-box; "
            + "background-clip: border-box; background-repeat: repeat repeat; "
            + "background-attachment: scroll; \">Hello Clément</span></p>\n"
            + "          </div>\n"
            + "</div>\n"
            + "</div>\n\n"
            + "</body></html>";

        assertEquals(expected, this.pdfExport.applyCSS(this.htmlContent, this.cssProperties, this.context));
    }