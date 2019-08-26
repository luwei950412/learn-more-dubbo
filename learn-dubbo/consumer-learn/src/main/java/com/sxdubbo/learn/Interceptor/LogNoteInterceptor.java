package com.sxdubbo.learn.Interceptor;


import com.sxdubboapi.learn.domain.Log;
import com.sxdubboapi.learn.service.CourseService;
import com.sxdubboapi.learn.service.LogService;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Date;


@Component
public class LogNoteInterceptor implements HandlerInterceptor {
//    private org.apache.commons.logging.Log log1 = LogFactory.getLog(this.getClass());


    @Autowired
    public LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Log log = new Log();
        if(request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")){
            log.setIp(InetAddress.getLocalHost().getHostAddress());
            System.out.println(request.getLocalAddr());
        }else{
            log.setIp(request.getRemoteAddr());
        }
        log.setVisitDate(new Date());
        log.setUrl(request.getRequestURI());
        String url = request.getRequestURI();
        if(url.indexOf("add") !=-1){
            log.setOperation("add");
            log.setOperationId(request.getParameter("id"));
        }else if(url.indexOf("delete") !=-1){
            log.setOperation("delete");
            log.setOperationId(request.getParameter("id"));
        }else if(url.indexOf("update") !=-1 || url.indexOf("change") !=-1){
            log.setOperation("update");
            log.setOperationId(request.getParameter("id"));
        }else{
            log.setOperation("select");
//            log.setOperationId("");
        }
        System.out.println(log);
        if(log == null){
            System.out.println("this log is null");
        }else{
            Log log1 = logService.addLog(log);
            if(log1 == null ){
                System.out.println("this log1 is null");
            }
        }

        System.out.println("记录一条日志记录");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) throws Exception {
//        System.out.println("end"+request.getRemoteAddr());
//        System.out.println(new Date());
    }
}
