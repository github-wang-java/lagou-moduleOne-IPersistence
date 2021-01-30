package com.lagou.dao;

import com.lagou.pojo.RpUser;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.util.List;

public interface IUserDao {

    public List<RpUser> findAll() throws Exception;

    public RpUser findByCondition(RpUser rpUser) throws Exception;

    public void update(RpUser rpUser) throws Exception;

    public void save(RpUser user) throws Exception;

    public void delete(RpUser user) throws Exception;

}
