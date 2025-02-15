package group.repository;

import group.menu.data.Response;

import java.util.Iterator;

/**
 * The interface to be used in use case methods.<p>
 * Contains only list related methods for the Repository.<p>
 * The interface does not contain any file / gateway related operations.
 *
 * @param <T> the entity type to be used
 * @author Dan Lyu
 */

public interface Repository<T extends UniqueId> {

    /**
     * @param entity the entity to be added
     */
    void add(T entity);

    /**
     * Gets the entity by unique id. List doesn't allow int size.
     * The int unique id decision was made as a convention earlier.
     * The implementation may change in the future to use Long.
     *
     * @param id the unique id of this entity
     * @return the entity
     */
    T get(int id);

    /**
     * @param entity the entity to be checked
     * @return <code>true</code> if this entity exists in this Repository
     */
    boolean ifExists(T entity);

    /**
     * @param filter the filter to be used to match results
     * @return <code>true</code> if the entity that matches the filter exists
     */
    boolean ifExists(Filter<T> filter);

    /**
     * The int unique id decision was made as a convention earlier.
     * The implementation may change in the future to use Long.
     *
     * @param id the unique id to be found
     * @return <code>true</code> if the entity with id exists
     */
    boolean ifExists(int id);

    /**
     * @param filter the filter to be used to match results
     * @return the first matched entity
     */
    T getFirst(Filter<T> filter);

    /**
     * @param entity the entity to be removed
     */
    void remove(T entity);

    /**
     * @param id the unique id of the entity to be removed
     */
    void remove(int id);

    /**
     * @param entity the entity who's unique id is unclear
     * @return the unique id
     */
    int getId(T entity);

    /**
     * @return the size of the Repository
     */
    int size();

    /**
     * @param filter the filter to be used to match results
     * @return the size of matched elements
     */
    int size(Filter<T> filter);

    /**
     * @param filter the filter to be used to match results
     * @return the iterator that iterates over matched entities
     */
    Iterator<T> iterator(Filter<T> filter);

    /**
     * @return the iterator that iterates every element in the Repository
     */
    Iterator<T> iterator();

    /**
     * @param filter the filter used to match the result
     * @param mapper A {@link ResponseMapper} used to directly map the records in iterator to a Response Object
     * @return the Response object
     */
    Response filterResponse(Filter<T> filter, ResponseMapper<T> mapper);

    /**
     * @param mapper the mapper used to map the iterator results to a Response object
     * @return the Response object
     */
    Response filterResponse(ResponseMapper<T> mapper);

}
