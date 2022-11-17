package com.li.handler;

import com.alibaba.fastjson.JSON;
import com.li.utils.WebUtils;
import com.li.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResultVO resultVO = new ResultVO(HttpStatus.FORBIDDEN.value(),"您的权限不足",null);
        String json = JSON.toJSONString(resultVO);
        WebUtils.renderString(response,json);
    }
}
