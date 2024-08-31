package org.mwangi.maya.controller;

import org.mwangi.maya.entities.Employee;
import org.mwangi.maya.services.EmployeeRegistrationService;
import org.mwangi.maya.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRegistrationService employeeRegistrationService;

    public EmployeeController( EmployeeService employeeService, EmployeeRegistrationService employeeRegistrationService) {

        this.employeeService = employeeService;
        this.employeeRegistrationService = employeeRegistrationService;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("employee",new Employee());
        return "register";
    }
    @PostMapping("/perform_registration")
    public  String registerEmployee(@ModelAttribute("employee") Employee employee, BindingResult result){
        if(result.hasErrors()){
            return "register";
        }
        try{
            employeeRegistrationService.registerNewEmployee(employee);
        }catch (RuntimeException e) {
            result.rejectValue("username", "error.user", "An account already exists for this username.");
            return "register";
        }
        return "redirect:/login?registered";

    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("employee",new Employee());
        return "login";
    }

   @GetMapping("/user")
    public String getUserInfo(Model model){
        //Employee employee=employeeService.getEmp();
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String username = authentication.getName();
       Employee employee=employeeService.getOneEmployee(username);
       model.addAttribute("employee", employee);

        return "user";
   }

    @GetMapping("/branch_staff")
    public  String allStuff(Model model){
        List<Employee> employee = employeeService.getEmployee();
        model.addAttribute("employees",employee);
        return  "employee_list";
    }
    @GetMapping("/deleteemployee/{id}")
    public  String deleteEmp(@PathVariable(name = "id") long id){
        employeeService.delete(id);
        return "redirect:/login";
    }
    @GetMapping("/editEmployee/{id}")
    public ModelAndView updateEmployee(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("edit_employee");
        Optional<Employee> optionalEmployee = employeeService.getEmp(id);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            System.out.println(employee);

            mav.addObject("employee", employee);
        } else {
            mav.setViewName("error"); // or any appropriate error view
            mav.addObject("errorMessage", "Employee not found");
        }

        return mav;
    }
  @PostMapping("/saveEmployee")
    public  String saveEmployee(@ModelAttribute Employee employee){
        employeeService.saveEmployee(employee);
        return  "redirect:/login";
  }

}
