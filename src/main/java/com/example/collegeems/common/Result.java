package com.example.collegeems.common;

import lombok.Data;

@Data
public class Result {

    //请求成功状态码
    private static final String SUCCESS = "200";
    //请求失败状态码
    private static final String ERROR = "500";

    private String code;
    private String msg;
    private Object data;

    //请求成功
    public static Result success(){
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("请求成功");
        result.setData(data);

        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(ERROR);
        return result;
    }
}
