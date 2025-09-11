package com.HungMinh.service_identify.repository;

import com.HungMinh.service_identify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Nơi để làm việc với database (CRUD).
@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername (String username);
    // Optional = hộp bọc một giá trị có thể null.
    Optional<User> findByUsername (String useraname);
//    Nếu tìm thấy: Optional.of(user).
//    Nếu không tìm thấy: Optional.empty().
//    Giúp ép bạn xử lý trường hợp không tồn tại thay vì để null.
}
