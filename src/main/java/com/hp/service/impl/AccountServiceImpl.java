package com.hp.service.impl;

import com.hp.dao.AccountDao;
import com.hp.mapper.AccountMapper;
import com.hp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录操作，需要实现UserDetailsService接口
 * 
 * @author baojulin
 *
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService, UserDetailsService {
	//@Resource
	private AccountDao accountDao;

	@Autowired
	private AccountMapper accountMapper;

	/**
	 * 登录的时候，将用户信息存储到Account中
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//return accountDao.getJoinRole(username);
		return accountMapper.getJoinRole(username);
	}
}
