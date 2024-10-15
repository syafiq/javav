    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileOfficeDocumentArtifact that = (FileOfficeDocumentArtifact) o;

        return new EqualsBuilder().append(getName(), that.getName())
            .append(this.content, that.content).isEquals();
    }