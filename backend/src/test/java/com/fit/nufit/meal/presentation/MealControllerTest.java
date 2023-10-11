package com.fit.nufit.meal.presentation;

import com.fit.nufit.common.ControllerTest;
import com.fit.nufit.meal.dto.request.MealCreateRequest;
import com.fit.nufit.meal.dto.request.MealDetailCreateRequest;
import com.fit.nufit.meal.dto.response.MealDailyCaloriesResponse;
import com.fit.nufit.meal.dto.response.MealDetailResponse;
import com.fit.nufit.meal.dto.response.MealDetailsResponse;
import com.fit.nufit.meal.dto.response.MealResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MealController.class)
class MealControllerTest extends ControllerTest {

    @Test
    void 식사를_등록한다() throws Exception {
        // given
        Long memberId = 1L;
        MealCreateRequest request = new MealCreateRequest();

        given(mealService.save(any(), any(MealCreateRequest.class)))
                .willReturn(new MealResponse());

        // when & then
        mockMvc.perform(post("/api/meals")
                        .param("memberId", String.valueOf(memberId))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(oauth2Login())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 식사상세를_등록한다() throws Exception {
        // given
        Long mealId = 1L;
        MealDetailCreateRequest request = new MealDetailCreateRequest();

        given(mealDetailService.save(any(), any(MealDetailCreateRequest.class)))
                .willReturn(new MealDetailResponse());

        // when & then
        mockMvc.perform(post("/api/meals/{mealId}", mealId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(oauth2Login())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 하루섭취_총칼로리를_조회한다() throws Exception {
        // given
        Long memberId = 1L;

        given(mealService.findDailyCaloriesByMemberId(any()))
                .willReturn(new MealDailyCaloriesResponse());

        // when & then
        mockMvc.perform(get("/api/meals/me/calories")
                        .param("memberId", String.valueOf(memberId))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(oauth2Login())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 식사에_해당하는_음식을_조회한다() throws Exception {
        // given
        Long mealId = 1L;

        given(mealDetailService.findAllByMealId(any()))
                .willReturn(new MealDetailsResponse());

        // when & then
        mockMvc.perform(get("/api/meals/{mealId}/details", mealId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                        .with(oauth2Login())
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void 식사상세를_삭제한다() throws Exception {
        // given
        Long mealDetailId = 1L;
        willDoNothing().given(mealDetailService).delete(any());

        // when & then
        mockMvc.perform(delete("/api/meals/details/{mealDetailId}", mealDetailId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(oauth2Login())
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}