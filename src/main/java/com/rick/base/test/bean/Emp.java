
package com.rick.base.test.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Emp {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer empno;
	
	private String ename;
	
	@Column(name="SAL")
	private float sal;
	
	@Column(name="HIREDATE")
	private Date hdate;

	public Date getHdate() {
		return hdate;
	}

	public void setHdate(Date hdate) {
		this.hdate = hdate;
	}

	public Integer getEmpno() {
		return empno;
	}

	public void setEmpno(Integer empno) {
		this.empno = empno;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public float getSal() {
		return sal;
	}

	public void setSal(float sal) {
		this.sal = sal;
	}
}
