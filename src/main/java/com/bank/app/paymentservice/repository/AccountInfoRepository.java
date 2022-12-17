package com.bank.app.paymentservice.repository;

import com.bank.app.paymentservice.models.entities.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {

    @Query("SELECT CASE WHEN (COUNT(ac) > 0) THEN TRUE ELSE FALSE END FROM AccountInfo AS ac WHERE ac.account = :account")
    Boolean isExistByAccount(@Param("account") String account);

    @Query("SELECT ac FROM AccountInfo AS ac WHERE ac.account = :account")
    AccountInfo findByAccount(@Param("account") String account);

}
