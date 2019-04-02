package com.gorankitic.rest_api_crud.dao;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gorankitic.rest_api_crud.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {		//DAO - Data Access Object
	
	//need to inject the session factory - dependency injection
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Customer> getCustomers() {
		//pravim hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		//pravim pretragu/query i sortiram po prezimenu
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
		
		//izvrsavam query i dohvatam rezultate
		List<Customer> customers = theQuery.getResultList();
		
		//vracam rezultat metode
		return customers;
	}
	
	@Override
	public void saveCustomer(Customer theCustomer) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theCustomer); 			//ako korisnik ima id-update / ako nema-novi korisnik-save
	}

	@Override
	public Customer getCustomers(int id) {
		Session currentSession = sessionFactory.getCurrentSession();
		Customer theCustomer = currentSession.get(Customer.class, id);
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int id) {
		Session currentSession = sessionFactory.getCurrentSession();
		@SuppressWarnings("rawtypes")
		Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", id);
		theQuery.executeUpdate();
	}
}
