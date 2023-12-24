package com.ood.tamanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;    // 响应码，1 代表成功; 0 代表失败
    private String msg;      // 响应信息 描述字符串
    private Object data;     // 返回的数据
    private String redirectUrl; // 重定向的url

    // 成功响应（不带数据和重定向 URL）
    public static Result success() {
        return new Result(1, "success", null, null);
    }

    // 成功响应（带数据）
    public static Result success(Object data) {
        return new Result(1, "success", data, null);
    }

    // 成功响应（带数据和重定向 URL）
    public static Result success(Object data, String redirectUrl) {
        return new Result(1, "success", data, redirectUrl);
    }

    // 失败响应
    public static Result error(String msg) {
        return new Result(0, msg, null, null);
    }

    public boolean isSuccess() {
        return this.code == 1;
    }
}


