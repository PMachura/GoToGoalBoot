/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.nutrition;

import gotogoal.service.user.UserService;
import gotogoal.exception.EntityDuplicateException;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import gotogoal.rest.resource.assembler.nutrition.NutritionDayResourceAssembler;
import java.util.Arrays;
import gotogoal.repository.nutrition.NutritionDayRepository;
import gotogoal.validator.NutritionDayValidator;
import java.security.Principal;
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
    private NutritionDayValidator nutritionDayValidator;

    @Autowired
    public NutritionDayService(NutritionDayRepository nutritionDayRepository,
            MealService mealService, UserService userService,
            NutritionDayResourceAssembler nutritionDayResourceAssembler,
            NutritionDayValidator nutritionDayValidator) {
        this.nutritionDayRepository = nutritionDayRepository;
        this.mealService = mealService;
        this.userService = userService;
        this.nutritionDayValidator = nutritionDayValidator;
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
    
    

    public NutritionDay findOneEager(Long id) {
        return setMealsLazy(findOne(id));
    }

    public NutritionDay findOneEagerDeep(Long id) {
        return setMealsEager(findOne(id));
    }

    public NutritionDay findOneByUserEmailAndDateEager(String userName, LocalDate localDate) {
        NutritionDay nutritionDay = this.findByUserEmailAndDate(userName, localDate);
        Collection<Meal> meals = mealService.findAllByNutritionDayIdLazy(nutritionDay.getId());
        nutritionDay.setMeals((List<Meal>) meals);
        return nutritionDay;
    }


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
    
    public Page<NutritionDay> findAllByUserIdAndDateLessThanEqualEager(Long userId, LocalDate localDate, Pageable pageable){
        Page<NutritionDay> nutritionDayPage = nutritionDayRepository.findByUserIdAndDateLessThanEqual(userId, localDate, pageable);
        setMealsLazy(nutritionDayPage.getContent());
        return nutritionDayPage;
    }

    @Transactional
    public NutritionDay create(NutritionDay nutritionDay, Long userId, Principal principal) {    
        nutritionDayValidator.validateNutritionDayCreation(nutritionDay, userId, principal);
        validateUniqueness(userId, nutritionDay.getDate());
        nutritionDay.setUser(userService.findOne(userId));
        NutritionDay created = nutritionDayRepository.save(nutritionDay);
        mealService.create(created.getMeals());
        return created;
    }

    public void delete(Long userId, Long nutritionDayId, Principal principal) {
        nutritionDayValidator.validateNutritionDayDeletion(userId, nutritionDayId, principal);
        mealService.deleteByNutritionDayId(nutritionDayId);
        nutritionDayRepository.delete(nutritionDayId);
    }

    //UWAGA TEGO NIE BYŁO
    @Transactional
    public NutritionDay update(NutritionDay nutritionDay, Long userId, Long nutritionDayId, Principal principal) {
        
        nutritionDayValidator.validateNutritionDayUpdates(nutritionDay, userId, nutritionDayId, principal);
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
