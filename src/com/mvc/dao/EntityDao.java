package com.mvc.dao;

import java.util.List;

public interface EntityDao {
	public Object findById(Class entityClass, Integer id);
	public List<Object> createQuery(final String queryString);
	public List<Object> createQueryLimit(final String queryString, final Integer offset, final Integer limit);
	public Object save(final Object model);
	public void update(final Object model);
	public void delete(final Object model);
	public List<Object> findByHQLCondition(Class entityClass,List propertyNameList,List propertyValueList);
}
