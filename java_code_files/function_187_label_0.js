    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(getName()).append(this.content).toHashCode();
    }