package com.highershine.oauth2.server.utils;


import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.enums.ExceptionEnum;
import com.highershine.oauth2.server.enums.HttpStatusEnum;
import com.highershine.oauth2.server.enums.ResultEnum;

/**
 * @author highershine.mizhanlei
 * @date 2018/8/27 13:03
 */
public final class ResultUtil {
    private ResultUtil() {

    }
    /**
     * 成功
     *
     * @return
     */
    public static Result successResult() {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        return result;
    }

    /**
     * 成功
     *
     * @param object
     * @return
     */
    public static Result successResult(Object object) {
        Result result = new Result();
        result.setCode(200);
        result.setData(object);
        result.setMsg("成功");

        return result;
    }

    /**
     * 成功
     *
     * @param resultEnum
     * @return
     */
    public static Result successResult(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());

        return result;
    }

    /**
     * 成功
     *
     * @param httpStatusEnum
     * @return
     */
    public static Result successResult(HttpStatusEnum httpStatusEnum) {
        Result result = new Result();
        result.setCode(httpStatusEnum.getCode());
        result.setMsg(httpStatusEnum.getMsg());

        return result;
    }

    /**
     * 成功
     *
     * @param httpStatusEnum
     * @return
     */
    public static Result successResult(HttpStatusEnum httpStatusEnum, Object object) {
        Result result = new Result();
        result.setCode(httpStatusEnum.getCode());
        result.setMsg(httpStatusEnum.getMsg());
        result.setData(object);

        return result;
    }

    /**
     * 成功
     *
     * @param resultEnum
     * @param object
     * @return
     */
    public static Result successResult(ResultEnum resultEnum, Object object) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(object);

        return result;
    }

    /**
     * 成功
     *
     * @param code
     * @param msg
     * @param object
     * @return
     */
    public static Result successResult(Integer code, String msg, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    /**
     * 成功
     *
     * @param code
     * @param msg
     * @return
     */
    public static Result successResult(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


    /**
     * 失败
     *
     * @param code
     * @param msg
     * @return
     */
    public static Result errorResult(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 失败
     *
     * @param resultEnum
     * @return
     */
    public static Result errorResult(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());

        return result;
    }

    /**
     * 失败
     *
     * @param exceptionEnum
     * @return
     */
    public static Result errorResult(ExceptionEnum exceptionEnum) {
        Result result = new Result();
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());

        return result;
    }
}
