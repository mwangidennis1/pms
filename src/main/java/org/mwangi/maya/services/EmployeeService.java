package org.mwangi.maya.services;

import org.mwangi.maya.entities.Employee;
import org.mwangi.maya.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    private PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> getEmployee(){
        return employeeRepository.findAll();
    }
    public Employee getOneEmployee(String name){
        return employeeRepository.getEmployeeIgoreCase(name);
    }
    public void updateResetPasswordToken(String token,String email){
        Employee employee=employeeRepository.findByEmail(email);
        if(employee != null){
            employee.setResetPasswordToken(token);
            employeeRepository.save(employee);
        }else {
            //custom exception
            throw  new UsernameNotFoundException("could find the nigga");
        }
    }
    public  Employee getByResetPasswordToken(String token){
        return  employeeRepository.findByResetPasswordToken(token);
    }

    public  void updatePassword(Employee employee,String newPassword){
        employee.setPassword(passwordEncoder.encode(newPassword));
        employee.setResetPasswordToken(null);
        employeeRepository.save(employee);
    }
    public Optional<Employee> getEmp(Long id){
        return
                employeeRepository.findById(id);
    }
    public  void delete(long id){
        employeeRepository.deleteById(id);
    }
    public Employee editEmployee(Employee newEmployee,long id){
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setUsername(newEmployee.getUsername());
                    employee.setPassword(newEmployee.getPassword());
                    employee.setEmail(newEmployee.getEmail());
                    employee.setRole(newEmployee.getRole());
                    return  employeeRepository.save(employee);
                }).orElseGet(
                        ()->{
                            newEmployee.setId(id);
                           return employeeRepository.save(newEmployee);
                        }
                );
    }
  public  void saveEmployee(Employee employee){
        String password =employee.getPassword();
        String encodeP=passwordEncoder.encode(password);
        employee.setPassword(encodeP);
        employeeRepository.save(employee);
  }

}
