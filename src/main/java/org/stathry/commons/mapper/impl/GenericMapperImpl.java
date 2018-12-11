/*
 * Copyright https://github.com/stathry/generator All rights reserved.
 */
package org.stathry.commons.mapper.impl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.stathry.commons.mapper.GenericMapper;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 通用数据访问实现
 *
 * @author Auto-generated by FreeMarker
 * @date 2018-07-19 17:46
 */

public class GenericMapperImpl<T, ID extends Serializable> extends SqlSessionDaoSupport implements GenericMapper<T, ID> {

    protected String namespace;
    protected GenericMapper<T, ID> mapper;
    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public GenericMapperImpl() {
        Class<?> interfaceClass = this.getClass().getInterfaces()[0];
        this.namespace = interfaceClass.getName();
        this.setSqlSessionFactory(ApplicationContextUtils.getBean("sqlSessionFactory", SqlSessionFactory.class));
        mapper = (GenericMapper<T, ID>) getSqlSession().getMapper(interfaceClass);
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
    public int insert(T t) {
        return mapper.insert(t);
    }

//        SqlSession session = ((SqlSessionTemplate) getSqlSession()).getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    @Override
    public int insertAll(List<T> list) {
        return mapper.insertAll(list);
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
    public int updateByMap(Map<String, Object> params) {
        return mapper.updateByMap(params);
    }

    @Override
    public int updateStateById(T t) {
        return mapper.updateStateById(t);
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
        return list == null ? Collections.emptyList() : list;
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
    public <T1> T1 getMapper(Class<T1> clazz) {
        return getSqlSession().getMapper(clazz);
    }

}
