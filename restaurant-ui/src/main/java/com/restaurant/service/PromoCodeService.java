package com.restaurant.service;


import com.restaurant.database.dao.PromoCodeRepository;
import com.restaurant.database.entity.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Order service.
 *
 * @author B.Loiko
 */
@Service
@Transactional
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public Optional<PromoCode> getPromoCode(String promoCode) {
        return promoCodeRepository.findByCode(promoCode);
    }
}