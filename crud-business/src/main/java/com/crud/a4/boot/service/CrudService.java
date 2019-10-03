package com.crud.a4.boot.service;

import java.util.List;

import com.crud.a4.boot.entity.Employee;

public interface CrudService {

	public List<Employee> findAll();
	
	public Employee findOne(Long id);
	
	public void save(Employee employee);
	
	public void deleteEmployee(Employee employee);
}
