package com.mx.moxietest.result.data;

import com.alibaba.fastjson.annotation.JSONField;

public class LivenessResult {
    private boolean success;
    private String code;
    private String msg;
    private ResultInfo data;

    @JSONField(name = "success")
    public boolean isSuccess() {
        return success;
    }

    @JSONField(name = "success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JSONField(name = "code")
    public String getCode() {
        return code;
    }

    @JSONField(name = "code")
    public void setCode(String code) {
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
    public ResultInfo getData() {
        return data;
    }

    @JSONField(name = "data")
    public void setData(ResultInfo data) {
        this.data = data;
    }

    public class ResultInfo{
        private String trans_id;
        private String mx_trans_id;
        private String fee;
        private String status;
        private String reason;


        @JSONField(name = "trans_id")
        public String getTrans_id() {
            return trans_id;
        }

        @JSONField(name = "trans_id")
        public void setTrans_id(String trans_id) {
            this.trans_id = trans_id;
        }

        @JSONField(name = "mx_trans_id")
        public String getMx_trans_id() {
            return mx_trans_id;
        }

        @JSONField(name = "mx_trans_id")
        public void setMx_trans_id(String mx_trans_id) {
            this.mx_trans_id = mx_trans_id;
        }

        @JSONField(name = "fee")
        public String getFee() {
            return fee;
        }

        @JSONField(name = "fee")
        public void setFee(String fee) {
            this.fee = fee;
        }

        @JSONField(name = "status")
        public String getStatus() {
            return status;
        }

        @JSONField(name = "status")
        public void setStatus(String status) {
            this.status = status;
        }

        @JSONField(name = "reason")
        public String getReason() {
            return reason;
        }

        @JSONField(name = "reason")
        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
