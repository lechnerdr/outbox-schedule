package com.exemplo.richard.outboxschedule.repository;

import com.exemplo.richard.outboxschedule.entity.Teste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteRepository extends JpaRepository<Teste, Long> {
}
