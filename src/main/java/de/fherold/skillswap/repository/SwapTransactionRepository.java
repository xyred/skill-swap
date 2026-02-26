package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.SwapTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, Long> {
    List<SwapTransaction> findByStudentId(Long studentId);

    List<SwapTransaction> findByProviderId(Long providerId);
}
