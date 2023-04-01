package com.restaurant.service;


import com.restaurant.database.dao.PromoCodeRepository;
import com.restaurant.database.entity.*;
import com.restaurant.web.dto.PromoCodeDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public String addPromoCode(PromoCodeDto promoCodeDto) {
        promoCodeRepository.save(new PromoCode(promoCodeDto.getCode(), promoCodeDto.getDiscount(), true));
        return promoCodeDto.getCode();
    }

    public String deactivatePromoCode(String promoCode) {
        return changeActiveStatusByCode(promoCode, false);
    }

    public String activatePromoCode(String promoCode) {
        return changeActiveStatusByCode(promoCode, true);
    }

    private String changeActiveStatusByCode(String promoCode, boolean active) {
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(promoCode);
        if (!optionalPromoCode.isPresent()) {
            throw new IllegalArgumentException("Promo Code is not present by this code");
        }

        PromoCode promoCodeFromDb = optionalPromoCode.get();
        promoCodeFromDb.setActive(active);
        promoCodeRepository.save(promoCodeFromDb);
        return String.valueOf(promoCode);
    }
}