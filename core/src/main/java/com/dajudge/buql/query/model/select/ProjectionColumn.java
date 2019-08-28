package com.dajudge.buql.query.model.select;

import java.util.function.Function;

import static com.dajudge.buql.util.StringUtils.quote;

public class ProjectionColumn {
    private final Function<ProjectionSources, String> inputColumn;
    private final String label;

    private ProjectionColumn(final Function<ProjectionSources, String> inputColumn, final String label) {
        this.inputColumn = inputColumn;
        this.label = label;
    }

    public String getSourceColumn(final ProjectionSources projectionSources) {
        return inputColumn.apply(projectionSources);
    }

    public String getLabel() {
        return quote(label);
    }

    public static ProjectionColumn fromFilters(final String filterColumn, final String label) {
        return new ProjectionColumn(sources -> sources.getFiltersColumn(filterColumn), label);
    }

    public static ProjectionColumn fromData(final String dataColumn, final String label) {
        return new ProjectionColumn(sources -> sources.getDataColumn(dataColumn), label);
    }

    public interface ProjectionSources {
        String getFiltersColumn(final String filterColumn);

        String getDataColumn(final String dataColumn);
    }
}
