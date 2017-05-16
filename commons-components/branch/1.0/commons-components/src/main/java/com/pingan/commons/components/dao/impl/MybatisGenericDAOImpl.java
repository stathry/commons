package com.pingan.commons.components.dao.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.pingan.commons.components.dao.GenericDAO;
import com.pingan.commons.components.utils.ApplicationContextUtils;

/**
 * 
 * @author Demon
 * @date 2016-7-4
 */
@SuppressWarnings("all")
public class MybatisGenericDAOImpl<T, ID extends Serializable> extends SqlSessionDaoSupport  implements GenericDAO<T, ID>  {
	
	protected String namespace;
	protected GenericDAO mapper;
	
	@SuppressWarnings("unchecked")
	public MybatisGenericDAOImpl() {
		Class interfaceClass = this.getClass().getInterfaces()[0];
		this.namespace = interfaceClass.getName();
		this.setSqlSessionFactory(ApplicationContextUtils.getBean(SqlSessionFactory.class));
		mapper = (GenericDAO) getSqlSession().getMapper(interfaceClass);
	}
	
	@Override
	public String wrapSQLID(String sqlID) {
		return getNamespace() + "." + sqlID;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public int insert(T t) {
		return mapper.insert(t);
	}

	@Override
	public int deleteById(ID id) {
		return mapper.deleteById(id);
	}

	@Override
	public int update(T t) {
		return mapper.update(t);
	}

	@Override
	public int updateList(List<T> list) {
		return mapper.updateList(list);
	}

	@Override
	public T queryById(ID id) {
		return (T) mapper.queryById(id);
	}

	@Override
	public List<T> queryAll() {
		 List<T> list = mapper.queryAll();
		 if(list == null) {
			 list = Collections.emptyList();
		 }
		 return list;
	}

	@Override
	public List<T> queryByCond(T t) {
		return mapper.queryByCond(t);
	}

	@Override
	public int count() {
		return mapper.count();
	}

	@Override
	public int countByCond(T t) {
		return mapper.countByCond(t);
	}

	@Override
	public GenericDAO<T, ID> getMapper() {
		return mapper;
	}

	@Override
	public int updateStateById(T t) {
		return mapper.updateStateById(t);
	}


}
