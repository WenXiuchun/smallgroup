package com.cc.smallgroup.repository;
import com.cc.smallgroup.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Probability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRespository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findById(String id);
}
