package com.mvc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EntityDaoImpl extends HibernateDaoSupport implements EntityDao{
	
	public Object findById(Class entityClass, Integer id) {
		   return this.getHibernateTemplate().get(entityClass,id);
		  }
	
	public List<Object> createQuery(final String queryString) {
		return (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Object doInHibernate(org.hibernate.Session session)
							throws org.hibernate.HibernateException {
						Query query = session.createQuery(queryString);
						List<Object> rows = query.list();
						return rows;
					}
				});
	}
	
	public List<Object> createQueryLimit(final String queryString, final Integer offset, final Integer limit) {

		return (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Object doInHibernate(org.hibernate.Session session)
							throws org.hibernate.HibernateException {
						Query query = session.createQuery(queryString);		
						query.setFirstResult(offset);
						query.setMaxResults(limit);
						List<Object> rows = query.list();
						return rows;
					}
				});
	}
	
	
	public Object save(final Object model) {
		return  getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Object doInHibernate(org.hibernate.Session session)
							throws org.hibernate.HibernateException {
						session.save(model);
						return null;
					}
				});
	}
	
	public void update(final Object model) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(org.hibernate.Session session)
					throws org.hibernate.HibernateException {
				session.update(model);
				return null;
			}
		});
	}
	public void delete(final Object model) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(org.hibernate.Session session)
					throws org.hibernate.HibernateException {
				session.delete(model);
				return null;
			}
		});
	}
	
	 public List<Object> findByHQLCondition(Class entityClass,List propertyNameList,List propertyValueList){
		   StringBuffer sqlStr = new StringBuffer();
		   sqlStr.append("from ").append(entityClass.getName()).append(" as bo");
		   sqlStr.append(" where 1=1 ");
		   if(propertyNameList !=null && propertyValueList!=null && propertyNameList.size() == propertyValueList.size()){
		    for(int i=0;i<propertyNameList.size();i++){
		     sqlStr.append(" and bo.").append(propertyNameList.get(i)).append(" = :").append(propertyNameList.get(i));
		    }
		   }else{
		    return null;
		   }
		   final String sql = sqlStr.toString();
		   final List propertyNList = propertyNameList;
		   final List propertyVList = propertyValueList;
		   return(List) getHibernateTemplate().execute(new HibernateCallback() {
		   public Object doInHibernate(Session session)
		          throws HibernateException, SQLException {
		     List li = new ArrayList();
		     Query query = session.createQuery(sql);
		     for(int i=0;i<propertyNList.size();i++){
		      query.setParameter(propertyNList.get(i).toString(),propertyVList.get(i));
		     }	     
		     li = query.list();
		     return li;
		    }
		   });
		  }
	
}
