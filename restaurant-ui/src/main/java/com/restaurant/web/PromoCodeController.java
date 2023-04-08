package com.restaurant.web;

import com.restaurant.database.entity.PromoCode;
import com.restaurant.service.PromoCodeService;
import com.restaurant.web.dto.PromoCodeDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            PromoCode code = promoCodeOptional.get();
            return mapPromoCode(code);
        }
        return new PromoCodeDto(false, promoCode, 0);
    }

    @GetMapping("/all")
    public List<PromoCodeDto> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromoCodes();

        return promoCodes.stream().map(PromoCodeController::mapPromoCode).collect(Collectors.toList());
    }

    @PostMapping
    public String addPromoCode(@RequestBody PromoCodeDto promoCodeDto) {
        return promoCodeService.addPromoCode(promoCodeDto);
    }

    @PutMapping("/deactivate/{promoCode}")
    public String deactivatePromoCode(@PathVariable String promoCode) {
        return promoCodeService.deactivatePromoCode(promoCode);
    }

    @PutMapping("/activate/{promoCode}")
    public String activatePromoCode(@PathVariable String promoCode) {
        return promoCodeService.activatePromoCode(promoCode);
    }

    private static PromoCodeDto mapPromoCode(PromoCode code) {
        return new PromoCodeDto(code.isActive(), code.getCode(), code.getDiscount());
    }
}
