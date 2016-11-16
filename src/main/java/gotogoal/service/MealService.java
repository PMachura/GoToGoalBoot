/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.Meal;
import gotogoal.model.MealFoodProduct;
import gotogoal.repository.MealRepository;
import gotogoal.rest.resource.MealResource;
import gotogoal.rest.resource.assembler.MealResourceAssembler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
public class MealService {

    private MealRepository mealRepository;
    private MealFoodProductService mealFoodProductService;
    private MealResourceAssembler mealResourceAssembler;

    @Autowired
    public MealService(MealRepository mealRepository,
            MealFoodProductService mealFoodProductService,
            MealResourceAssembler mealResourceAssembler) {
        this.mealRepository = mealRepository;
        this.mealFoodProductService = mealFoodProductService;
        this.mealResourceAssembler = mealResourceAssembler;
    }

    private Meal findOne(Long id) {
        return mealRepository.findOne(id);
    }

    private Collection<Meal> findAll(String userEmail, LocalDate nutritionDayDate) {
        return mealRepository.findByNutritionDayUserEmailAndNutritionDayDate(userEmail, nutritionDayDate);
    }

    private Meal setMealFoodProduct(Meal meal) {
        meal.setMealsFoodProducts(mealFoodProductService.findByMealId(meal.getId()));
        return meal;
    }

    private Collection<Meal> setMealFoodProduct(Collection<Meal> meals) {
        meals.forEach((Meal meal) -> {
            setMealFoodProduct(meal);
        });
        return meals;
    }

    private Meal setMealInMealsFoodProducts(Meal meal) {
        meal.getMealsFoodProducts().forEach((MealFoodProduct mealFoodProduct) -> {
            mealFoodProduct.setMeal(meal);
        });
        return meal;
    }

    private Collection<MealResource> mapToResource(Collection<Meal> meals) {
        return Arrays.asList(meals.stream()
                .map(mealResourceAssembler::toResource)
                .toArray(MealResource[]::new));
    }

    private Meal save(Meal meal) {
        return mealRepository.save(meal);
    }

    private Collection<Long> getMealFoodProductIds(Meal meal) {
        return meal.getMealsFoodProducts()
                .stream()
                .map((MealFoodProduct mealFoodProduct) -> mealFoodProduct.getId())
                .filter(out -> out != null)
                .collect(Collectors.toList());
    }

    /**
     * **************************************************************************************************
     */
    public Meal findOneEager(Long id) {
        return setMealFoodProduct(findOne(id));
    }

    public void temporaryDebug(String message, Meal meal) {
        System.out.println(message);
        System.out.println("MEAL ID" + meal.getId());
        if (meal.getNutritionDay() == null) {
            System.out.println("MEAL  -> NUTRITION DAY = NULL");
        } else {
            System.out.println("MEAL  -> NUTRITION DAY ID " + meal.getNutritionDay().getId());
        }
        System.out.println("MEAL FOOD PRODUCT LOOP");
        for (MealFoodProduct mfp : meal.getMealsFoodProducts()) {
            System.out.println("MEAL FOOD PRODUCT ID " + mfp.getId());
            System.out.println("GRAMS " + mfp.getGrams());
            if (mfp.getFoodProduct() == null) {
                System.out.println("MEAL FOOD PRODUCT -> FOOD PRODUCT = NULL ");
            } else {
                System.out.println("MEAL FOOD PRODUCT -> FOOD PRODUCT ID " + mfp.getFoodProduct().getId());
            }
            if (mfp.getMeal() == null) {
                System.out.println("MEAL FOOD PRODUCT ->  MEAL = NULL");
            } else {
                System.out.println("MEAL FOOD PRODUCT -> MEAL ID " + mfp.getMeal().getId());
            }
            System.out.println();
        }
    }

    @Transactional
    public Meal create(Meal meal) {
        meal = setMealInMealsFoodProducts(meal);
        Meal created = save(meal);
        // temporaryDebug("saved meal", created);
        mealFoodProductService.save(created.getMealsFoodProducts());
        return created;
    }

    /**
     * This function first delete the MealFoodProduct entities which are not in
     * the passed Meal Then updates the MealFoodProduct entities which are in
     * the passed Meal Then updates Meal entity
     *
     * @param meal
     */
    @Transactional
    public Meal update(Meal meal) {

        meal = setMealInMealsFoodProducts(meal);
        Meal updated = save(meal);
        Collection<MealFoodProduct> savedMealsFoodProducts = mealFoodProductService.save(meal.getMealsFoodProducts());
        updated.setMealsFoodProducts((List<MealFoodProduct>) savedMealsFoodProducts);
        mealFoodProductService.deleteByMealIdInAndIdNotIn(meal.getId(), getMealFoodProductIds(meal));
        return updated;
    }

    @Transactional
    public void delete(Long mealId) {
        mealFoodProductService.deleteByMealId(mealId);
        mealRepository.delete(mealId);
    }

    public Collection<Meal> findAllByUserEmailAndNutritionDayDateEager(String userEmail, LocalDate nutritionDayDate) {
        Collection<Meal> meals = findAll(userEmail, nutritionDayDate);
        meals = setMealFoodProduct(meals);
        return meals;
    }

    public Collection<MealResource> findAllByUserEmailAndNutritionDayDateAsResourceEager(String userEmail, LocalDate nutritionDayDate) {
        return mapToResource(findAllByUserEmailAndNutritionDayDateEager(userEmail, nutritionDayDate));
    }

    public Collection<Meal> findAllByNutritionDayIdLazy(Long nutritionDayId) {
        return mealRepository.findByNutritionDayId(nutritionDayId);
    }

    public Collection<Meal> findAllByNutritionDayIdEager(Long nutritionDayId) {
        Collection<Meal> meals = mealRepository.findByNutritionDayId(nutritionDayId);
        setMealFoodProduct(meals);
        return meals;
    }

    public Collection<MealResource> findAllByNutritionDayIdAsResourceEager(Long nutritionDayId) {
        return mapToResource(findAllByNutritionDayIdEager(nutritionDayId));
    }

}
