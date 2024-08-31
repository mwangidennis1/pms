package org.mwangi.maya.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.mwangi.maya.entities.Employee;
import org.mwangi.maya.services.EmployeeService;
import org.mwangi.maya.services.GmailNotification;
import org.mwangi.maya.utility.Utility;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.apache.commons.lang3.RandomStringUtils;
@Controller
public class ForgotPasswordController {
    private GmailNotification gmailNotification;
    private EmployeeService employeeService;

    public ForgotPasswordController(GmailNotification gmailNotification, EmployeeService employeeService) {
        this.gmailNotification = gmailNotification;
        this.employeeService = employeeService;
    }

    @GetMapping("/forgot_password")
    public String forgotPassi(){
        return "forgot_password_form";
    }
    @PostMapping("/forgot_password")
    public String processPassi(HttpServletRequest request, Model model){
        String email=request.getParameter("email");
        //System.out.println(email);
        String token=RandomStringUtils.randomAlphanumeric(30);
        //System.out.println(token);
        employeeService.updateResetPasswordToken(token,email);
        String resetPasswordLink= Utility.getSiteURL(request) + "/reset_password?token=" + token;
        gmailNotification.sendEmail(email,resetPasswordLink);
        model.addAttribute("message","We have sent a reset password link to your email. Please check");
        return "forgot_password_form";
    }

    @GetMapping("/reset_password")
    public  String  resetPassi(@Param(value = "token") String token,Model model){
        Employee employee=employeeService.getByResetPasswordToken(token);
        //System.out.println(employee);
        model.addAttribute("token",token);
        if(employee ==null){
            model.addAttribute("message","Invalid Token");
            return "message";
        }
        return "reset_password_form";
    }
    @PostMapping("/reset_password")
    public String processResetPassi(HttpServletRequest request,Model model){
        String token = request.getParameter("token");
        String password=request.getParameter("password");
        Employee employee=employeeService.getByResetPasswordToken(token);
        System.out.println(token);
        System.out.println(password);
        System.out.println(employee);
        model.addAttribute("title","Reset your password");
        if(employee == null){
            model.addAttribute("message","Invalid Token");
            return  "message";
        }else {
            employeeService.updatePassword(employee,password);
            model.addAttribute("message","you have successfully changed your password");
        }
        return "redirect:/login";
    }

}
