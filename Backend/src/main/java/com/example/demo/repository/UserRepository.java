package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByUserName(String userName);

    @Override
    Optional<UserEntity> findById(Integer integer);


    UserEntity getReferenceById(int id);
    Optional<UserEntity> findByUserNameAndUserPassword(String name, String password);
}
