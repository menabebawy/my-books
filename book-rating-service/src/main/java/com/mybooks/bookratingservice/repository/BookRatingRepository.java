package com.mybooks.bookratingservice.repository;

import com.mybooks.bookratingservice.model.UserBooksRatings;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface BookRatingRepository extends PagingAndSortingRepository<UserBooksRatings, String> {
}
