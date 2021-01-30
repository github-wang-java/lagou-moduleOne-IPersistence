package com.lagou.sqlSession;

import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;
import com.lagou.utils.TokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;

public class SimpleExecutor implements Executor {

    /**
     *
     * @param configuration
     * @param mappedStatement
     * @param params 可变参，为数组类型
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1、注册驱动
        Connection connection = configuration.getDataSource().getConnection();
        //2.1、获取sql语句
        String sql = mappedStatement.getSql();
        //2.2、转换sql语句
        BoundSql boundSql = getBoundSql(sql);
        //3、获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getParseSql());
        //4、设置参数
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size() ; i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String parameterName = parameterMapping.getContent();

            //反射
            String parameterType = mappedStatement.getParameterType();
            Class<?> parameterClass = getClassType(parameterType);
            Field declaredField = parameterClass.getDeclaredField(parameterName);
            //暴力访问(防止该属性为私有属性)
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            //设置参数时下标从1开始
            preparedStatement.setObject(i+1,o);
        }

        //5、执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        String resultType = mappedStatement.getResultType();
        Class resultClass = getClassType(resultType);

        //6、封装返回值
        List<T> resultList = new ArrayList<>();
        while (resultSet.next()){
            Object o = resultClass.newInstance();
            //获取元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                //字段名
                String columnName = metaData.getColumnName(i);
                //字段值
                Object value = resultSet.getObject(columnName);
                //使用反射或内省，根据数据库表和实体的对应关系，完成封装。(此处使用的是内省)
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName,resultClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);

            }
            resultList.add((T) o);
        }

        return resultList;
    }

    private Class getClassType(String path) throws ClassNotFoundException {
        if (path!=null){
            return Class.forName(path);
        }
        return null;
    }

    /**
     * 完成对#{}的解析：1、蒋#{}用？代替；2、解析出#{}中的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql){
        //标记处理类：配合标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        //标记解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",tokenHandler);
        //进行解析后得到的sql
        String parseSql = genericTokenParser.parse(sql);
        //解析后获取到的sql中的参数名
        List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;
    }
}
