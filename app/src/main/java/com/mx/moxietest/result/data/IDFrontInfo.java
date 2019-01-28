package com.mx.moxietest.result.data;

import com.alibaba.fastjson.annotation.JSONField;

public class IDFrontInfo {
    private boolean success;
    private String code;
    private String msg;
    private FrontInfo data;

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
    public FrontInfo getData() {
        return data;
    }

    @JSONField(name = "data")
    public void setData(FrontInfo data) {
        this.data = data;
    }

    public class FrontInfo{
        private String fee;
        private String name;
        private String sex;
        private String nation;
        private String birth;
        private String address;
        private String mx_trans_id;
        private String trans_id;
        private String id_card;
        private String status;
        private String reason;

        @JSONField(name = "fee")
        public String getFee() {
            return fee;
        }

        @JSONField(name = "fee")
        public void setFee(String fee) {
            this.fee = fee;
        }

        @JSONField(name = "name")
        public String getName() {
            return name;
        }

        @JSONField(name = "name")
        public void setName(String name) {
            this.name = name;
        }

        @JSONField(name = "sex")
        public String getSex() {
            return sex;
        }

        @JSONField(name = "sex")
        public void setSex(String sex) {
            this.sex = sex;
        }

        @JSONField(name = "nation")
        public String getNation() {
            return nation;
        }

        @JSONField(name = "nation")
        public void setNation(String nation) {
            this.nation = nation;
        }

        @JSONField(name = "birth")
        public String getBirth() {
            return birth;
        }

        @JSONField(name = "birth")
        public void setBirth(String birth) {
            this.birth = birth;
        }

        @JSONField(name = "address")
        public String getAddress() {
            return address;
        }

        @JSONField(name = "address")
        public void setAddress(String address) {
            this.address = address;
        }

        @JSONField(name = "mx_trans_id")
        public String getMx_trans_id() {
            return mx_trans_id;
        }

        @JSONField(name = "mx_trans_id")
        public void setMx_trans_id(String mx_trans_id) {
            this.mx_trans_id = mx_trans_id;
        }

        @JSONField(name = "trans_id")
        public String getTrans_id() {
            return trans_id;
        }

        @JSONField(name = "trans_id")
        public void setTrans_id(String trans_id) {
            this.trans_id = trans_id;
        }

        @JSONField(name = "id_card")
        public String getId_card() {
            return id_card;
        }

        @JSONField(name = "id_card")
        public void setId_card(String id_card) {
            this.id_card = id_card;
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
