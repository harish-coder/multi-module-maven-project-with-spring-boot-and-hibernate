package com.crud.a4.boot.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crud.a4.boot.entity.Employee;
import com.crud.a4.boot.service.CrudService;

import com.crud.a4.boot.common.Util;

@RestController
public class CrudController {

	@Autowired
	private CrudService crudService;

	private static final Log log = LogFactory.getLog(CrudController.class);
	
	/**
	 * @author Harish Arumugam
	 * 
	 * find All Employee
	 * 
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getAllEmployee/", method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> getAllEmployee() {
		log.info("/************************************************");
		List<Employee> employee = crudService.findAll();
		if(Util.isNullOrEmpty(employee)) {
			return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Employee>>(employee, HttpStatus.OK);
	}
	
	
	/**
	 * @author Harish Arumugam
	 * 
	 * @param employeeId
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getEmployeeById/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
		Employee employee = crudService.findOne(employeeId);
		if(employee==null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	/**
	 * @author Harish Arumugam
	 * 
	 * 
	 * @param employee
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/persistEmployee/", method = RequestMethod.POST)
	public ResponseEntity<Employee> persistEmployee(@RequestBody Employee employee) {
		if(employee==null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.NO_CONTENT);
		}
		crudService.save(employee);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	
	/**
	 * @author Harish Arumugam
	 * 
	 * 
	 * @param emp
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/updateEmployee/", method = RequestMethod.PUT)
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee emp) {
		Employee employee = crudService.findOne(emp.getEmployeeId());
		if(employee==null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.NO_CONTENT);
		}
		crudService.save(emp);
		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * @author Harish Arumugam
	 * 
	 * @param employeeId
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/deleteEmployee/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId) {
		Employee employee = crudService.findOne(employeeId);
		if(employee==null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.NO_CONTENT);
		}
		crudService.deleteEmployee(employee);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	
	
	
	@RequestMapping("/Hello")
	public String sayHello() {
		return "Hello World Test!!";
	}

}
