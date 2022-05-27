package dev.toma.gunsrpg.client.render.infobar;

public abstract class DataSourcedElement<T> implements IDataElement {

    private final T source;

    public DataSourcedElement(T dataSource) {
        this.source = dataSource;
    }

    public T getDataSource() {
        return source;
    }
}
