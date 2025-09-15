package com.example.demo.repository;

import com.example.demo.entity.TariffEntity;
import com.example.demo.entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TariffRepository extends JpaRepository<TariffEntity, Integer> {
    List<TariffEntity> findAll();
}
