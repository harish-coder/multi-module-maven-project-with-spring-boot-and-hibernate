package com.crud.a4.boot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "EMPLOYEE")
public class Employee implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_ID_SEQ")
	@SequenceGenerator(sequenceName = "employee_id_seq", allocationSize = 1, name = "EMPLOYEE_ID_SEQ")
	@Column(name = "EMPLOYEE_ID")
	private Long employeeId;

	@Column(name = "EMPLOYEE_FIRST_NAME")
	private String employeeFirstname;

	@Column(name = "EMPLOYEE_LAST_NAME")
	private String employeeLastName;

	@Column(name = "EMPLOYEE_ENTERPRISE_ID")
	private Long employeeEnterpriseId;

	@Column(name = "EMPLOYEE_ROLL")
	private String employeeRollNo;

	@Column(name = "EMPLOYEE_PHONE_NO")
	private Long employeePhoneNo;

	@Column(name = "EMPLOYEE_ADDRESS")
	private String employeeAddress;

	@Column(name = "EMPLOYEE_EMAIL_ID")
	private String employeeEmailId;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeFirstname() {
		return employeeFirstname;
	}

	public void setEmployeeFirstname(String employeeFirstname) {
		this.employeeFirstname = employeeFirstname;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public Long getEmployeeEnterpriseId() {
		return employeeEnterpriseId;
	}

	public void setEmployeeEnterpriseId(Long employeeEnterpriseId) {
		this.employeeEnterpriseId = employeeEnterpriseId;
	}

	public String getEmployeeRollNo() {
		return employeeRollNo;
	}

	public void setEmployeeRollNo(String employeeRollNo) {
		this.employeeRollNo = employeeRollNo;
	}

	public Long getEmployeePhoneNo() {
		return employeePhoneNo;
	}

	public void setEmployeePhoneNo(Long employeePhoneNo) {
		this.employeePhoneNo = employeePhoneNo;
	}

	public String getEmployeeAddress() {
		return employeeAddress;
	}

	public void setEmployeeAddress(String employeeAddress) {
		this.employeeAddress = employeeAddress;
	}

	public String getEmployeeEmailId() {
		return employeeEmailId;
	}

	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}

}
