package com.hp.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色实体类
 * 
 * @author baojulin
 *
 */
@Entity
@Table(name = "role", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Role extends BaseModel implements GrantedAuthority, ConfigAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -638143376103147253L;
	private String name;
	private String detail;
	private Set<Account> accounts = new HashSet<Account>(0);

	private String authority;
	private String attribute;

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DETAIL", nullable = false, length = 100)
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "account_role", schema = "", joinColumns = { @JoinColumn(name = "rid", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "aid", nullable = false, updatable = false) })
	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	/**
	 * 返回角色名称
	 */
	@Transient
	@Override
	public String getAuthority() {
		return name;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Transient
	@Override
	public String getAttribute() {
		return name;
	}

}
