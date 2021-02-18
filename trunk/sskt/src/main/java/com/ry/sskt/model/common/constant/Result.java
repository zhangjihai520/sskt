package com.ry.sskt.model.common.constant;


import com.ry.sskt.service.IRespCode;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 接口返回对象枚举
 */
public enum Result implements IRespCode {
    PUB_UNKNOWN_ERROR(0,"未知错误！"),
    PUB_SUCCESS(1, "执行成功！"),
    PUB_VALID_ERROR(2, "验证失败！"),
    PUB_FAIL(3, "执行失败！"),

    CANNOT_SPEECH(2, "您不能在该讨论区发言！");
    private int ResultType;

    private String Message;

    Result(Integer ResultType, String Message) {
        this.ResultType = ResultType;
        this.Message = Message;
    }

    public void setResultType(Integer ResultType) {
        this.ResultType = ResultType;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     * 错误码
     *
     * @return 结果
     */
    @Override
    public int getResultType() {
        return this.ResultType;
    }

    /**
     * 错误信息
     *
     * @return 结果
     */
    @Override
    public String getMessage() {
        return this.Message;
    }


    @Override
    public String toString() {
        return "[" + ResultType + "" + Message;
    }
}
