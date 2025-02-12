package com.practiceJWT.practiceJWT.Service;


import com.practiceJWT.practiceJWT.Entity.Employee;
import com.practiceJWT.practiceJWT.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getEmployee(){
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee){
        Employee exitingEmployee = employeeRepository.findById(id).orElseThrow();

        exitingEmployee.setName(employee.getName());
        exitingEmployee.setEmail(employee.getEmail());
        exitingEmployee.setJobTitle(employee.getJobTitle());
        exitingEmployee.setSalary(employee.getSalary());

        return employeeRepository.save(exitingEmployee);
    }

    public void deleteEmployee(Long id ){
        employeeRepository.deleteById(id);
    }


}
