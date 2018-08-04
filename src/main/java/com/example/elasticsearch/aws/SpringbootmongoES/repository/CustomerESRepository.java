package com.example.elasticsearch.aws.SpringbootmongoES.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.elasticsearch.aws.SpringbootmongoES.model.CustomerES;

@Repository
public interface CustomerESRepository extends ElasticsearchRepository<CustomerES, String> {
	

	CustomerES findByFirstName(String firstName);
	List<CustomerES> findByLastName(String lastName);
    @Query("{ 'country' : ?0 }")
    public List<CustomerES> findByCountry(String country);

}
