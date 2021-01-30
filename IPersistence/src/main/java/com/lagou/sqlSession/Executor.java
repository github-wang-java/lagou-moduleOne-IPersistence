package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Executor {

    <T> List<T> query(Configuration configuration, MappedStatement MappedStatement, Object... params) throws SQLException, Exception;

}
