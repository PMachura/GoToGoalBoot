/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gotogoal.config.LocalDateAttributeConverter;
import gotogoal.config.LocalDateTimeAttributeConverter;
import gotogoal.config.LocalTimeAttributeConverter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Przemek
 */
@Entity
public class NutritionUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = LocalTimeAttributeConverter.class)
    private LocalTime time;

    @ManyToOne
    private NutritionDay nutritionDay;

    @OneToMany(mappedBy = "nutritionUnit")
    private List<NutritionUnitFoodProduct> nutritionUnitsFoodProducts;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime localTime) {
        this.time = localTime;
    }

    public NutritionDay getNutritionDay() {
        return nutritionDay;
    }

    public void setNutritionDay(NutritionDay nutritionDay) {
        this.nutritionDay = nutritionDay;
    }

    public List<NutritionUnitFoodProduct> getNutritionUnitsFoodProducts() {
        return nutritionUnitsFoodProducts;
    }

    public void setNutritionUnitsFoodProducts(List<NutritionUnitFoodProduct> nutritionUnitsFoodProducts) {
        this.nutritionUnitsFoodProducts = nutritionUnitsFoodProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
