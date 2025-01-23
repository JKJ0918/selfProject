package com.single.sng.repository;

import com.single.sng.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUsername(String username); // exist 중복검사

    UserEntity findByUsername(String username);

}
