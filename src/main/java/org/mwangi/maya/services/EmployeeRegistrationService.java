package org.mwangi.maya.services;

import org.mwangi.maya.entities.Employee;
import org.mwangi.maya.repositories.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeRegistrationService {
    private EmployeeRepository employeeRepository;
    private PasswordEncoder passwordEncoder;

    public EmployeeRegistrationService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void registerNewEmployee(Employee employee) {
        Employee e= Employee.builder()
                .username(employee.getUsername())
                .password(passwordEncoder.encode(employee.getPassword()))
                .email(employee.getEmail())
                .role(employee.getRole())
                .build();

        // Check if username already exists
        if (employeeRepository.findEmployeeByEmail(e.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }


        employeeRepository.save(e);

    }
}
