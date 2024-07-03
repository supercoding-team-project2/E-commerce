package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.sale.dto.SaleGetDto;
import com.github.supercodingproject2mall.sale.dto.SalePostDto;
import com.github.supercodingproject2mall.sale.dto.SalePutDto;
import com.github.supercodingproject2mall.sale.mapper.SaleMapper;
import com.github.supercodingproject2mall.sale.service.SaleGetService;
import com.github.supercodingproject2mall.sale.service.SalePostService;
import com.github.supercodingproject2mall.sale.service.SalePutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sale")
public class SaleController {

    private final SalePostService salePostService;
    private final SaleGetService saleGetService;
    private final SalePutService salePutService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SaleMapper saleMapper;

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<SalePostDto> addSaleItem(
            @RequestPart("salePostDto") String salePostDtoString,
            @RequestPart("images") List<MultipartFile> images,
            HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);

            SalePostDto salePostDto = saleMapper.mapJsonToSalePostDto(salePostDtoString);

            SalePostDto response = salePostService.addSaleItem(salePostDto, userId, images);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SalePostDto());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<SaleGetDto>> getCurrentSaleItems(HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);

            List<SaleGetDto> response = saleGetService.getCurrentSaleItems(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update-stock")
    public ResponseEntity<String> updateItemStock(@RequestBody SalePutDto salePutDto, HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);

            salePutService.updateItemStock(salePutDto, userId);
            return ResponseEntity.ok("성공적으로 수정 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("재고 업데이트 실패.");
        }
    }
}
