package com.mybooks.api.reposiotry;

import com.mybooks.api.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, String> {
}
