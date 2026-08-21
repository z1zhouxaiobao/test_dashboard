package com.liquicool.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContactSettingsDto {
    /** 站点 Logo（顶栏/页脚），空则用默认 /logo.svg */
    private String logoUrl;

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
    /** 售前按钮链接，支持 tel: / mailto: / https:// */
    private String presalesBtnLink;
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
    /** 售后按钮链接，支持 tel: / mailto: / https:// */
    private String aftersalesBtnLink;
    private String supportHeading;
    private String supportHeadingTw;
    private String supportHeadingEn;
    private String email;
    private String companyPhone;

    /** 多地址（含三语） */
    private List<ContactAddressItem> addresses = new ArrayList<>();

    /** 品宣账号二维码（公众号、抖音等） */
    private List<ContactSocialAccountItem> socialAccounts = new ArrayList<>();

    /** 页脚版权文案，空则用前台默认文案 */
    private String copyrightText;
    /** ICP 备案号，如 粤ICP备xxxxxxxx号 */
    private String icpNumber;
    /** ICP 备案跳转链接，默认工信部查询页 */
    private String icpUrl;
    /** 公安备案号，如 粤公网安备xxxxxxxx号 */
    private String policeBeian;
    /** 公安备案跳转链接 */
    private String policeBeianUrl;

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
