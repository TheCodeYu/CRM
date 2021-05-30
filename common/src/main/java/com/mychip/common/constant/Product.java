package com.mychip.common.constant;

/**
 * Description:
 * User:   zhouyu
 * Date:   2021/5/30 0030
 */
public enum  Product {


    crm("00","com.mychip.crm"),
    test("01","com.mychip.test");
    private final String code;
    private final String info;

    Product(String code, String info) {
        this.code = code;
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }
}
