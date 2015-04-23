package com.rick.base.test.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name="t_user")
public class User {
	
	@Id
	private Integer id;
	
	private Date birthday;
	
	@Column(name="USER_NAME",nullable=false,unique=true)
	private String name;
	
	@Column(columnDefinition="VARCHAR2(10 CHAR)")
	private String addr;

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
