/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.NutritionDay;
import gotogoal.model.Meal;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gotogoal.rest.resource.NutritionDayResource;
import gotogoal.rest.resource.assembler.NutritionDayResourceAssembler;
import java.util.Arrays;
import gotogoal.repository.NutritionDayRepository;
import java.time.LocalDate;
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

    /************************************* HELPERS FUNCTION ******************************/
    
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
    
    private NutritionDay setMeals(NutritionDay nutritionDay){
        nutritionDay.setMeals((List<Meal>) mealService.findAllByNutritionDayIdLazy(nutritionDay.getId()));
        return nutritionDay;
    }
    
    private NutritionDay setMealsToNull(NutritionDay nutritionDay){
       nutritionDay.setMeals(null);
       return nutritionDay;
    }
    
    private Collection<NutritionDay> setMealsToNull(Collection<NutritionDay> nutritionDays){
        nutritionDays.forEach((NutritionDay nutritionDay) -> {
            nutritionDay.setMeals(null);
        });
        return nutritionDays;
    }
    
    /********************************************************************************************/

    public NutritionDay findOneEager(Long id) {
        NutritionDay nutritionDay = this.findOne(id);
        Collection<Meal> meals = mealService.findAllByNutritionDayIdLazy(id);
        nutritionDay.setMeals((List<Meal>) meals);
        return nutritionDay;
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
    public NutritionDay create(NutritionDay nutritionDay, String userEmail){
        nutritionDay.setUser(userService.findByEmail(userEmail));
        
        NutritionDay created = nutritionDayRepository.save(nutritionDay);
        
        System.out.println("Po zapisie dnia");
        System.out.println("NutritionDay->id " + created.getId());
        System.out.println("NutritionDay->date " + created.getDate());
        System.out.println("NutritionDay->userEmail " + created.getUser().getEmail());
        for(Meal meal : nutritionDay.getMeals()){
            mealService.temporaryDebug("", meal);
        }
        
        mealService.create(created.getMeals());
        System.out.println("Po zapisie posiłków");
        System.out.println("NutritionDay->id " + created.getId());
        System.out.println("NutritionDay->date " + created.getDate());
        System.out.println("NutritionDay->userEmail " + created.getUser().getEmail());
        for(Meal meal : nutritionDay.getMeals()){
            mealService.temporaryDebug("", meal);
        }
        
        return created;
             
    }
    
    public void delete(Long mealId){
        mealService.deleteByNutritionDayId(mealId);
        nutritionDayRepository.delete(mealId);
    }
}
