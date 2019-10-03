package com.crud.a4.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.crud.a4.boot.entity.Employee;

public interface CrudRepositoryDao extends CrudRepository<Employee, Long>{

	public List<Employee> findAll();
	
	public Employee findOne(Long id);
	
	public void delete(Employee employee); 
	
	
	
}
