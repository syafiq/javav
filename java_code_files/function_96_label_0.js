    public ViewPage viewVersion(String version)
    {
        this.pane.findElement(By.xpath(".//table//tr//td[position()=3]/a[contains(., '" + version + "')]")).click();

        return new ViewPage();
    }