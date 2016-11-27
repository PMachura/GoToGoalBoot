/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.nutrition;

import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.repository.nutrition.MealRepository;
import gotogoal.rest.resource.nutrition.MealResource;
import gotogoal.rest.resource.assembler.nutrition.MealResourceAssembler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    private Collection<Meal> findByUserIdAndNutritionDayId(Long userId, Long nutritionDayId) {
        return mealRepository.findByNutritionDayUserIdAndNutritionDayId(userId, nutritionDayId);
    }

    private Meal setMealFoodProduct(Meal meal) {
        meal.setMealsFoodProducts(mealFoodProductService.findByMealId(meal.getId()));
        return meal;
    }

    private Collection<Meal> setMealFoodProductToNull(Collection<Meal> meals) {
        meals.forEach((Meal meal) -> {
            meal.setMealsFoodProducts(null);
        });
        return meals;
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

    public Collection<MealResource> mapToResource(Collection<Meal> meals) {
        return Arrays.asList(meals.stream()
                .map(mealResourceAssembler::toResource)
                .toArray(MealResource[]::new));
    }
    
    public MealResource mapToResource(Meal meal) {
        return mealResourceAssembler.toResource(meal);
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
        System.out.println("MEAL TIME" + meal.getTime());
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

        mealFoodProductService.save(created.getMealsFoodProducts());
        return created;
    }

    @Transactional
    Collection<Meal> create(Collection<Meal> meals) {
        Collection<Meal> created = new ArrayList<Meal>();
        meals.forEach((Meal meal) -> created.add(this.create(meal)));
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
        if (getMealFoodProductIds(meal).isEmpty()) {
            mealFoodProductService.deleteByMealId(meal.getId());
        } else {
            mealFoodProductService.deleteByMealIdInAndIdNotIn(meal.getId(), getMealFoodProductIds(meal));
        }

        return updated;
    }

    @Transactional
    public Collection<Meal> update(Collection<Meal> meals) {
        Collection<Meal> created = new ArrayList<Meal>();
        meals.forEach((Meal meal) -> created.add(update(meal)));
        return created;
    }

    @Transactional
    public void delete(Long mealId) {
        mealFoodProductService.deleteByMealId(mealId);
        mealRepository.delete(mealId);
    }

    @Transactional
    public void deleteByNutritionDayId(Long nutritionDayId) {
        mealFoodProductService.deleteByMealNutritionDayId(nutritionDayId);
        mealRepository.deleteByNutritionDayId(nutritionDayId);
    }

    @Transactional
    public void deleteByNutritionDayIdInAndIdNotId(Long nutritionDayId, Collection<Long> melsIds) {
        mealFoodProductService.deleteByMealNutritionDayIdInAndMealIdNotIn(nutritionDayId, melsIds);
        mealRepository.deleteByNutritionDayIdInAndIdNotIn(nutritionDayId, melsIds);
    }

    public Collection<Meal> findByUserEmailAndNutritionDayDateEager(String userEmail, LocalDate nutritionDayDate) {
        Collection<Meal> meals = findAll(userEmail, nutritionDayDate);
        meals = setMealFoodProduct(meals);
        return meals;
    }

    public Collection<Meal> findByUserIdAndNutritionDayIdEager(Long userId, Long nutritionDayId) {
        Collection<Meal> meals = findByUserIdAndNutritionDayId(userId, nutritionDayId);
        meals = setMealFoodProduct(meals);
        return meals;
    }

//    public Collection<MealResource> findAllByUserEmailAndNutritionDayDateAsResourceEager(String userEmail, LocalDate nutritionDayDate) {
//        return mapToResource(findByUserEmailAndNutritionDayDateEager(userEmail, nutritionDayDate));
//    }

//    public Collection<MealResource> findByUserIdAndNutritionDayIdAsResourceEager(Long userId, Long nutritionDayId) {
//        return mapToResource(findByUserIdAndNutritionDayIdEager(userId, nutritionDayId));
//    }

    public Collection<Meal> findAllByNutritionDayIdLazy(Long nutritionDayId) {
        Collection<Meal> meals = mealRepository.findByNutritionDayId(nutritionDayId);
        return setMealFoodProductToNull(meals);
    }

    public Collection<Meal> findAllByNutritionDayIdEager(Long nutritionDayId) {
        Collection<Meal> meals = mealRepository.findByNutritionDayId(nutritionDayId);
        setMealFoodProduct(meals);
        return meals;
    }

//    public Collection<MealResource> findAllByNutritionDayIdAsResourceEager(Long nutritionDayId) {
//        return mapToResource(findAllByNutritionDayIdEager(nutritionDayId));
//    }

}
