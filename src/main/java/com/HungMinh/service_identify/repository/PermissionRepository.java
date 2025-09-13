package com.HungMinh.service_identify.repository;

import com.HungMinh.service_identify.entity.Permision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permision,String> {
}
