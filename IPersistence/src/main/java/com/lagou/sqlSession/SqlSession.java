package com.lagou.sqlSession;

import java.util.List;

public interface SqlSession {

    public <E> List<E> selectList(String statementId, Object... params) throws Exception;

    public <T> T selectOne(String statementId, Object... params) throws Exception;

    //为DAO接口生成实现类
    public <T> T getMapper(Class<?> mapperClass);

}
