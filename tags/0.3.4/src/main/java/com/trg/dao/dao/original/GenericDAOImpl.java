package com.trg.dao.dao.original;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;

import com.trg.dao.hibernate.BaseDAOImpl;
import com.trg.dao.search.Search;
import com.trg.dao.search.SearchResult;

/**
 * Implementation of <code>GeneralDAO</code> using SpringHibernateSupport and
 * HQL for searches.
 * 
 * @author dwolverton
 * 
 * @param <T>
 *            The type of the domain object for which this instance is to be
 *            used.
 * @param <ID>
 *            The type of the id of the domain object for which this instance is
 *            to be used.
 */
@SuppressWarnings("unchecked")
public class GenericDAOImpl<T, ID extends Serializable> extends
		BaseDAOImpl implements GenericDAO<T, ID> {

	@Override
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	protected Class<T> persistentClass = (Class<T>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[0];

	public void create(T object) {
		if (!persistentClass.isInstance(object))
			throw new IllegalArgumentException(
					"Object class does not match dao type.");
		_save(object);
	}

	public boolean createOrUpdate(T object) {
		Serializable id = getMetaDataUtil().getId(object);
		if (id == null || (new Long(0)).equals(id)) {
			create(object);
			return true;
		} else {
			update(object);
			return false;
		}
	}

	public boolean deleteById(ID id) {
		return _deleteById(persistentClass, id);
	}

	public boolean deleteEntity(T object) {
		if (!persistentClass.isInstance(object))
			throw new IllegalArgumentException(
					"Object class does not match dao type.");
		return _deleteEntity(object);
	}

	public T fetch(ID id) {
		return (T) _get(persistentClass, id);
	}

	public List<T> fetchAll() {
		return _all(persistentClass);
	}

	public void update(T object) {
		if (!persistentClass.isInstance(object))
			throw new IllegalArgumentException(
					"Object class does not match dao type.");
		_update(object);
	}

	public List<T> search(Search search) {
		if (search == null)
			return fetchAll();
		
		return _search(search, persistentClass);
	}

	public int count(Search search) {
		if (search == null)
			return 0;
		return _count(search, persistentClass);
	}

	public SearchResult<T> searchAndCount(Search search) {
		if (search == null) {
			SearchResult<T> result = new SearchResult<T>();
			result.results = fetchAll();
			result.totalLength = result.results.size();
			return result;
		}
		
		return _searchAndCount(search, persistentClass);
	}

	public boolean isConnected(Object object) {
		return _isAttached(object);
	}

	public void flush() {
		_flush();
	}

	public void refresh(Object object) {
		_refresh(object);
	}

	public List searchGeneric(Search search) {
		if (search == null)
			return fetchAll();
		
		return _search(search, persistentClass);
	}

	public Object searchUnique(Search search) throws NonUniqueResultException {
		if (search == null)
			return null;
		
		return _searchUnique(search, persistentClass);
	}

}