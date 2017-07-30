package com.hp.dao.impl;

import com.hp.dao.UrlDao;
import com.hp.model.Url;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * @author baojulin
 *
 */
//@Component("urlDao")
public class UrlDaoImpl implements UrlDao {
//	@Resource
	private SessionFactory sessionFactory;

	/**
	 * 通过URL地址获取相应权限然后在获取相应的角色集合
	 */
	@Override
	public Url getRoleByUrl(String url) {
		String hql = "FROM Url u JOIN FETCH u.privilege up JOIN FETCH up.roles WHERE u.url=:url";
		Session session = sessionFactory.openSession();
		return (Url) session.createQuery(hql).setString("url", url).uniqueResult();
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml", "spring-*.xml");
		UrlDaoImpl urlDao = (UrlDaoImpl) context.getBean("urlDao");
		Url url = urlDao.getRoleByUrl("/user/save.jsp");
		System.out.println(url.getUrl());
		System.out.println(url.getPrivilege().getName());
		System.out.println(url.getPrivilege().getRoles());
	}

}
