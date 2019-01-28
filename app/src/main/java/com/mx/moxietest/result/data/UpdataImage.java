package com.mx.moxietest.result.data;

import com.alibaba.fastjson.annotation.JSONField;

public class UpdataImage {
    private boolean success;
    private int code;
    private String msg;
    private String data;

    @JSONField(name = "success")
    public boolean isSuccess() {
        return success;
    }

    @JSONField(name = "success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JSONField(name = "code")
    public int getCode() {
        return code;
    }

    @JSONField(name = "code")
    public void setCode(int code) {
        this.code = code;
    }

    @JSONField(name = "msg")
    public String getMsg() {
        return msg;
    }

    @JSONField(name = "msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JSONField(name = "data")
    public String getData() {
        return data;
    }

    @JSONField(name = "data")
    public void setData(String data) {
        this.data = data;
    }
}
