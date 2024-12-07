package org.coupon.issue.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.coupon.issue.application.CouponIssueService;
import org.coupon.issue.domain.CouponIssueCommand;
import org.coupon.issue.presentation.request.CouponIssueRequest;
import org.coupon.issue.presentation.request.CouponRedeemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[controller 단위테스트] CouponIssueController")
@WebMvcTest(controllers = CouponIssueController.class)
class CouponIssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponIssueService couponIssueService;

    @DisplayName("쿠폰 코드를 발급할 수 있다.")
    @Test
    void issueCouponCode() throws Exception {
        // given
        CouponIssueRequest couponIssueRequest = CouponIssueRequest.builder()
                .couponId(1L)
                .memberId(1L)
                .build();

        // stubbing
        when(couponIssueService.issueCouponCode(any(CouponIssueCommand.class)))
                .thenReturn("couponCode");

        // when
        var result = mockMvc.perform(post("/v1/issues")
                .content(objectMapper.writeValueAsString(couponIssueRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @DisplayName("쿠폰 코드 발급 요청시 memberId 는 null 일 수 없다.")
    @Test
    void issueCouponCodeWithNullMemberId() throws Exception {
        // given
        CouponIssueRequest couponIssueRequest = CouponIssueRequest.builder()
                .couponId(1L)
                .memberId(null)
                .build();

        // stubbing
        when(couponIssueService.issueCouponCode(any(CouponIssueCommand.class)))
                .thenReturn("couponCode");

        // when
        var result = mockMvc.perform(post("/v1/issues")
                .content(objectMapper.writeValueAsString(couponIssueRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }

    @DisplayName("쿠폰 코드 발급 요청시 couponId 는 null 일 수 없다.")
    @Test
    void issueCouponCodeWithNullCouponId() throws Exception {
        // given
        CouponIssueRequest couponIssueRequest = CouponIssueRequest.builder()
                .couponId(null)
                .memberId(1L)
                .build();

        // stubbing
        when(couponIssueService.issueCouponCode(any(CouponIssueCommand.class)))
                .thenReturn("couponCode");

        // when
        var result = mockMvc.perform(post("/v1/issues")
                .content(objectMapper.writeValueAsString(couponIssueRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }

    @DisplayName("쿠폰 코드를 사용할 수 있다.")
    @Test
    void redeemCouponCode() throws Exception {
        // given
        CouponRedeemRequest couponRedeemRequest = CouponRedeemRequest.builder()
                .memberId(1L)
                .couponCode("1234567891011abc")
                .build();
        ;

        // when
        var result = mockMvc.perform(post("/v1/issues/redeem")
                .content(objectMapper.writeValueAsString(couponRedeemRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @DisplayName("쿠폰 코드 사용 요청시 memberId 는 null 일 수 없다.")
    @Test
    void redeemCouponCodeWithNullMemberId() throws Exception {
        // given
        CouponRedeemRequest couponRedeemRequest = CouponRedeemRequest.builder()
                .memberId(null)
                .couponCode("1234567891011abc")
                .build();
        ;

        // when
        var result = mockMvc.perform(post("/v1/issues/redeem")
                .content(objectMapper.writeValueAsString(couponRedeemRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }

    @DisplayName("쿠폰 코드 발급 요청시 couponCode 는 null 일 수 없다.")
    @Test
    void redeemCouponCodeWithNullCouponId() throws Exception {
        // given
        CouponRedeemRequest couponRedeemRequest = CouponRedeemRequest.builder()
                .memberId(1L)
                .couponCode(null)
                .build();
        ;

        // when
        var result = mockMvc.perform(post("/v1/issues/redeem")
                .content(objectMapper.writeValueAsString(couponRedeemRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result_code").value("400"));
    }
}