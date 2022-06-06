package com.mybooks.api.repository;

import com.mybooks.api.model.Book;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, String> {
}
