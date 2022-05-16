package com.ds.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    /**
     * 例外処理
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler({Exception.class})
    public ModelAndView handlerExceprion(HttpServletRequest request, Exception ex) throws Exception{

        logger.error("Request URL : {} , Exception : {}",request.getRequestURL(),ex.getMessage());

        if(AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null){
            throw ex;
        }

        var mav = new ModelAndView();
        mav.addObject("url",request.getRequestURL());
        mav.addObject("exception",ex);
        mav.setViewName("error/error");

        return mav;

    }
}
