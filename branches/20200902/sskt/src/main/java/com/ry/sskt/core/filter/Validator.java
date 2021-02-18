package com.ry.sskt.core.filter;

import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.common.constant.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author xrq
 * decription  Controller 参数验证
 * date 2020/4/14
 */
@Aspect
@Component
public class Validator {

    @Around("execution(* com.ry.*.controller..*(..)) && args(..,result)" )
    public Object doAround(ProceedingJoinPoint pjp, BindingResult result) throws Throwable {
        Object retVal;
        if (result.hasErrors()) {
            retVal = doErrorHandle(result);
        } else {
            retVal = pjp.proceed();
        }
        return retVal;
    }


    /**
     * 处理错误信息
     *
     * @param result 参数校验结果
     * @return 结果
     */
    private Object doErrorHandle(BindingResult result) {
        List<FieldError> err = result.getFieldErrors();
        FieldError fe;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < err.size(); i++) {
            fe = err.get(i);
            buffer.append(fe.getDefaultMessage() + " ,");
        }
        return new ResponseBase(Result.PUB_VALID_ERROR.getResultType(), buffer.substring(0, buffer.length() - 1));
    }


}
