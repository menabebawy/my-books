package com.mybooks.bookinfoservice.repository;

import com.mybooks.bookinfoservice.model.Book;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface BookInfoRepository extends PagingAndSortingRepository<Book, String> {
}
