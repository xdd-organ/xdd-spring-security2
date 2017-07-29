package com.hp.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * url实体类
 * 
 * @author baojulin
 *
 */
@Entity
@Table(name = "url", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Url extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7126290374189794535L;
	// Fields
	private String url;
	private Privilege privilege;

	public Url() {
	}

	@ManyToOne
	@JoinColumn(name = "pid", referencedColumnName = "id")
	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	@Column(name = "URL", nullable = false, length = 100)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
