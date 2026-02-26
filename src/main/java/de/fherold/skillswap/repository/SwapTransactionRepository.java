package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.SwapTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, Long> {
}
