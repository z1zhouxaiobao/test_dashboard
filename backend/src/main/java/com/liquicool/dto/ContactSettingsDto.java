package com.liquicool.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContactSettingsDto {
    private String talkNow;
    private String talkNowTw;
    private String talkNowEn;
    private String presalesTitle;
    private String presalesTitleTw;
    private String presalesTitleEn;
    private String presalesDesc;
    private String presalesDescTw;
    private String presalesDescEn;
    private String presalesPhone;
    private String presalesBtn;
    private String presalesBtnTw;
    private String presalesBtnEn;
    private String aftersalesTitle;
    private String aftersalesTitleTw;
    private String aftersalesTitleEn;
    private String aftersalesDesc;
    private String aftersalesDescTw;
    private String aftersalesDescEn;
    private String aftersalesPhone;
    private String aftersalesBtn;
    private String aftersalesBtnTw;
    private String aftersalesBtnEn;
    private String supportHeading;
    private String supportHeadingTw;
    private String supportHeadingEn;
    private String email;
    private String companyPhone;

    /** 多地址（含三语） */
    private List<ContactAddressItem> addresses = new ArrayList<>();

    /**
     * 兼容旧字段：取第一条地址简体
     */
    public String getAddress() {
        if (addresses != null && !addresses.isEmpty() && addresses.get(0) != null) {
            return addresses.get(0).getText();
        }
        return null;
    }

    public String getAddressTw() {
        if (addresses != null && !addresses.isEmpty() && addresses.get(0) != null) {
            return addresses.get(0).getTextTw();
        }
        return null;
    }

    public String getAddressEn() {
        if (addresses != null && !addresses.isEmpty() && addresses.get(0) != null) {
            return addresses.get(0).getTextEn();
        }
        return null;
    }
}
