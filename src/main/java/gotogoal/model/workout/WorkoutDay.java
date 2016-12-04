/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.workout;

import gotogoal.config.LocalDateAttributeConverter;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.user.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Przemek
 */

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "date"})
})
public class WorkoutDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date;

    @OneToMany(mappedBy = "workoutDay")
    private Collection<Workout> workouts;

    @ManyToOne
    private User user;
    
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Collection<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(Collection<Workout> workouts) {
        this.workouts = workouts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
