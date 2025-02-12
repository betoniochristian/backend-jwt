package com.practiceJWT.practiceJWT.Repository;


import com.practiceJWT.practiceJWT.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long> {
}
