package com.restaurant.web;

import com.restaurant.database.entity.PromoCode;
import com.restaurant.service.PromoCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.restaurant.web.dto.PromoCodeDto;

import java.util.Optional;

@RestController
@RequestMapping("/promocode")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping("/{promoCode}")
    public PromoCodeDto getPromoCode(@PathVariable String promoCode) {
        Optional<PromoCode> promoCodeOptional = promoCodeService.getPromoCode(promoCode);

        if (promoCodeOptional.isPresent()) {
            return new PromoCodeDto(true, promoCodeOptional.get().getCode(), promoCodeOptional.get().getDiscount());
        }
        return new PromoCodeDto(false, promoCode, 0);
    }
}
