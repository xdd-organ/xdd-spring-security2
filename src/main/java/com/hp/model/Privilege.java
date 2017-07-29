package com.hp.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

/**
 * 权限实体类
 * 
 * @author baojulin
 *
 */
@Entity
@Table(name = "privilege", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Privilege extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1574165994680444408L;
	private String name;

	private Set<Role> roles;

	/** default constructor */
	public Privilege() {
	}

	@ManyToMany
	@JoinTable(name = "privilege_role", schema = "", joinColumns = { @JoinColumn(name = "pid", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "rid", nullable = false, updatable = false) })
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/** full constructor */
	public Privilege(String name) {
		this.name = name;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
