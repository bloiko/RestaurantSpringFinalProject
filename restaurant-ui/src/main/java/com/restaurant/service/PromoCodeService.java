package com.restaurant.service;


import com.restaurant.database.dao.PromoCodeRepository;
import com.restaurant.database.entity.PromoCode;
import com.restaurant.web.dto.PromoCodeDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Order service.
 *
 * @author B.Loiko
 */
@Service
@Transactional
public class PromoCodeService extends ReaderServiceImpl<PromoCode> {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        super(promoCodeRepository);
        this.promoCodeRepository = promoCodeRepository;
    }

    public Optional<PromoCode> getPromoCode(String promoCode) {
        return promoCodeRepository.findByCode(promoCode);
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
        PromoCode promoCodeSaved = getPromoCode(promoCode)
                .orElseThrow(() -> new IllegalArgumentException("Promo Code is not present by this code"));

        promoCodeSaved.setActive(active);
        promoCodeRepository.save(promoCodeSaved);
        return String.valueOf(promoCode);
    }
}