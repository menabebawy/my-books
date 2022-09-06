package com.mybooks.oauthserver.repository;

import com.mybooks.oauthserver.model.UserEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface UserInfoRepository extends PagingAndSortingRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String username);
}
