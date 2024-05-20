package com.exemplo.richard.outboxschedule.repository;

import com.exemplo.richard.outboxschedule.entity.TesteOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteOutboxRepository extends JpaRepository<TesteOutbox, Long> {
}
