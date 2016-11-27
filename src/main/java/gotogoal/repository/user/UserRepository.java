package gotogoal.repository.user;

import gotogoal.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface UserRepository extends JpaRepository<User,Long> { 
    
    public User findByEmail(String email);
    public void deleteByEmail(String email);
}
