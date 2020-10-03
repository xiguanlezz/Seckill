package com.cj.cn.exception;

import com.cj.cn.response.CodeEnum;
import com.cj.cn.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResultResponse exceptionHandler(HttpServletRequest request, Exception e) {
        log.info("全局异常处理器捕获到了异常: {}", e);
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return ResultResponse.error(ex.getCodeEnum());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            String errorMsg = getErrorMsg(errors);
            return ResultResponse.error(CodeEnum.BIND_ERROR.setMsg(errorMsg));
        } else {
//            return Result.error(CodeMsg.SERVER_ERROR);
        }
        return null;
    }

    private String getErrorMsg(List<ObjectError> errors) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < errors.size(); i++) {
            buffer.append(errors.get(i).getDefaultMessage());
            if (i != errors.size() - 1) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
