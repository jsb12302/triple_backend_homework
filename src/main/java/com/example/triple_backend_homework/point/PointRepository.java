package com.example.triple_backend_homework.point;

import com.example.triple_backend_homework.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point,Long> {

    Point findByAccount(Account account);



}
