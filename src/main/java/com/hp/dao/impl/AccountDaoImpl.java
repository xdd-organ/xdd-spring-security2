package com.hp.dao.impl;

import com.hp.dao.AccountDao;
import com.hp.model.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author baojulin
 *
 */
//@Component("accountDao")
public class AccountDaoImpl implements AccountDao {

//	@Resource
	private SessionFactory sessionFactory;

	/**
	 * 查询用户名和角色信息
	 */
	@Override
	public Account getJoinRole(String login) {
		Session session = sessionFactory.getCurrentSession();
		return (Account) session.createQuery("FROM Account a LEFT JOIN FETCH a.roles WHERE a.login=:login").setString("login", login).uniqueResult();
	}

	public static void main(String[] args) {
		// 运行的时候，需要将getCurrentSession改为openSession();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-*.xml");
		System.out.println(context);
		AccountDao accountDao = (AccountDao) context.getBean("accountDao");
		System.out.println(accountDao);
		Account account = accountDao.getJoinRole("admin");
		System.out.println(account.getRoles().size());
	}
}
