package com.HungMinh.service_identify.repository;

import com.HungMinh.service_identify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// Nơi để làm việc với database (CRUD).
@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername (String username);

}
