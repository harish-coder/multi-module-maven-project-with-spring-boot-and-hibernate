package com.crud.a4.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crud.a4.boot.entity.Employee;

@Repository
@Transactional
public class CrudDaoImpl implements CrudDao{

	@Autowired
	private CrudRepositoryDao crudRepositoryDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> findAll() {
		String queryString = " select employee from " + Employee.class.getName() + " employee ";

		List<Employee> employees = sessionFactory.getCurrentSession().createQuery(queryString).list();

		return employees;
	}

	@Override
	public Employee findOne(Long id) {
		return crudRepositoryDao.findOne(id);
	}

	@Override
	public void save(Employee employee) {
		crudRepositoryDao.save(employee);
	}

	@Override
	public void deleteEmployee(Employee employee) {
		crudRepositoryDao.delete(employee);
	}

}
