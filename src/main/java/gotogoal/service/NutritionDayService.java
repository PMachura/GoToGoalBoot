/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.exception.EntityDuplicateException;
import gotogoal.model.NutritionDay;
import gotogoal.model.Meal;
import gotogoal.model.MealFoodProduct;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gotogoal.rest.resource.NutritionDayResource;
import gotogoal.rest.resource.assembler.NutritionDayResourceAssembler;
import java.util.Arrays;
import gotogoal.repository.NutritionDayRepository;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
public class NutritionDayService {

    private NutritionDayRepository nutritionDayRepository;
    private MealService mealService;
    private UserService userService;
    private NutritionDayResourceAssembler nutritionDayResourceAssembler;

    @Autowired
    public NutritionDayService(NutritionDayRepository nutritionDayRepository,
            MealService mealService, UserService userService,
            NutritionDayResourceAssembler nutritionDayResourceAssembler) {
        this.nutritionDayRepository = nutritionDayRepository;
        this.nutritionDayResourceAssembler = nutritionDayResourceAssembler;
        this.mealService = mealService;
        this.userService = userService;
    }

    /**
     * *********************************** HELPERS FUNCTION
     * *****************************
     */
    private void validateUniqueness(Long userId, LocalDate date) {
        if (nutritionDayRepository.findByUserIdAndDate(userId, date) != null) {
            throw new EntityDuplicateException("Nutrition Day with date: " + date + " already exists for user: " + userId);
        }
    }

    private NutritionDay findOne(Long id) {
        return nutritionDayRepository.findOne(id);
    }

    private NutritionDay findByUserEmailAndDate(String userName, LocalDate localDate) {
        return nutritionDayRepository.findByUserEmailAndDate(userName, localDate);
    }

    private Collection<NutritionDay> findAll() {
        return nutritionDayRepository.findAll();
    }

    private Collection<NutritionDayResource> mapToResource(Collection<NutritionDay> collection) {
        return Arrays.asList(collection.stream().map(nutritionDayResourceAssembler::toResource)
                .toArray(NutritionDayResource[]::new));
    }

    private NutritionDay setMealsLazy(NutritionDay nutritionDay) {
        nutritionDay.setMeals((List<Meal>) mealService.findAllByNutritionDayIdLazy(nutritionDay.getId()));
        return nutritionDay;
    }

    private Collection<NutritionDay> setMealsLazy(Collection<NutritionDay> nutritionDays) {
        nutritionDays.forEach((NutritionDay nutritionDay) -> setMealsLazy(nutritionDay));
        return nutritionDays;
    }

    private NutritionDay setMealsEager(NutritionDay nutritionDay) {
        nutritionDay.setMeals((List<Meal>) mealService.findAllByNutritionDayIdEager(nutritionDay.getId()));
        return nutritionDay;
    }

    private Collection<NutritionDay> setMealsToNull(Collection<NutritionDay> nutritionDays) {
        nutritionDays.forEach((NutritionDay nutritionDay) -> {
            nutritionDay.setMeals(null);
        });
        return nutritionDays;
    }

    private Collection<Long> getMealsIds(NutritionDay nutritionDay) {
        return nutritionDay.getMeals()
                .stream()
                .map((Meal meal) -> meal.getId())
                .filter(out -> out != null)
                .collect(Collectors.toList());
    }

    private NutritionDay save(NutritionDay nutritionDay) {
        return nutritionDayRepository.save(nutritionDay);
    }

    /**
     * *****************************************************************************************
     */
    public NutritionDayResource mapToResource(NutritionDay nutritionDay) {
        return nutritionDayResourceAssembler.toResource(nutritionDay);
    }

    public Page<NutritionDayResource> mapToResource(Page<NutritionDay> nutritionDayPage) {
        return nutritionDayPage.map(nutritionDayResourceAssembler::toResource);
    }

    public NutritionDay findOneEager(Long id) {
        return setMealsLazy(findOne(id));
    }

    public NutritionDay findOneEagerDeep(Long id) {
        return setMealsEager(findOne(id));
    }

//    public NutritionDayResource findOneAsResourceEagerDeep(Long id) {
//        return nutritionDayResourceAssembler.toResource(this.findOneEagerDeep(id));
//    }
    public NutritionDay findOneByUserEmailAndDateEager(String userName, LocalDate localDate) {
        NutritionDay nutritionDay = this.findByUserEmailAndDate(userName, localDate);
        Collection<Meal> meals = mealService.findAllByNutritionDayIdLazy(nutritionDay.getId());
        nutritionDay.setMeals((List<Meal>) meals);
        return nutritionDay;
    }

//    public NutritionDayResource findOneEagerAsResource(Long id) {
//        return nutritionDayResourceAssembler.toResource(this.findOneEager(id));
//    }
//    public NutritionDayResource findOneByUserEmailAndDateAsResourceEager(String userName, LocalDate localDate) {
//        return nutritionDayResourceAssembler.toResource(this.findOneByUserEmailAndDateEager(userName, localDate));
//    }
    public Page<NutritionDay> findAllByUserEmailAndDateAsPageLazy(String userEmail, LocalDate localDate, Pageable pageable) {
        Page<NutritionDay> nutritionDayPage = nutritionDayRepository.findByUserEmail(userEmail, pageable);
        setMealsToNull(nutritionDayPage.getContent());
        return nutritionDayPage;
    }

    public Page<NutritionDay> findAllByUserIdEager(Long userId, Pageable pageable) {
        Page<NutritionDay> nutritionDayPage = nutritionDayRepository.findByUserId(userId, pageable);
        setMealsLazy(nutritionDayPage.getContent());
        return nutritionDayPage;
    }

    @Transactional
    public NutritionDay create(NutritionDay nutritionDay, Long userId) {
        validateUniqueness(userId, nutritionDay.getDate());
        nutritionDay.setUser(userService.findOne(userId));
        NutritionDay created = nutritionDayRepository.save(nutritionDay);
        mealService.create(created.getMeals());
        return created;
    }

    public void delete(Long userId, Long nutritionDayId) {
        mealService.deleteByNutritionDayId(nutritionDayId);
        nutritionDayRepository.delete(nutritionDayId);
    }

    public NutritionDay update(NutritionDay nutritionDay, Long userId, Long nutritionDayId) {
        nutritionDay.setUser(userService.findOne(userId));
        NutritionDay saved = save(nutritionDay);
        if (getMealsIds(nutritionDay).isEmpty()) {
            mealService.deleteByNutritionDayId(saved.getId());
        } else {
            mealService.deleteByNutritionDayIdInAndIdNotId(nutritionDayId, getMealsIds(nutritionDay));
        }
        
        mealService.update(nutritionDay.getMeals());
        return saved;
    }
}
