/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

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
    private NutritionDay findOne(Long id) {
        return nutritionDayRepository.findOne(id);
    }

    private NutritionDay findOneByUserEmailAndDate(String userName, LocalDate localDate) {
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

    private NutritionDay setMealsEager(NutritionDay nutritionDay) {
        nutritionDay.setMeals((List<Meal>) mealService.findAllByNutritionDayIdEager(nutritionDay.getId()));
        return nutritionDay;
    }

    private NutritionDay setMealsToNull(NutritionDay nutritionDay) {
        nutritionDay.setMeals(null);
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
        NutritionDay nutritionDay = this.findOneByUserEmailAndDate(userName, localDate);
        Collection<Meal> meals = mealService.findAllByNutritionDayIdLazy(nutritionDay.getId());
        nutritionDay.setMeals((List<Meal>) meals);
        return nutritionDay;
    }

    public NutritionDayResource findOneEagerAsResource(Long id) {
        return nutritionDayResourceAssembler.toResource(this.findOneEager(id));
    }

    public NutritionDayResource findOneByUserEmailAndDateAsResourceEager(String userName, LocalDate localDate) {
        return nutritionDayResourceAssembler.toResource(this.findOneByUserEmailAndDateEager(userName, localDate));
    }

    public Page<NutritionDay> findAllByUserEmailAndDateAsPageLazy(String userEmail, LocalDate localDate, Pageable pageable) {
        Page<NutritionDay> nutritionDayPage = nutritionDayRepository.findByUserEmail(userEmail, pageable);
        setMealsToNull(nutritionDayPage.getContent());
        return nutritionDayPage;
    }

    @Transactional
    public NutritionDay create(NutritionDay nutritionDay, String userEmail) {
        nutritionDay.setUser(userService.findByEmail(userEmail));

        NutritionDay created = nutritionDayRepository.save(nutritionDay);

        System.out.println("Po zapisie dnia");
        System.out.println("NutritionDay->id " + created.getId());
        System.out.println("NutritionDay->date " + created.getDate());
        System.out.println("NutritionDay->userEmail " + created.getUser().getEmail());
        for (Meal meal : nutritionDay.getMeals()) {
            mealService.temporaryDebug("", meal);
        }

        mealService.create(created.getMeals());
        System.out.println("Po zapisie posiłków");
        System.out.println("NutritionDay->id " + created.getId());
        System.out.println("NutritionDay->date " + created.getDate());
        System.out.println("NutritionDay->userEmail " + created.getUser().getEmail());
        for (Meal meal : nutritionDay.getMeals()) {
            mealService.temporaryDebug("", meal);
        }

        return created;

    }

    public void delete(String userEmail, LocalDate date) {
        Long mealId = findOneByUserEmailAndDate(userEmail, date).getId();
        mealService.deleteByNutritionDayId(mealId);
        nutritionDayRepository.delete(mealId);
    }

    public NutritionDay update(NutritionDay nutritionDay) {
        nutritionDay.setUser(userService.findByEmail(nutritionDay.getUser().getEmail()));
        NutritionDay saved = save(nutritionDay);
        System.out.println("Po updacie dnia");
        System.out.println("NutritionDay->id " + saved.getId());
        System.out.println("NutritionDay->date " + saved.getDate());
        System.out.println("NutritionDay->userEmail " + saved.getUser().getEmail());
        for (Meal meal : nutritionDay.getMeals()) {
            mealService.temporaryDebug("", meal);
        }
        mealService.deleteByNutritionDayIdInAndIdNotId(nutritionDay.getId(), getMealsIds(nutritionDay));
        System.out.println("Po usunięciu posiłków dnia");
        System.out.println("NutritionDay->id " + saved.getId());
        System.out.println("NutritionDay->date " + saved.getDate());
        System.out.println("NutritionDay->userEmail " + saved.getUser().getEmail());
        for (Meal meal : nutritionDay.getMeals()) {
            mealService.temporaryDebug("", meal);
        }

        
        mealService.update(nutritionDay.getMeals());
        System.out.println("Po updacie posiłków");
        System.out.println("NutritionDay->id " + saved.getId());
        System.out.println("NutritionDay->date " + saved.getDate());
        System.out.println("NutritionDay->userEmail " + saved.getUser().getEmail());
        for (Meal meal : nutritionDay.getMeals()) {
            mealService.temporaryDebug("", meal);
        }
        return saved;
    }
}
