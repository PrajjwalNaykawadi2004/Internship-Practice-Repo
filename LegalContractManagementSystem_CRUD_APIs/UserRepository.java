package com.legalcontract.legal_contract_management_system.repository;

import com.legalcontract.legal_contract_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{

}