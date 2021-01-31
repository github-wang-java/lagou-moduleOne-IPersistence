package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.RpUser;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream inputStream = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        List<RpUser> list = userDao.findAll();
        list.forEach(rpUser -> {
            System.out.println(rpUser.toString());
        });
        System.out.println("===================================");

        //update
        RpUser para = new RpUser();
        para.setId(1);
        para.setName("wahson");
        userDao.update(para);


        //insert
        RpUser para3 = new RpUser();
        para3.setName("xianyu");
        userDao.save(para3);

        //delete
        RpUser para4 = new RpUser();
        para4.setId(10);
        userDao.delete(para4);

        List<RpUser> list2 = userDao.findAll();
        list2.forEach(rpUser -> {
            System.out.println(rpUser.toString());
        });

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


    }
}
