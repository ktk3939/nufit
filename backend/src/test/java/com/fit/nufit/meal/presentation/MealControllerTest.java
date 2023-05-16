package com.fit.nufit.meal.presentation;

import com.fit.nufit.common.ControllerTest;
import com.fit.nufit.food.dto.response.FoodSimpleResponse;
import com.fit.nufit.meal.domain.MealType;
import com.fit.nufit.meal.dto.request.MealCreateRequest;
import com.fit.nufit.meal.dto.response.MealDetailsResponse;
import com.fit.nufit.meal.dto.response.MealResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MealController.class)
class MealControllerTest extends ControllerTest {

    @Test
    void 식사를_등록한다() throws Exception {
        // given
        Long memberId = 1L;
        Long mealId = 1L;
        MealCreateRequest request = new MealCreateRequest(MealType.BREAKFAST);
        given(mealService.save(any(), any(MealCreateRequest.class)))
                .willReturn(new MealResponse());

        // when & then
        mockMvc.perform(post("/api/meals")
                        .param("memberId", String.valueOf(memberId))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 식사에_해당하는_음식을_조회한다() throws Exception {
        // given
        Long mealId = 1L;
        List<FoodSimpleResponse> simpleFoods = List.of(
                new FoodSimpleResponse(1L, 1, "사과", 40),
                new FoodSimpleResponse(2L, 1, "배", 30)
        );

        given(mealDetailService.findAllByMealId(any()))
                .willReturn(new MealDetailsResponse(any(), 1.1, simpleFoods));

        // when & then
        mockMvc.perform(get("/api/meals/{mealId}/details", mealId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
