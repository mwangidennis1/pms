package org.mwangi.maya.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception e){
        ModelAndView modelAndView=new ModelAndView("error");
        modelAndView.addObject("errorMessage","An unexpected error occured: "
        + e.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException e){
        ModelAndView modelAndView=new ModelAndView("error");
        modelAndView.addObject("errorMessage",e.getMessage());
        return modelAndView;
    }
}
