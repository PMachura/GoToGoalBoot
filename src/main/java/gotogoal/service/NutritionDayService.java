/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.NutritionDay;
import gotogoal.model.NutritionUnit;
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

/**
 *
 * @author Przemek
 */
@Service
public class NutritionDayService {

    private NutritionDayRepository nutritionDayRepository;
    private NutritionUnitService nutritionUnitService;
    private NutritionDayResourceAssembler nutritionDayResourceAssembler;

    @Autowired
    public NutritionDayService(NutritionDayRepository nutritionDayRepository,
            NutritionUnitService nutritionUnitService,
            NutritionDayResourceAssembler nutritionDayResourceAssembler) {
        this.nutritionDayRepository = nutritionDayRepository;
        this.nutritionDayResourceAssembler = nutritionDayResourceAssembler;
        this.nutritionUnitService = nutritionUnitService;
    }

    private NutritionDay findOne(Long id) {
        return nutritionDayRepository.findOne(id);
    }

    private NutritionDay findOne(String userName, LocalDate localDate) {
        return nutritionDayRepository.findByUserEmailAndDate(userName, localDate);
    }

    private Collection<NutritionDay> findAll() {
        return nutritionDayRepository.findAll();
    }

    public NutritionDay findOneEager(Long id) {
        NutritionDay dailyNutrition = this.findOne(id);
        Collection<NutritionUnit> nutritionUnits = nutritionUnitService.findByNutritionDiaryId(id);
        dailyNutrition.setNutritionUnits((List<NutritionUnit>) nutritionUnits);
        return dailyNutrition;
    }

    public NutritionDay findOneEager(String userName, LocalDate localDate) {
        NutritionDay dailyNutrition = this.findOne(userName, localDate);
        Collection<NutritionUnit> nutritionUnits = nutritionUnitService.findByNutritionDiaryId(dailyNutrition.getId());
        dailyNutrition.setNutritionUnits((List<NutritionUnit>) nutritionUnits);
        return dailyNutrition;
    }

    public NutritionDayResource findOneEagerAsResource(Long id) {
        return nutritionDayResourceAssembler.toResource(this.findOneEager(id));
    }

    public NutritionDayResource findOneEagerAsResource(String userName, LocalDate localDate) {
        return nutritionDayResourceAssembler.toResource(this.findOneEager(userName, localDate));
    }
    
    public Page<NutritionDay> findAllLazy(String userEmail, LocalDate localDate, Pageable pageable){
        Page<NutritionDay> nutritionDayPage = nutritionDayRepository.findByUserEmail(userEmail, pageable);
        nutritionDayPage.getContent().forEach((NutritionDay nutritionDay)->{
            nutritionDay.setNutritionUnits(null);
        });
        return nutritionDayPage;
    }
    
    //TESTOWY
    public Page<NutritionDay> findAllEagerAsResourceDateLess(LocalDate localDate, Pageable pageable){
        Page<NutritionDay> nutritionDayPage = nutritionDayRepository.findByDateLessThanEqual(localDate, pageable);
        nutritionDayPage.getContent().forEach((NutritionDay nutritionDay)->{
            nutritionDay.setNutritionUnits(null);
        });
        return nutritionDayPage;
    }

    public Collection<NutritionDay> findAllWithEmpyNutritionUnits() {
        Collection<NutritionDay> nutritionDiaries = this.findAll();
        nutritionDiaries.forEach((NutritionDay nutritionDiary) -> {
            nutritionDiary.setNutritionUnits(null);
        });
        return nutritionDiaries;
    }

    public Collection<NutritionDayResource> findAllAsResourceWithEmpyNutritionUnits() {
        return this.mapToResource(this.findAllWithEmpyNutritionUnits());
    }

    public Collection<NutritionDayResource> findAllAsResourceWithNutritionUnits() {
        return this.mapToResource(this.findAllWithNutritionUnits());
    }

    public Collection<NutritionDayResource> mapToResource(Collection<NutritionDay> collection) {
        return Arrays.asList(collection.stream().map(nutritionDayResourceAssembler::toResource)
                .toArray(NutritionDayResource[]::new));
    }

    public Collection<NutritionDay> findAllWithNutritionUnits() {
        Collection<NutritionDay> nutritionDiaries = this.findAll();
        nutritionDiaries.forEach((NutritionDay nutritionDiary) -> {
            nutritionDiary.setNutritionUnits((List<NutritionUnit>) nutritionUnitService.findByNutritionDiaryId(nutritionDiary.getId()));
        });
        return nutritionDiaries;

    }

    public NutritionDayResource findAllByDateGreaterThan(LocalDate localDate, Pageable pageable) {
        Page<NutritionDay> nutritionDayPage = this.nutritionDayRepository.findByDateGreaterThanEqual(localDate, pageable);
        if (nutritionDayPage == null) {
            System.out.println("@@@@ PAGE NULL");
        } else {
            System.out.println("@@@@ PAGE NOT NULL");
        }
        nutritionDayPage.getContent().forEach((NutritionDay nutritionDay) -> {
            System.out.println("@@@@ ID  " + nutritionDay.getId());
            nutritionDay.setNutritionUnits(null);
        });
        if (nutritionDayResourceAssembler == null) {
            System.out.println("@@@@ Assembler null");
        }
        return nutritionDayResourceAssembler.toResource(localDate, nutritionDayPage);
    }

    /********************************* SAVE ********************************************/
    
    public NutritionDay save(NutritionDay nutritionDay) {
        return nutritionDayRepository.save(nutritionDay);
    }
    
    
    /********************************* UPDATE ********************************************/
    
    public NutritionDay update(NutritionDay nutritionDay) {
        return nutritionDayRepository.save(nutritionDay);
    }
    
    
    /********************************* DELETE ********************************************/
    public void delete(NutritionDay nutritionDay){
        nutritionDayRepository.delete(nutritionDay);
    }
    public void delete(Long id){
        nutritionDayRepository.delete(id);
    }

}
