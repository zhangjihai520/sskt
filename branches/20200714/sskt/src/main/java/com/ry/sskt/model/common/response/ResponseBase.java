package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.constant.Result;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 返回实体对象基类
 */
@Data
@Accessors(chain = true)
public class ResponseBase<T> {
    @JSONField(name = "ResultType")
    private int resultType;
    @JSONField(name = "Message")
    private String message;
    @JSONField(name = "ReturnEntity")
    private T returnEntity;//对象


    /**
     * 操作成功 构造函数
     */
    public ResponseBase() {
        this.resultType = Result.PUB_SUCCESS.getResultType();
        this.message = Result.PUB_SUCCESS.getMessage();
    }

    /**
     * 自定义码 构造函数
     *
     * @param ResultType
     * @param Message
     */
    public ResponseBase(Integer ResultType, String Message) {
        this.resultType = ResultType;
        this.message = Message;
    }

    /**
     * 异常
     *
     * @param ex
     */
    public ResponseBase(Exception ex) {
        this.resultType = Result.PUB_UNKNOWN_ERROR.getResultType();
        this.message = ex.getMessage();
    }

    /**
     * 验证失败 公共异常
     *
     * @return
     */
    public static ResponseBase GetValidateErrorResponse() {
        return new ResponseBase(Result.PUB_VALID_ERROR.getResultType(), Result.PUB_VALID_ERROR.getMessage());
    }

    /**
     * 验证失败 自定义错误码
     *
     * @return
     */
    public static ResponseBase GetValidateErrorResponse(Result result) {
        return new ResponseBase(result.getResultType(), result.getMessage());
    }

    /**
     * 验证失败 自定义错误码
     *
     * @return
     */
    public static ResponseBase GetValidateErrorResponse(String message) {
        return new ResponseBase(Result.PUB_VALID_ERROR.getResultType(), message);
    }


    /**
     * 执行失败 公共异常
     *
     * @return
     */
    public static ResponseBase GetErrorResponse() {
        return new ResponseBase(Result.PUB_FAIL.getResultType(), Result.PUB_FAIL.getMessage());
    }

    /**
     * 执行失败 自定义错误码
     *
     * @return
     */
    public static ResponseBase GetErrorResponse(Result result) {
        return new ResponseBase(result.getResultType(), result.getMessage());
    }

    /**
     * 验证失败 自定义错误信息
     *
     * @return
     */
    public static ResponseBase GetErrorResponse(String message) {
        return new ResponseBase(Result.PUB_FAIL.getResultType(), message);
    }

    /**
     * 执行成功
     *
     * @return
     */
    public static ResponseBase GetSuccessResponse() {
        return new ResponseBase<>().setReturnEntity(1);
    }

    /**
     * 执行成功并返回对象数据
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ResponseBase GetSuccessResponse(T result) {
        ResponseBase responseBase = new ResponseBase<>();
        responseBase.setReturnEntity(result);
        return responseBase;
    }

}
