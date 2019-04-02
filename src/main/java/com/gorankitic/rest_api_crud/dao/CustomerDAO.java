package com.gorankitic.rest_api_crud.dao;

import java.util.List;

import com.gorankitic.rest_api_crud.entity.Customer;

public interface CustomerDAO {		//DAO - Data Access Object
	
	List<Customer> getCustomers();

	void saveCustomer(Customer theCustomer);

	Customer getCustomers(int id);

	void deleteCustomer(int id);

}
