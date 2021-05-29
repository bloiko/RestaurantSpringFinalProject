package com.restaurant.database.dao;


import com.restaurant.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    <S extends User> S save(S s);
    @Override
    Optional<User> findById(Long aLong);
    Optional<User> findByUserName(String username);
}















