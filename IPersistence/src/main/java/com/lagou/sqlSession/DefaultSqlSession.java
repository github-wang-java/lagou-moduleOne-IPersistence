package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.beans.Expression;
import java.lang.reflect.*;
import java.util.List;
import java.util.concurrent.Executor;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {

        //完成对simpleExecutor中的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        List<Object> objects = simpleExecutor.query(configuration, configuration.getMappedStatementMap().get(statementId), params);

        return (List<E>) objects;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = this.selectList(statementId, params);
        if (objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("result is null or select one but found more");
        }
    }

    @Override
    public void insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        simpleExecutor.query(configuration,configuration.getMappedStatementMap().get(statementId),params);
    }

    @Override
    public void delete(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        simpleExecutor.query(configuration,configuration.getMappedStatementMap().get(statementId),params);
    }

    @Override
    public void update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        simpleExecutor.query(configuration,configuration.getMappedStatementMap().get(statementId),params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理为DAO生成代理对象
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            /**
             * @param proxy 当前代理对象的引用
             * @param method 当前被调用方法的引用
             * @param args 当前被调用方法的参数
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                //获取参数1：statementId：namespace.id
                String name = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className + "." + name;

                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                //获取参数2：params：args
                Object obj = new Object();
                switch (mappedStatement.getSqlType()){
                    case "select":
                        obj = invokeSelect(statementId,method,args);
                        break;
                    case "update":
                        obj = invokeUpdate(statementId,args);
                        break;
                    case "insert":
                        obj = invokeInsert(statementId,args);
                        break;
                    case "delete":
                        obj = invokeDelete(statementId,args);
                        break;
                    default:
                        throw new RuntimeException("unknow sql type");
                }
                return obj;
            }
        });
        return (T) proxyInstance;
    }

    private Object invokeSelect(String statementId,Method method,Object[] args) throws Exception {
        //获取被调用方法的返回值
        Type returnType = method.getGenericReturnType();
        //判断该方法的返回值类型是否进行了 泛型类型参数化（即返回值是否包含泛型，如：List<T>）
        if (returnType instanceof ParameterizedType){
            return selectList(statementId,args);
        }
        return selectOne(statementId,args);
    }

    private Object invokeUpdate(String statementId,Object[] args) throws Exception {
        update(statementId, args);
        return null;
    }

    private Object invokeInsert(String statementId,Object[] args) throws Exception {
        insert(statementId, args);
        return null;
    }

    private Object invokeDelete(String statementId,Object[] args) throws Exception {
        delete(statementId, args);
        return null;
    }
}
