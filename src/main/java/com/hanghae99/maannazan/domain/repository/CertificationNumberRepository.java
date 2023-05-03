package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.CertificationNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationNumberRepository extends JpaRepository<CertificationNumber, Long> {
    Optional<CertificationNumber> findByStr(String str);
}
