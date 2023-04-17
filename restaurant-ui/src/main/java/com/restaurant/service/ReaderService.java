package com.restaurant.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

interface ReaderService<I> {

    I getById(Long id);

    I getByField(String value, Function<String, Optional<I>> getFunction);

    List<I> getAll();
}
