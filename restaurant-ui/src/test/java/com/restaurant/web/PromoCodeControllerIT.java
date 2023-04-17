package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.PromoCodeRepository;
import com.restaurant.database.entity.PromoCode;
import com.restaurant.web.dto.PromoCodeDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class PromoCodeControllerIT {

    @Autowired
    private PromoCodeController promoCodeController;

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Test
    void getPromoCode() {
        String promoCode = "PROMOCODE_20";
        int discount = 20;
        promoCodeRepository.save(new PromoCode(promoCode, discount, true));

        PromoCodeDto promoCodeDto = promoCodeController.getPromoCode(promoCode);

        assertTrue(promoCodeDto.isActive());
        assertEquals(promoCode, promoCodeDto.getCode());
        assertEquals(discount, promoCodeDto.getDiscount());
    }

    @Test
    void getPromoCodeThatNotExists() {
        String promoCode = "not_exists";

        PromoCodeDto promoCodeDto = promoCodeController.getPromoCode(promoCode);

        assertFalse(promoCodeDto.isActive());
        assertEquals(promoCode, promoCodeDto.getCode());
        assertEquals(0, promoCodeDto.getDiscount());
    }

    @Test
    void getAllPromoCodes() {
        String promoCode = "PROMOCODE_20";
        int discount = 20;
        PromoCode entity = new PromoCode(promoCode, discount, true);
        promoCodeRepository.save(entity);

        List<PromoCodeDto> promoCodes = promoCodeController.getAllPromoCodes();

        assertTrue(promoCodes.size() > 0);
        assertTrue(promoCodes.contains(new PromoCodeDto(true, promoCode, discount)));
    }

    @Test
    void addPromoCode() {
        String promoCode = "NEW_PROMOCODE_21";
        int discount = 13;

        String code = promoCodeController.addPromoCode(new PromoCodeDto(true, promoCode, discount));

        assertEquals(promoCode, code);
        Optional<PromoCode> promoCodeFromDb = promoCodeRepository.findByCode(promoCode);
        assertTrue(promoCodeFromDb.isPresent());
        assertEquals(promoCode, promoCodeFromDb.get().getCode());
        assertEquals(discount, promoCodeFromDb.get().getDiscount());
        assertTrue(promoCodeFromDb.get().isActive());
    }

    @Test
    void deactivatePromoCode() {
        String promoCode = "PROMOCODE_21";
        int discount = 21;
        promoCodeRepository.save(new PromoCode(promoCode, discount, true));

        String code = promoCodeController.deactivatePromoCode(promoCode);

        assertEquals(promoCode, code);
        Optional<PromoCode> promoCodeFromDb = promoCodeRepository.findByCode(promoCode);
        assertTrue(promoCodeFromDb.isPresent());
        assertEquals(promoCode, promoCodeFromDb.get().getCode());
        assertEquals(discount, promoCodeFromDb.get().getDiscount());
        assertFalse(promoCodeFromDb.get().isActive());
    }

    @Test
    void activatePromoCode() {
        String promoCode = "PROMOCODE_21";
        int discount = 21;
        promoCodeRepository.save(new PromoCode(promoCode, discount, false));

        String code = promoCodeController.activatePromoCode(promoCode);

        assertEquals(promoCode, code);
        Optional<PromoCode> promoCodeFromDb = promoCodeRepository.findByCode(promoCode);
        assertTrue(promoCodeFromDb.isPresent());
        assertEquals(promoCode, promoCodeFromDb.get().getCode());
        assertEquals(discount, promoCodeFromDb.get().getDiscount());
        assertTrue(promoCodeFromDb.get().isActive());
    }
}