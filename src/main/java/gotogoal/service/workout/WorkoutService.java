/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.workout;

import gotogoal.model.nutrition.Meal;
import gotogoal.model.workout.Workout;
import gotogoal.model.workout.WorkoutExercise;
import gotogoal.repository.workout.WorkoutExerciseRepository;
import gotogoal.repository.workout.WorkoutRepository;
import gotogoal.rest.resource.assembler.workout.WorkoutResourceAssembler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
public class WorkoutService {

    private WorkoutRepository workoutRepository;
    private WorkoutExerciseService workoutExerciseService;
    private WorkoutResourceAssembler workoutResourceAssembler;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, WorkoutResourceAssembler workoutResourceAssembler) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.workoutResourceAssembler = workoutResourceAssembler;
    }

    private Collection<Workout> findByUserIdAndWorkoutDayId(Long userId, Long workoutDayId) {
        return workoutRepository.findByWorkoutDayUserIdAndWorkoutDayId(userId, workoutDayId);
    }

    private Workout setWorkoutExercises(Workout workout) {
        workout.setWorkoutExercises(workoutExerciseService.findByWorkoutId(workout.getId()));
        return workout;
    }

    private Collection<Workout> setWorkoutExercises(Collection<Workout> workouts) {
        workouts.forEach((Workout workout) -> {
            setWorkoutExercises(workout);
        });
        return workouts;
    }

    private Collection<Workout> setWorkoutExercisesToNull(Collection<Workout> workouts) {
        workouts.forEach((Workout workout) -> {
            workout.setWorkoutExercises(null);
        });
        return workouts;
    }

    private Workout setWorkoutInWorkoutExercises(Workout workout) {
        workout.getWorkoutExercises().forEach((WorkoutExercise workoutExercise) -> {
            workoutExercise.setWorkout(workout);
        });
        return workout;
    }

    private Collection<Long> getWorkoutExercisesIds(Workout workout) {
        return workout.getWorkoutExercises()
                .stream()
                .map((WorkoutExercise workoutExercise) -> workoutExercise.getId())
                .filter(out -> out != null)
                .collect(Collectors.toList());
    }

    public Collection<Workout> findByUserIdAndWorkoutDayIdEager(Long userId, Long nutritionDayId) {
        Collection<Workout> workouts = findByUserIdAndWorkoutDayId(userId, nutritionDayId);
        workouts = setWorkoutExercises(workouts);
        return workouts;
    }

    public Collection<Workout> findByWorkoutDayIdLazy(Long workoutDayId) {
        Collection<Workout> workouts = workoutRepository.findByWorkoutDayId(workoutDayId);
        return setWorkoutExercisesToNull(workouts);
    }

    public void deleteByWorkoutDayId(Long workoutDayId) {
        workoutExerciseService.deleteByWorkoutWorkoutDayId(workoutDayId);
        workoutRepository.deleteByWorkoutDayId(workoutDayId);
    }

    @Transactional
    public Workout create(Workout workout) {
        workout = setWorkoutInWorkoutExercises(workout);
        Workout created = workoutRepository.save(workout);
        workoutExerciseService.save(created.getWorkoutExercises());
        return created;
    }

    @Transactional
    Collection<Workout> create(Collection<Workout> workouts) {
        Collection<Workout> created = new ArrayList<Workout>();
        workouts.forEach((Workout workout) -> created.add(this.create(workout)));
        return created;
    }

    Collection<Workout> findByWorkoutDayIdEager(Long workoutDayId) {
        Collection<Workout> workouts = workoutRepository.findByWorkoutDayId(workoutDayId);
        setWorkoutExercises(workouts);
        return workouts;

    }

    @Transactional
    public void deleteByWorkoutDayIdInAndIdNotIn(Long workoutDayId, Collection<Long> workoutsIds) {
        workoutExerciseService.deleteByWorkoutWorkoutDayIdInAndWorkoutIdNotIn(workoutDayId, workoutsIds);
        workoutRepository.deleteByWorkoutDayIdInAndIdNotIn(workoutDayId, workoutsIds);
    }

    @Transactional
    public Workout update(Workout workout) {
        workout = setWorkoutInWorkoutExercises(workout);
        Workout updated = workoutRepository.save(workout);
        Collection<WorkoutExercise> savedWorkoutExercises = workoutExerciseService.save(workout.getWorkoutExercises());
        updated.setWorkoutExercises(savedWorkoutExercises);
        if (getWorkoutExercisesIds(workout).isEmpty()) {
            workoutExerciseService.deleteByWorkoutId(workout.getId());
        } else {
            workoutExerciseService.deleteByWorkoutIdInAndIdNotIn(workout.getId(), getWorkoutExercisesIds(workout));
        }

        return updated;
    }

    @Transactional
    public Collection<Workout> update(Collection<Workout> workouts) {
        Collection<Workout> created = new ArrayList<Workout>();
        workouts.forEach((Workout workout) -> created.add(update(workout)));
        return created;
    }

    public Workout findOneEager(Long id) {
        return setWorkoutExercises(workoutRepository.findOne(id)); 
   }

}
