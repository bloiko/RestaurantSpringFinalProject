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
        promoCodeRepository.save(new PromoCode(promoCode, discount));

        PromoCodeDto promoCodeDto = promoCodeController.getPromoCode(promoCode);

        assertTrue(promoCodeDto.isExists());
        assertEquals(promoCode, promoCodeDto.getCode());
        assertEquals(discount, promoCodeDto.getDiscount());
    }

    @Test
    void getPromoCodeThatNotExists() {
        String promoCode = "not_exists";

        PromoCodeDto promoCodeDto = promoCodeController.getPromoCode(promoCode);

        assertFalse(promoCodeDto.isExists());
        assertEquals(promoCode, promoCodeDto.getCode());
        assertEquals(0, promoCodeDto.getDiscount());
    }
}