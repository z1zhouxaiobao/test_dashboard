package com.liquicool.dto;

import lombok.Data;

@Data
public class ContactSocialAccountItem {
    /** 账号名称（如微信公众号、抖音） */
    private String name;
    private String nameTw;
    private String nameEn;
    /** 二维码图片 URL */
    private String qrUrl;
}
