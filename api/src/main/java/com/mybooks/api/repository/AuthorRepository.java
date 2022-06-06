package com.mybooks.api.repository;

import com.mybooks.api.model.Author;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, String> {
}
