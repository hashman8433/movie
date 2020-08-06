package com.free.common.resp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Result {

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    private Object data;
}
