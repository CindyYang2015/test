package com.cloudwalk.common.base.token;
import java.lang.reflect.Method;
/**
 * Project Name:wams-service
 * File Name:TokenInterceptor.java
 * Package Name:com.cloudwalk.core.shire
 * Date:2016年4月1日下午2:48:08
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
*/
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * ClassName:TokenInterceptor <br/>
 * Description: TODO Description. <br/>
 * Date:     2016年4月1日 下午2:48:08 <br/>
 * @author   Chunjie He
 * @version  
 * @since    JDK 1.6	 
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
            Object handler) throws Exception {
        
        if (handler instanceof HandlerMethod) {
            
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Token annotation = method.getAnnotation(Token.class);
            
            if (annotation != null ) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                    request.getSession( false ).setAttribute( "token" , UUID.randomUUID().toString());
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        return false ;
                    }
                    request.getSession( false ).removeAttribute( "token" );
                }
            }
            return true ;
        } else {
            return super .preHandle(request, response, handler);
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession( false ).getAttribute( "token" );
        if (serverToken == null ) {
            return true ;
        }
        
        String clinetToken = request.getParameter( "token" );
        if (clinetToken == null ) {
            return true ;
        }
        
        if (!serverToken.equals(clinetToken)) {
            return true ;
        }
        
        return false ;
    }
}

