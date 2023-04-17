package com.restaurant.service;

import com.restaurant.web.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ReaderServiceImpl<I> implements ReaderService<I> {

    private final JpaRepository<I, Long> jpaRepository;

    public ReaderServiceImpl(JpaRepository<I, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public I getById(Long id) {
        return getByField(id.toString(), idStr -> jpaRepository.findById(Long.parseLong(idStr)));
    }

    public I getByField(String value, Function<String, Optional<I>> getFunction) {
        Optional<I> optional = getFunction.apply(value);

        if (!optional.isPresent()) {
            throw new ResourceNotFoundException("Entity is not present by this value " + value);
        }

        return optional.get();
    }

    public List<I> getAll() {
        return jpaRepository.findAll();
    }
}
