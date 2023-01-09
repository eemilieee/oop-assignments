package input;

public final class FilterInput {
    private SortInput sort;
    private ContainsInput contains;

    public FilterInput() {
    }

    public SortInput getSort() {
        return sort;
    }

    public void setSort(final SortInput sort) {
        this.sort = sort;
    }

    public ContainsInput getContains() {
        return contains;
    }

    public void setContains(final ContainsInput contains) {
        this.contains = contains;
    }

    @Override
    public String toString() {
        return "Filter{"
                + "filters: " + this.sort + "\n"
                + "contains: " + this.contains + "}" + "\n";
    }
}
