package com.example.triple_backend_homework.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {

    Account findByUserId(String UserId);

}
