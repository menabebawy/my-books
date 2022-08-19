package com.mybooks.authorinfoservice.repository;

import com.mybooks.authorinfoservice.model.Author;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, String> {
}
