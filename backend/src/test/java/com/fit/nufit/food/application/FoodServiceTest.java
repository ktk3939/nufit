package com.fit.nufit.food.application;

import com.fit.nufit.food.domain.*;
import com.fit.nufit.food.dto.request.FoodCreateRequest;
import com.fit.nufit.food.dto.request.FoodNutrientCreateRequest;
import com.fit.nufit.food.dto.response.NutrientDetailResponse;
import com.fit.nufit.meal.domain.*;
import com.fit.nufit.member.domain.Member;
import com.fit.nufit.member.domain.MemberRepository;
import com.fit.nufit.nutrient.domain.Nutrient;
import com.fit.nufit.nutrient.domain.NutrientRepository;
import com.fit.nufit.nutrient.domain.NutrientUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FoodServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FoodService foodService;

    @Autowired
    NutrientRepository nutrientRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FoodNutrientRepository foodNutrientRepository;

    @Autowired
    MealDetailRepository mealDetailRepository;

    @Autowired
    MealRepository mealRepository;

    @BeforeEach
    void beforeEach() {
        nutrientRepository.save(new Nutrient("탄수화물", NutrientUnit.G));
        nutrientRepository.save(new Nutrient("지방", NutrientUnit.G));
        nutrientRepository.save(new Nutrient("단백질", NutrientUnit.G));
    }

    @Test
    @Transactional
    void 음식의_영양성분_상세를_조회한다() {

        // given
        Member member = new Member("태경@google.com");
        memberRepository.save(member);

        Meal meal = new Meal(member, MealType.LUNCH);
        mealRepository.save(meal);

        Food pasta = new Food("파스타", 1, FoodUnit.G, "오뚜기", FoodType.from("brand"), 500);
        foodRepository.save(pasta);

        MealDetail mealDetail = new MealDetail(meal, pasta, 2);
        mealDetailRepository.save(mealDetail);

        Nutrient carb = new Nutrient("탄수화물", NutrientUnit.G);
        nutrientRepository.save(carb);
        Nutrient sugar = new Nutrient("당", NutrientUnit.G);
        sugar.setParentNutrient(carb);
        nutrientRepository.save(sugar);

        FoodNutrient foodNutrient1 = new FoodNutrient(pasta, carb, 50);
        FoodNutrient foodNutrient2 = new FoodNutrient(pasta, sugar, 15);
        foodNutrientRepository.save(foodNutrient1);
        foodNutrientRepository.save(foodNutrient2);

        // when
        NutrientDetailResponse response = foodService.getNutrientDetailByMealDetailId(mealDetail.getId());

        // then
        assertThat(response.getFoodName()).isEqualTo(pasta.getName());
        assertThat(response.getCalorieTotal()).isEqualTo((int) pasta.getCalorie() * mealDetail.getFoodCount());
        assertThat(response.getNutrientResponses().get(0).getName()).isEqualTo("탄수화물");
        assertThat(response.getNutrientResponses().get(0)
                .getChildNutrientResponses().get(0).getName()).isEqualTo("당");
    }

    @Test
    void 새로운_음식을_등록한다() {
        // given
        FoodNutrientCreateRequest carb = new FoodNutrientCreateRequest("탄수화물", 10);
        FoodNutrientCreateRequest fat = new FoodNutrientCreateRequest("지방", 5);
        FoodCreateRequest foodCreateRequest = new FoodCreateRequest("파스타", "오뚜기", 1,
                "g", "brand", 500, List.of(carb, fat));

        // when
        foodService.save(foodCreateRequest);

        // then
        assertDoesNotThrow(() ->
                foodRepository.getByName("파스타"));
        Food pasta = foodRepository.getByName("파스타");
        List<FoodNutrient> foodNutrients = foodNutrientRepository.getByFoodId(pasta.getId());
        assertThat(foodNutrients.size()).isEqualTo(2);
    }
}
