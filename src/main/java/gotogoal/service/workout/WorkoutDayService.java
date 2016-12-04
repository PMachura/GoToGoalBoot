/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.workout;

import gotogoal.model.workout.Workout;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.repository.nutrition.NutritionDayRepository;
import gotogoal.repository.workout.WorkoutDayRepository;
import gotogoal.service.user.UserService;
import gotogoal.validator.WorkoutDayValidator;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
public class WorkoutDayService {
    
    WorkoutDayRepository workoutDayRepository;
    WorkoutService workoutService;
    UserService userService;
    WorkoutDayValidator workoutDayValidator;

    public WorkoutDayService(WorkoutDayRepository workoutDayRepository, WorkoutService workoutService, UserService userService, WorkoutDayValidator workoutDayValidator) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutService = workoutService;
        this.userService = userService;
        this.workoutDayValidator = workoutDayValidator;
    }
    
    private WorkoutDay setWorkoutsLazy(WorkoutDay workoutDay) {
        workoutDay.setWorkouts(workoutService.findByWorkoutDayIdLazy(workoutDay.getId()));
        return workoutDay;
    }
    
    private Collection<WorkoutDay> setWorkoutsLazy(Collection<WorkoutDay> workoutDays) {
        workoutDays.forEach((WorkoutDay workoutDay) -> setWorkoutsLazy(workoutDay));
        return workoutDays;
    }
    
    private WorkoutDay setWorkoutsEager(WorkoutDay workoutDay) {
        workoutDay.setWorkouts(workoutService.findByWorkoutDayIdEager(workoutDay.getId()));
        return workoutDay;
    }
    
     private Collection<Long> getWorkoutsIds(WorkoutDay workoutDay) {
        return workoutDay.getWorkouts()
                .stream()
                .map((Workout workout) -> workout.getId())
                .filter(out -> out != null)
                .collect(Collectors.toList());
    }
    
    public Page<WorkoutDay> findByUserIdEager(Long userId, Pageable pageable){
        Page<WorkoutDay> workoutDayPage = workoutDayRepository.findByUserId(userId, pageable);
        setWorkoutsLazy(workoutDayPage.getContent());
        return workoutDayPage;
    }
    
    public Page<WorkoutDay> findByUserIdAndDateLessThanEqualEager(Long userId,LocalDate date, Pageable pageable){
        Page<WorkoutDay> workoutDayPage = workoutDayRepository.findByUserIdAndDateLessThanEqual(userId, date, pageable);
        setWorkoutsLazy(workoutDayPage.getContent());
        return workoutDayPage;
    }
    
    public WorkoutDay findOneEagerDeep(Long id){
        return setWorkoutsEager(workoutDayRepository.findOne(id));
    }
    
    @Transactional
    public void delete(Long userId, Long workoutDayId, Principal principal){
        workoutDayValidator.validateWorkouDayDeletion(userId, workoutDayId, principal);
        workoutService.deleteByWorkoutDayId(workoutDayId);
        workoutDayRepository.delete(workoutDayId);
    }
    
    @Transactional
    public WorkoutDay create(WorkoutDay workoutDay, Long userId, Principal principal){
        workoutDayValidator.validateWorkoutDayCreation(workoutDay, userId, principal);
        workoutDay.setUser(userService.findOne(userId));
        WorkoutDay created = workoutDayRepository.save(workoutDay);
        workoutService.create(created.getWorkouts());
        return created;
    }
    
    @Transactional 
    public WorkoutDay update(WorkoutDay workoutDay, Long userId, Long workoutDayId, Principal principal){
        workoutDayValidator.validateWorkoutDayUpdates(workoutDay, userId, workoutDayId, principal);
        workoutDay.setUser(userService.findOne(userId));
        WorkoutDay saved = workoutDayRepository.save(workoutDay);
        if(getWorkoutsIds(workoutDay).isEmpty()){
            workoutService.deleteByWorkoutDayId(saved.getId());
        } else {
            workoutService.deleteByWorkoutDayIdInAndIdNotIn(saved.getId(), getWorkoutsIds(workoutDay));
        }
        workoutService.update(workoutDay.getWorkouts());
        return saved;
        
    }
    
    
}
