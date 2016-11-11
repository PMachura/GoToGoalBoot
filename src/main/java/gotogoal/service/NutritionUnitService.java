/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.NutritionUnit;
import gotogoal.repository.NutritionUnitRepository;
import gotogoal.rest.resource.NutritionUnitResource;
import gotogoal.rest.resource.assembler.NutritionUnitResourceAssembler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Przemek
 */
@Service
public class NutritionUnitService {

    private NutritionUnitRepository nutritionUnitRepository;
    private NutritionUnitFoodProductService nutritionUnitFoodProductService;
    private NutritionUnitResourceAssembler nutritionUnitResourceAssembler;

    @Autowired
    public NutritionUnitService(NutritionUnitRepository nutritionUnitRepository,
            NutritionUnitFoodProductService nutritionUnitFoodProductService,
            NutritionUnitResourceAssembler nutritionUnitResourceAssembler) {
        this.nutritionUnitRepository = nutritionUnitRepository;
        this.nutritionUnitFoodProductService = nutritionUnitFoodProductService;
        this.nutritionUnitResourceAssembler = nutritionUnitResourceAssembler;
    }

    /*************************************** HELPERS FUNCTION ***************************************/
    
    //DO ZMIANY NA PRYWATNE
    public NutritionUnit findOne(Long id) {
        return nutritionUnitRepository.findOne(id);
    }
    
    private Collection<NutritionUnit> findAll(String userEmail, LocalDate nutritionDayDate){
        return nutritionUnitRepository.findByNutritionDayUserEmailAndNutritionDayDate(userEmail, nutritionDayDate );
    }
    
     private NutritionUnit setNutritionUnitFoodProduct(NutritionUnit nutritionUnit) {
        nutritionUnit.setNutritionUnitsFoodProducts(nutritionUnitFoodProductService.findByNutritionUnitId(nutritionUnit.getId()));
        return nutritionUnit;
    }

    private Collection<NutritionUnit> setNutritionUnitFoodProduct(Collection<NutritionUnit> nutritionUnits) {
        nutritionUnits.forEach((NutritionUnit nutritionUnit) -> {
            setNutritionUnitFoodProduct(nutritionUnit);
        });
        return nutritionUnits;
    }

    private Collection<NutritionUnitResource> mapToResource(Collection<NutritionUnit> nutritionUnits) {
        return Arrays.asList(nutritionUnits.stream()
                .map(nutritionUnitResourceAssembler::toResource)
                .toArray(NutritionUnitResource[]::new));
    }
    /*****************************************************************************************************/
    
    public Collection<NutritionUnit> findAllEager(String userEmail, LocalDate nutritionDayDate){
        Collection<NutritionUnit> nutritionUnits = this.findAll(userEmail, nutritionDayDate);
        nutritionUnits = setNutritionUnitFoodProduct(nutritionUnits);
        return nutritionUnits;
    }
    
    public Collection<NutritionUnitResource> findAllAsResourceEager(String userEmail, LocalDate nutritionDayDate){
        return this.mapToResource(this.findAllEager(userEmail, nutritionDayDate));
    }
    
    

   

    public Collection<NutritionUnit> findAllByNutritionDayEager(Long nutritionDayId) {
        Collection<NutritionUnit> nutritionUnits = nutritionUnitRepository.findByNutritionDayId(nutritionDayId);
        setNutritionUnitFoodProduct(nutritionUnits);
        return nutritionUnits;
    }
    public Collection<NutritionUnitResource> findAllByNutritionDayAsResourceEager(Long nutritionDayId){
        return mapToResource(findAllByNutritionDayEager(nutritionDayId));
    }

    public NutritionUnit findOneWithNutritionUnitsFoodProducts(Long id) {
        NutritionUnit nutritionUnit = this.findOne(id);
        nutritionUnit.setNutritionUnitsFoodProducts(nutritionUnitFoodProductService.findByNutritionUnitId(id));
        return nutritionUnit;
    }

    public NutritionUnit findOneWithEmptyNutritionUnitsFoodProducts(Long id) {
        NutritionUnit nutritionUnit = this.findOne(id);
        nutritionUnit.setNutritionUnitsFoodProducts(null);
        return nutritionUnit;
    }

    public Collection<NutritionUnit> findByNutritionDiaryId(Long dailyNutritionId) {
        return nutritionUnitRepository.findByNutritionDayId(dailyNutritionId);
    }

    public Collection<NutritionUnit> findByNutritionDiaryIdWithNutritionUnitsFoodProducts(Long dailyNutritionId) {
        Collection<NutritionUnit> nutritionUnits = this.findByNutritionDiaryId(dailyNutritionId);
        nutritionUnits.forEach((NutritionUnit nutritionUnit) -> {
            nutritionUnit.setNutritionUnitsFoodProducts(nutritionUnitFoodProductService.findByNutritionUnitId(nutritionUnit.getId()));
        });
        return nutritionUnits;
    }

    public Collection<NutritionUnit> findByNutritionDiaryIdWithEmptyNutritionUnitsFoodProducts(Long dailyNutritionId) {
        Collection<NutritionUnit> nutritionUnits = this.findByNutritionDiaryId(dailyNutritionId);
        nutritionUnits.forEach((NutritionUnit nutritionUnit) -> {
            nutritionUnit.setNutritionUnitsFoodProducts(null);
        });
        return nutritionUnits;
    }

    public NutritionUnit save(NutritionUnit nutritionUnit) {
        return nutritionUnitRepository.save(nutritionUnit);
    }

    public Page<NutritionUnit> testPage(Pageable pageable) {
        Page<NutritionUnit> page = nutritionUnitRepository.findAll(pageable);
        page.getContent().forEach((NutritionUnit nutritionUnit) -> {
            nutritionUnit.setNutritionUnitsFoodProducts(null);
        });

        return page;
    }

    public Collection<NutritionUnit> testDateTime(LocalTime localTime) {
        Collection<NutritionUnit> collection = this.nutritionUnitRepository.findByTimeGreaterThan(localTime);
        collection.forEach((NutritionUnit nutritionUnit) -> {
            nutritionUnit.setNutritionUnitsFoodProducts(null);
        });
        return collection;
    }
}
