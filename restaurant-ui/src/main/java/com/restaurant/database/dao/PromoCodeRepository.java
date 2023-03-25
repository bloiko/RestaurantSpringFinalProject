package com.restaurant.database.dao;


import com.restaurant.database.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    boolean existsPromoCodeByCode(String code);

    int findPromoCodeByCode(String code);

    Optional<PromoCode> findByCode(String code);
}















