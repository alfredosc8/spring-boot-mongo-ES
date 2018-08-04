package com.example.elasticsearch.aws.SpringbootmongoES.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.elasticsearch.aws.SpringbootmongoES.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);
    @Query("{ 'country' : ?0 }")
    public List<Customer> findByCountry(String country);

}
