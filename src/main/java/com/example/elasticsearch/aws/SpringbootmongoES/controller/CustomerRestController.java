package com.example.elasticsearch.aws.SpringbootmongoES.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.elasticsearch.aws.SpringbootmongoES.model.Customer;
import com.example.elasticsearch.aws.SpringbootmongoES.model.CustomerES;
import com.example.elasticsearch.aws.SpringbootmongoES.repository.CustomerESRepository;
import com.example.elasticsearch.aws.SpringbootmongoES.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRestController.class);
	
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private CustomerESRepository repositoryES;
	
	@RequestMapping("customer/")
	public List<Customer> findAll(){
		final List<Customer> customers = repository.findAll();
		LOGGER.info("Fetching customers from database {}" , customers);
		return customers;
	}
	
	@RequestMapping("es/customer/")
	public List<CustomerES> findESAll(){
		
		Iterable<CustomerES> source = repositoryES.findAll();
		List<CustomerES> target = new ArrayList<>();
		source.forEach(target::add);
		LOGGER.info("Fetching customers from database ES {}" , target);
		return target;
	}
	
	@RequestMapping("es/customer/first/{firstName}")
	public CustomerES findByFirstNameES(@PathVariable("firstName") String firstName){
		final CustomerES customer = repositoryES.findByFirstName(firstName);
		LOGGER.info("Fetching customers from database {}" , customer);
		return customer;
	}
	
	@RequestMapping("customer/first/{firstName}")
	public Customer findByFirstName(@PathVariable("firstName") String firstName){
		final Customer customer = repository.findByFirstName(firstName);
		LOGGER.info("Fetching customers from database {}" , customer);
		return customer;
	}
	
	@RequestMapping("customer/last/{lastName}")
	public List<Customer> findByLastName(@PathVariable("lastName") String lastName){
		final List<Customer> customer = repository.findByLastName(lastName);
		LOGGER.info("Fetching customers from database {}" , customer);
		return customer;
	}
	
	@RequestMapping("es/customer/last/{lastName}")
	public List<CustomerES> findByLastNameES(@PathVariable("lastName") String lastName){
		final List<CustomerES> customer = repositoryES.findByLastName(lastName);
		LOGGER.info("Fetching customers from database {}" , customer);
		return customer;
	}
	
	@RequestMapping("customer/cn/{country}")
	public List<Customer> findByCountry(@PathVariable("country") String country){
		final List<Customer> customer = repository.findByCountry(country);
		LOGGER.info("Fetching customers from database {}" , customer);
		return customer;
	}
	
	@RequestMapping("es/customer/cn/{country}")
	public List<CustomerES> findByCountryES(@PathVariable("country") String country){
		final List<CustomerES> customer = repositoryES.findByCountry(country);
		LOGGER.info("Fetching customers from database {}" , customer);
		return customer;
	}
	
	@RequestMapping(value = "customer/" , method = RequestMethod.POST)
	public void save(@RequestBody Customer customer){
		LOGGER.info("Storing customer in database {}", customer);
		repository.save(customer);
	}
	
	@RequestMapping(value = "es/customer/" , method = RequestMethod.POST)
	public void save(@RequestBody CustomerES customer){
		LOGGER.info("Storing customer in database {}", customer);
		repositoryES.save(customer);
	}
	
	@RequestMapping(value = "es/sync/" , method = RequestMethod.GET)
	public void syncMongoAndES(){
		LOGGER.info("Sync ES Repository");
		List<Customer> data = repository.findAll();
		List<CustomerES> finalData = data.stream().map(i -> {
			CustomerES es = new CustomerES();
            try {
            	es.setCountry(i.country);
            	es.setFirstName(i.firstName);
            	es.setLastName(i.lastName);
            	es.setLocation(i.location);
            	es.setMiddleName(i.middleName);
            	es.setPhonenumber(i.phonenumber);
            	es.setTitle(i.title);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return es;
        }).collect(Collectors.toList());
		// delete Elasticsearch documents if already populated
		long documentCount = repositoryES.count();
		if (documentCount > 0) {
			repositoryES.deleteAll();
		}

		repositoryES.saveAll(finalData);
	}
	
}
