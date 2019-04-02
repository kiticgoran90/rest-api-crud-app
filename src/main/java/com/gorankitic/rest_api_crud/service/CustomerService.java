package com.gorankitic.rest_api_crud.service;

import java.util.List;

import com.gorankitic.rest_api_crud.entity.Customer;

public interface CustomerService {
	
	List<Customer> getCustomers();

	void saveCustomer(Customer theCustomer);

	Customer getCustomer(int id);

	void deleteCustomer(int id);

}
