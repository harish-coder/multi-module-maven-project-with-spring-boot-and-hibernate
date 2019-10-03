package com.crud.a4.dao;

import java.util.List;

import com.crud.a4.boot.entity.Employee;


public interface CrudDao{

	public List<Employee> findAll();
	
	public Employee findOne(Long id);
	
	public void save(Employee employee);
	
	public void deleteEmployee(Employee employee);
}
