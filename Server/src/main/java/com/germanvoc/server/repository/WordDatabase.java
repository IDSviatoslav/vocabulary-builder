package com.germanvoc.server.repository;

import com.germanvoc.server.model.Word;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WordDatabase extends CrudRepository<Word, Integer> {
    @Override
    Optional<Word> findById(Integer integer);

    List<Word> findByOriginal(String translation);
}
