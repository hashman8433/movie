package com.free.common.resp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result implements Serializable {
    public static Result SUCCESS;

    public static String SUCCESS_CODE = "0";

    static {
        SUCCESS = new Result(SUCCESS_CODE, "");
    }

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success(String msg) {
        return new Result(SUCCESS_CODE, msg);
    }

    public static Result success(String msg, Object data) {
        return new Result(SUCCESS_CODE, msg, data);
    }

    private String code;

    private String msg;

    private Object data;
}
