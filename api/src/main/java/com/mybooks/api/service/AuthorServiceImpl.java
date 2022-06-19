package com.mybooks.api.service;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.mapper.AuthorMapper;
import com.mybooks.api.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<AuthorDTO> fetchAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .map(authorMapper::transformToAuthorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO fetchById(String id) throws AuthorNotFoundException {
        return authorRepository.findById(id)
                .map(authorMapper::transformToAuthorDTO)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public AuthorDTO save(AuthorDTO authorDTO) {
        return Optional.of(authorDTO)
                .map(authorMapper::transformToAuthor)
                .map(authorRepository::save)
                .map(authorMapper::transformToAuthorDTO)
                .orElseThrow();
    }

    @Override
    public AuthorDTO update(AuthorDTO updatedAuthorDTO, String id) throws AuthorNotFoundException {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setFirstName(updatedAuthorDTO.getFirstName());
                    author.setLastName(updatedAuthorDTO.getLastName());
                    return author;
                })
                .map(authorRepository::save)
                .map(authorMapper::transformToAuthorDTO)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public void deleteAuthor(String id) throws AuthorNotFoundException {
        fetchById(id);
        authorRepository.deleteById(id);
    }
}
