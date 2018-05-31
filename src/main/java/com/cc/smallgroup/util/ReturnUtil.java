package com.cc.smallgroup.util;

public class ReturnUtil {
        private static String NOAUTH_CODE = "-1";
        private static String NOAUTH_INFO = "非法访问";
        private static String FAIL_CODE = "0";
        private static String FAIL_INFO = "请求失败";
        private static String SUCCESS_CODE = "1";
        private static String SUCCESS_INFO = "请求成功";
        private static String NOPERMISSION_CODE = "-2";
        private static String NOPERMISSION_INFO = "无操作权限";

        private String code;
        private String info;
        private Object data;

        public static ReturnUtil noLogin() {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = ReturnUtil.NOAUTH_CODE;
            returnUtil.info = ReturnUtil.NOAUTH_INFO;
            return returnUtil;
        }

        public static ReturnUtil success() {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = SUCCESS_CODE;
            returnUtil.info = SUCCESS_INFO;
            return returnUtil;
        }

        public static ReturnUtil success(String msg) {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = SUCCESS_CODE;
            returnUtil.info = msg;
            return returnUtil;
        }

        public static ReturnUtil result(Object data) {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = SUCCESS_CODE;
            returnUtil.info = SUCCESS_INFO;
            returnUtil.data = data;
            return returnUtil;
        }

        public static ReturnUtil fail() {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = ReturnUtil.FAIL_CODE;
            returnUtil.info = ReturnUtil.FAIL_INFO;
            return returnUtil;
        }

        public static ReturnUtil fail(String msg) {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = ReturnUtil.FAIL_CODE;
            returnUtil.info = msg;
            return returnUtil;
        }

        public static ReturnUtil noPermission() {
            ReturnUtil returnUtil = new ReturnUtil();
            returnUtil.code = ReturnUtil.NOPERMISSION_CODE;
            returnUtil.info = ReturnUtil.NOPERMISSION_INFO;
            return returnUtil;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public Object getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
}
