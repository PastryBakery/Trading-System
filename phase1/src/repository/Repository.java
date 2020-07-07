package repository;

import menu.data.Response;

import java.util.Iterator;

public interface Repository<T extends UniqueId> {

    void save();

    void add(T entity);

    T get(int id);

    Iterator<T> iterator(Filter<T> filter);

    Response filterResponse(Filter<T> filter, ResponseMapper<T> mapper);

}
