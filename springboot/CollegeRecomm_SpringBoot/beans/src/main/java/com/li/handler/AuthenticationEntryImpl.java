package com.li.handler;

import com.alibaba.fastjson.JSON;
import com.li.utils.WebUtils;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String msg = e.getMessage();
        ResultVO resultVO;
        System.out.println("msg:"+msg);
        if ("Bad credentials".equals(msg) ){
            msg = "密码错误";
            resultVO = new ResultVO(ResStatus.NO,msg,null);
        }else if ("Not Found".equals(msg)){
            msg = "用户名不存在";
            resultVO = new ResultVO(ResStatus.EXIST,msg,null);
        }else {
            msg = "用户认证失败";
            resultVO = new ResultVO(HttpStatus.UNAUTHORIZED.value(),msg,null);
        }
        String json = JSON.toJSONString(resultVO);
        //处理异常
        WebUtils.renderString(response,json);
    }
}
