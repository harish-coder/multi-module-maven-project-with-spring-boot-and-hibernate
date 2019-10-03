package com.crud.a4.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.a4.boot.entity.Employee;
import com.crud.a4.dao.CrudDao;

@Service
public class CrudServiceImpl implements CrudService{
	
	@Autowired
	private CrudDao crudDao;


	@Override
	public List<Employee> findAll() {
		return crudDao.findAll();
	}


	@Override
	public Employee findOne(Long id) {
		return crudDao.findOne(id);
	}


	@Override
	public void save(Employee employee) {
		crudDao.save(employee);
	}


	@Override
	public void deleteEmployee(Employee employee) {
		crudDao.deleteEmployee(employee);
	}

}
