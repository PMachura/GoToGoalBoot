/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 *
 * @author Przemek
 */
@Entity
public class FoodProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 20)
    private String name;

    @Min(value = 0)
    @Max(value = 900)
    private Float calories;

    @Min(value = 0)
    @Max(value = 100)
    private Float proteins;

    @Min(value = 0)
    @Max(value = 100)
    private Float carbohydrates;

    @Min(value = 0)
    @Max(value = 100)
    private Float fats;

    @Enumerated(EnumType.STRING)
    private FoodProductCategory category;

    @JsonIgnore
    @OneToMany(mappedBy = "foodProduct")
    private List<NutritionUnitFoodProduct> nutritionUnitFoodProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Float getProteins() {
        return proteins;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Float getFats() {
        return fats;
    }

    public void setFats(Float fats) {
        this.fats = fats;
    }

    public List<NutritionUnitFoodProduct> getNutritionUnitFoodProduct() {
        return nutritionUnitFoodProduct;
    }

    public void setNutritionUnitFoodProduct(List<NutritionUnitFoodProduct> nutritionUnitFoodProduct) {
        this.nutritionUnitFoodProduct = nutritionUnitFoodProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodProductCategory getCategory() {
        return category;
    }

    public void setCategory(FoodProductCategory category) {
        this.category = category;
    }

}
