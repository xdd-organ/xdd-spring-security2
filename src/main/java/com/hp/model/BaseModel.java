package com.hp.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
// 表示这个类的字段不是完整的，不会映射到数据库。
public abstract class BaseModel implements Serializable {
	
	private static final long serialVersionUID = -1107296259318239654L;
	// 定义主键
	private Integer id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "persistenceGenerator", strategy = "native")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
