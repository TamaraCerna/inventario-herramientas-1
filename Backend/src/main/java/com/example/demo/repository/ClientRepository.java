package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.enums.StateClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    List<ClientEntity> findByClientState(StateClient state);

    List<ClientEntity> findAll();

}
