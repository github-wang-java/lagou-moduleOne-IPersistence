package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.RpUser;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream inputStream = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        //update
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//        RpUser para = new RpUser();
//        para.setId(1);
//        para.setName("wang");
//        userDao.update(para);
//
//        RpUser para2 = new RpUser();
//        para2.setId(1);
//        para2.setName("wang");
//        RpUser rpUser = userDao.findByCondition(para2);
//        System.out.println(rpUser.toString());

        //调用
        //insert
//        RpUser para3 = new RpUser();
//        para3.setName("xianyu");
//        userDao.save(para3);

        //调用
        //insert
        RpUser para4 = new RpUser();
        para4.setId(15);
        userDao.delete(para4);



//        RpUser o = sqlSession.selectOne("rpUserMapper.selectOne", user);
//        System.out.println(o);
//        System.out.println("=====================");
//
//        List<RpUser> list = sqlSession.selectList("rpUserMapper.selectList");
//        list.stream().forEach(rpUser->{
//            System.out.println(rpUser);
//        });

//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//        RpUser rpUser = userDao.findByCondition(user);
//        System.out.println(rpUser);

        List<RpUser> list = userDao.findAll();
        list.stream().forEach(e ->{
            System.out.println(e);
        });
    }
}
