package com.ry.sskt.core.filter;

import com.ry.sskt.model.common.constant.Result;
import com.ry.sskt.service.IRespCode;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 自定义异常
 */
@Data
public class GlobalException extends RuntimeException {

    /**
     * 错误代码
     */
    private int errorCode;

    /**
     * 错误代码接口
     */
    private IRespCode respCode;


    public GlobalException() {
        super();
        errorCode = Result.PUB_FAIL.getResultType();
        respCode = Result.PUB_FAIL;
    }


    /**
     * 添加构造方法
     *
     * @param respCode 错误代码枚举类型
     */
    public GlobalException(IRespCode respCode) {
        super(respCode.getMessage());
        this.errorCode = respCode.getResultType();
        this.respCode = respCode;
    }

    /**
     * 构造方法
     *
     * @param message  默认消息
     */
    public GlobalException(String message) {
        super(message);
        this.errorCode = respCode.getResultType();
        this.respCode = respCode;
    }

    /**
     * 构造方法
     *
     * @param respCode 错误代码
     * @param message  默认消息
     */
    public GlobalException(IRespCode respCode, String message) {
        super(!StringUtils.isEmpty(message) ? message : respCode.getMessage());
        this.respCode = respCode;
        this.errorCode = respCode.getResultType();
    }


    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder(getClass().getName());
        messageBuilder.append(":");
        if (respCode == null) {
            messageBuilder.append(this.errorCode);
        } else {
            messageBuilder.append(respCode.getResultType()).append(":").append(respCode.name());
        }
        String message = getLocalizedMessage();
        return !StringUtils.isEmpty(message) ? messageBuilder.append(":").append(message).toString() : messageBuilder
                .toString();
    }

    public String getMessage() {
       return this.respCode.getMessage();
    }
}
