package org.mwangi.maya;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mwangi.maya.entities.Employee;
import org.mwangi.maya.entities.Role;
import org.mwangi.maya.repositories.EmployeeRepository;
import org.mwangi.maya.services.EmployeeService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmployee() {
        List<Employee> employees = List.of(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getEmployee();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getOneEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.getEmployeeIgoreCase("testName")).thenReturn(employee);

        Employee result = employeeService.getOneEmployee("testName");

        assertEquals(employee, result);
        verify(employeeRepository, times(1)).getEmployeeIgoreCase("testName");
    }

    @Test
    void updateResetPasswordToken() {
        Employee employee = new Employee();
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(employee);

        employeeService.updateResetPasswordToken("testToken", "test@example.com");

        assertEquals("testToken", employee.getResetPasswordToken());
        verify(employeeRepository, times(1)).findByEmail("test@example.com");
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void updateResetPasswordToken_ThrowsException() {
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                employeeService.updateResetPasswordToken("testToken", "test@example.com")
        );

        assertEquals("could find the nigga", exception.getMessage());
        verify(employeeRepository, times(1)).findByEmail("test@example.com");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getByResetPasswordToken() {
        Employee employee = new Employee();
        when(employeeRepository.findByResetPasswordToken("testToken")).thenReturn(employee);

        Employee result = employeeService.getByResetPasswordToken("testToken");

        assertEquals(employee, result);
        verify(employeeRepository, times(1)).findByResetPasswordToken("testToken");
    }

    @Test
    void updatePassword() {
        Employee employee = new Employee();
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        employeeService.updatePassword(employee, "newPassword");

        assertEquals("encodedPassword", employee.getPassword());
        assertNull(employee.getResetPasswordToken());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getEmp() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmp(1L);

        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void delete() {
        employeeService.delete(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void editEmployee() {
        Employee existingEmployee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        Employee newEmployee = new Employee();
        newEmployee.setUsername("newUsername");
        newEmployee.setPassword("newPassword");
        newEmployee.setEmail("newEmail");
        newEmployee.setRole(Role.valueOf("ROLE_STAFF"));

        Employee result = employeeService.editEmployee(newEmployee, 1L);

        assertEquals(existingEmployee, result);
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void saveEmployee() {
        Employee employee = new Employee();
        employee.setPassword("rawPassword");
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        employeeService.saveEmployee(employee);

        assertEquals("encodedPassword", employee.getPassword());
        verify(employeeRepository, times(1)).save(employee);
    }
}
