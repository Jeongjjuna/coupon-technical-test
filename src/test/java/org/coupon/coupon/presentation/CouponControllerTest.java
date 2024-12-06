package org.coupon.coupon.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.coupon.coupon.application.CouponService;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponCreate;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.presentation.request.CouponCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[controller 단위테스트]")
@WebMvcTest(controllers = CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @DisplayName("쿠폰을 등록할 수 있다.")
    @Test
    void createCoupon() throws Exception {
        // given
        CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .couponType("BASIC")
                .description("description")
                .totalCouponCount(100)
                .build();

        // stubbing
        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .createdAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .suspendedAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .build();
        when(couponService.create(any(CouponCreate.class)))
                .thenReturn(coupon);

        // when
        var result = mockMvc.perform(post("/v1/coupons")
                .content(objectMapper.writeValueAsString(couponCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @DisplayName("쿠폰 등록 요청 시 couponType 은 null 일 수 없다.")
    @Test
    void createCouponWithNullCouponType() throws Exception {
        // given
        CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .couponType(null)
                .description("description")
                .totalCouponCount(100)
                .build();

        // when
        var result = mockMvc.perform(post("/v1/coupons")
                .content(objectMapper.writeValueAsString(couponCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }

    @DisplayName("쿠폰 등록 요청 시 description 은 null 일 수 없다.")
    @Test
    void createCouponWithNullDescription() throws Exception {
        // given
        CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .couponType("BASIC")
                .description(null)
                .totalCouponCount(100)
                .build();

        // when
        var result = mockMvc.perform(post("/v1/coupons")
                .content(objectMapper.writeValueAsString(couponCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }

    @DisplayName("쿠폰 등록 요청 시 totalCouponCount 은 null 일 수 없다.")
    @Test
    void createCouponWithNullTotalCouponCount() throws Exception {
        // given
        CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .couponType("BASIC")
                .description("description")
                .totalCouponCount(null)
                .build();

        // when
        var result = mockMvc.perform(post("/v1/coupons")
                .content(objectMapper.writeValueAsString(couponCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }
}