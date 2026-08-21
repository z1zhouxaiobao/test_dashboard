package com.liquicool.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liquicool.dto.ContactAddressItem;
import com.liquicool.dto.ContactSettingsDto;
import com.liquicool.dto.ContactSocialAccountItem;
import com.liquicool.entity.SysConfig;
import com.liquicool.repository.SysConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContactSettingsService {

    public static final String ADDRESSES_KEY = "contact.addresses";
    public static final String SOCIAL_ACCOUNTS_KEY = "contact.social_accounts";

    public static final Map<String, String[]> DEFAULTS = new LinkedHashMap<>();

    static {
        DEFAULTS.put("site.logo_url", new String[]{"", "站点Logo图片URL"});
        DEFAULTS.put("contact.talk_now", new String[]{"即刻对话", "联系页-即刻对话标题"});
        DEFAULTS.put("contact.talk_now.tw", new String[]{"即刻對話", "联系页-即刻对话标题-繁"});
        DEFAULTS.put("contact.talk_now.en", new String[]{"Talk Now", "联系页-即刻对话标题-英"});
        DEFAULTS.put("contact.presales.title", new String[]{"售前人工客服", "联系页-售前标题"});
        DEFAULTS.put("contact.presales.title.tw", new String[]{"售前人工客服", "联系页-售前标题-繁"});
        DEFAULTS.put("contact.presales.title.en", new String[]{"Presales Support", "联系页-售前标题-英"});
        DEFAULTS.put("contact.presales.desc", new String[]{"售前咨询、方案选型与商务对接，工作日人工客服在线响应。", "联系页-售前说明"});
        DEFAULTS.put("contact.presales.desc.tw", new String[]{"售前諮詢、方案選型與商務對接，工作日人工客服在線響應。", "联系页-售前说明-繁"});
        DEFAULTS.put("contact.presales.desc.en", new String[]{"Presales consulting, solution selection and business contact on business days.", "联系页-售前说明-英"});
        DEFAULTS.put("contact.presales.phone", new String[]{"400-888-0000", "联系页-售前电话"});
        DEFAULTS.put("contact.presales.btn", new String[]{"售前人工客服", "联系页-售前按钮"});
        DEFAULTS.put("contact.presales.btn.tw", new String[]{"售前人工客服", "联系页-售前按钮-繁"});
        DEFAULTS.put("contact.presales.btn.en", new String[]{"Presales Support", "联系页-售前按钮-英"});
        DEFAULTS.put("contact.presales.btn_link", new String[]{"tel:400-888-0000", "联系页-售前按钮链接"});
        DEFAULTS.put("contact.aftersales.title", new String[]{"售后技术支持", "联系页-售后标题"});
        DEFAULTS.put("contact.aftersales.title.tw", new String[]{"售後技術支持", "联系页-售后标题-繁"});
        DEFAULTS.put("contact.aftersales.title.en", new String[]{"After-sales Support", "联系页-售后标题-英"});
        DEFAULTS.put("contact.aftersales.desc", new String[]{"已购产品的技术支持、故障排查与运维协助，智能与人工协同服务。", "联系页-售后说明"});
        DEFAULTS.put("contact.aftersales.desc.tw", new String[]{"已購產品的技術支持、故障排查與運維協助，智能與人工協同服務。", "联系页-售后说明-繁"});
        DEFAULTS.put("contact.aftersales.desc.en", new String[]{"Technical support, troubleshooting and O&M assistance for purchased products.", "联系页-售后说明-英"});
        DEFAULTS.put("contact.aftersales.phone", new String[]{"400-888-0001", "联系页-售后电话"});
        DEFAULTS.put("contact.aftersales.btn", new String[]{"售后技术支持", "联系页-售后按钮"});
        DEFAULTS.put("contact.aftersales.btn.tw", new String[]{"售後技術支持", "联系页-售后按钮-繁"});
        DEFAULTS.put("contact.aftersales.btn.en", new String[]{"After-sales Support", "联系页-售后按钮-英"});
        DEFAULTS.put("contact.aftersales.btn_link", new String[]{"tel:400-888-0001", "联系页-售后按钮链接"});
        DEFAULTS.put("contact.support_heading", new String[]{"获取产品和服务支持", "联系页-支持区标题"});
        DEFAULTS.put("contact.support_heading.tw", new String[]{"獲取產品和服務支持", "联系页-支持区标题-繁"});
        DEFAULTS.put("contact.support_heading.en", new String[]{"Get Product & Service Support", "联系页-支持区标题-英"});
        DEFAULTS.put("contact.email", new String[]{"contact@liquicool.com", "联系页-邮箱"});
        DEFAULTS.put("contact.address", new String[]{"北京市海淀区科技园区", "联系页-地址"});
        DEFAULTS.put("contact.address.tw", new String[]{"北京市海淀區科技園區", "联系页-地址-繁"});
        DEFAULTS.put("contact.address.en", new String[]{"Haidian Science Park, Beijing", "联系页-地址-英"});
        DEFAULTS.put("contact.company_phone", new String[]{"400-888-0000", "联系页-公司电话"});
        DEFAULTS.put(ADDRESSES_KEY, new String[]{
                "[{\"text\":\"北京市海淀区科技园区\",\"textTw\":\"北京市海淀區科技園區\",\"textEn\":\"Haidian Science Park, Beijing\"}]",
                "联系页-地址列表JSON"
        });
        DEFAULTS.put(SOCIAL_ACCOUNTS_KEY, new String[]{
                "["
                        + "{\"name\":\"微信公众号\",\"nameTw\":\"微信公眾號\",\"nameEn\":\"WeChat Official Account\",\"qrUrl\":\"\"},"
                        + "{\"name\":\"抖音\",\"nameTw\":\"抖音\",\"nameEn\":\"Douyin\",\"qrUrl\":\"\"}"
                        + "]",
                "品宣账号二维码列表JSON"
        });
        DEFAULTS.put("site.copyright", new String[]{"版权所有 © 2026 立冷科技有限公司 LIQUICOOL", "页脚版权文案"});
        DEFAULTS.put("site.icp_number", new String[]{"粤ICP备2025499996号", "ICP备案号"});
        DEFAULTS.put("site.icp_url", new String[]{"https://beian.miit.gov.cn/", "ICP备案查询链接"});
        DEFAULTS.put("site.police_beian", new String[]{"粤公网安备44030002009032号", "公安备案号"});
        DEFAULTS.put("site.police_beian_url", new String[]{
                "https://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030002009032",
                "公安备案查询链接"
        });
    }

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public ContactSettingsDto getSettings() {
        ContactSettingsDto dto = new ContactSettingsDto();
        dto.setLogoUrl(get("site.logo_url"));
        dto.setTalkNow(get("contact.talk_now"));
        dto.setTalkNowTw(get("contact.talk_now.tw"));
        dto.setTalkNowEn(get("contact.talk_now.en"));
        dto.setPresalesTitle(get("contact.presales.title"));
        dto.setPresalesTitleTw(get("contact.presales.title.tw"));
        dto.setPresalesTitleEn(get("contact.presales.title.en"));
        dto.setPresalesDesc(get("contact.presales.desc"));
        dto.setPresalesDescTw(get("contact.presales.desc.tw"));
        dto.setPresalesDescEn(get("contact.presales.desc.en"));
        dto.setPresalesPhone(get("contact.presales.phone"));
        dto.setPresalesBtn(get("contact.presales.btn"));
        dto.setPresalesBtnTw(get("contact.presales.btn.tw"));
        dto.setPresalesBtnEn(get("contact.presales.btn.en"));
        dto.setPresalesBtnLink(get("contact.presales.btn_link"));
        dto.setAftersalesTitle(get("contact.aftersales.title"));
        dto.setAftersalesTitleTw(get("contact.aftersales.title.tw"));
        dto.setAftersalesTitleEn(get("contact.aftersales.title.en"));
        dto.setAftersalesDesc(get("contact.aftersales.desc"));
        dto.setAftersalesDescTw(get("contact.aftersales.desc.tw"));
        dto.setAftersalesDescEn(get("contact.aftersales.desc.en"));
        dto.setAftersalesPhone(get("contact.aftersales.phone"));
        dto.setAftersalesBtn(get("contact.aftersales.btn"));
        dto.setAftersalesBtnTw(get("contact.aftersales.btn.tw"));
        dto.setAftersalesBtnEn(get("contact.aftersales.btn.en"));
        dto.setAftersalesBtnLink(get("contact.aftersales.btn_link"));
        dto.setSupportHeading(get("contact.support_heading"));
        dto.setSupportHeadingTw(get("contact.support_heading.tw"));
        dto.setSupportHeadingEn(get("contact.support_heading.en"));
        dto.setEmail(get("contact.email"));
        dto.setCompanyPhone(get("contact.company_phone"));
        dto.setAddresses(loadAddresses());
        dto.setSocialAccounts(loadSocialAccounts());
        dto.setCopyrightText(get("site.copyright"));
        dto.setIcpNumber(get("site.icp_number"));
        dto.setIcpUrl(get("site.icp_url"));
        dto.setPoliceBeian(get("site.police_beian"));
        dto.setPoliceBeianUrl(get("site.police_beian_url"));
        return dto;
    }

    @Transactional
    public ContactSettingsDto saveSettings(ContactSettingsDto dto) {
        put("site.logo_url", dto.getLogoUrl());
        put("contact.talk_now", dto.getTalkNow());
        put("contact.talk_now.tw", dto.getTalkNowTw());
        put("contact.talk_now.en", dto.getTalkNowEn());
        put("contact.presales.title", dto.getPresalesTitle());
        put("contact.presales.title.tw", dto.getPresalesTitleTw());
        put("contact.presales.title.en", dto.getPresalesTitleEn());
        put("contact.presales.desc", dto.getPresalesDesc());
        put("contact.presales.desc.tw", dto.getPresalesDescTw());
        put("contact.presales.desc.en", dto.getPresalesDescEn());
        put("contact.presales.phone", dto.getPresalesPhone());
        put("contact.presales.btn", dto.getPresalesBtn());
        put("contact.presales.btn.tw", dto.getPresalesBtnTw());
        put("contact.presales.btn.en", dto.getPresalesBtnEn());
        put("contact.presales.btn_link", dto.getPresalesBtnLink());
        put("contact.aftersales.title", dto.getAftersalesTitle());
        put("contact.aftersales.title.tw", dto.getAftersalesTitleTw());
        put("contact.aftersales.title.en", dto.getAftersalesTitleEn());
        put("contact.aftersales.desc", dto.getAftersalesDesc());
        put("contact.aftersales.desc.tw", dto.getAftersalesDescTw());
        put("contact.aftersales.desc.en", dto.getAftersalesDescEn());
        put("contact.aftersales.phone", dto.getAftersalesPhone());
        put("contact.aftersales.btn", dto.getAftersalesBtn());
        put("contact.aftersales.btn.tw", dto.getAftersalesBtnTw());
        put("contact.aftersales.btn.en", dto.getAftersalesBtnEn());
        put("contact.aftersales.btn_link", dto.getAftersalesBtnLink());
        put("contact.support_heading", dto.getSupportHeading());
        put("contact.support_heading.tw", dto.getSupportHeadingTw());
        put("contact.support_heading.en", dto.getSupportHeadingEn());
        put("contact.email", dto.getEmail());
        put("contact.company_phone", dto.getCompanyPhone());
        put("site.copyright", dto.getCopyrightText());
        put("site.icp_number", dto.getIcpNumber());
        put("site.icp_url", dto.getIcpUrl());
        put("site.police_beian", dto.getPoliceBeian());
        put("site.police_beian_url", dto.getPoliceBeianUrl());

        List<ContactAddressItem> list = normalizeAddresses(dto.getAddresses());
        saveAddresses(list);
        // 兼容旧单地址字段
        if (!list.isEmpty()) {
            ContactAddressItem first = list.get(0);
            put("contact.address", first.getText());
            put("contact.address.tw", first.getTextTw());
            put("contact.address.en", first.getTextEn());
        } else {
            put("contact.address", "");
            put("contact.address.tw", "");
            put("contact.address.en", "");
        }
        saveSocialAccounts(normalizeSocialAccounts(dto.getSocialAccounts()));
        return getSettings();
    }

    @Transactional
    public void ensureDefaults() {
        for (Map.Entry<String, String[]> e : DEFAULTS.entrySet()) {
            // 地址/品宣列表单独处理，避免覆盖已有配置
            if (ADDRESSES_KEY.equals(e.getKey()) || SOCIAL_ACCOUNTS_KEY.equals(e.getKey())) {
                continue;
            }
            if (!sysConfigRepository.findByConfigKey(e.getKey()).isPresent()) {
                SysConfig c = new SysConfig();
                c.setConfigKey(e.getKey());
                c.setConfigValue(e.getValue()[0]);
                c.setRemark(e.getValue()[1]);
                sysConfigRepository.save(c);
            }
        }
        // 尚无列表时：优先用已有单地址字段拼成一条
        if (!sysConfigRepository.findByConfigKey(ADDRESSES_KEY).isPresent()) {
            ContactAddressItem item = new ContactAddressItem();
            item.setText(getRawOrDefault("contact.address"));
            item.setTextTw(getRawOrDefault("contact.address.tw"));
            item.setTextEn(getRawOrDefault("contact.address.en"));
            if (!StringUtils.hasText(item.getText())) {
                item.setText("北京市海淀区科技园区");
                item.setTextTw("北京市海淀區科技園區");
                item.setTextEn("Haidian Science Park, Beijing");
            }
            saveAddresses(Collections.singletonList(item));
        }
        if (!sysConfigRepository.findByConfigKey(SOCIAL_ACCOUNTS_KEY).isPresent()) {
            String[] def = DEFAULTS.get(SOCIAL_ACCOUNTS_KEY);
            put(SOCIAL_ACCOUNTS_KEY, def != null ? def[0] : "[]");
        }
    }

    private List<ContactAddressItem> loadAddresses() {
        String json = sysConfigRepository.findByConfigKey(ADDRESSES_KEY)
                .map(SysConfig::getConfigValue)
                .orElse("");
        if (StringUtils.hasText(json)) {
            try {
                List<ContactAddressItem> list = objectMapper.readValue(
                        json, new TypeReference<List<ContactAddressItem>>() {});
                list = normalizeAddresses(list);
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception ignored) {
                // fall through to legacy
            }
        }
        // 兼容旧配置
        String zh = get("contact.address");
        if (!StringUtils.hasText(zh)) {
            return new ArrayList<>();
        }
        ContactAddressItem item = new ContactAddressItem();
        item.setText(zh);
        item.setTextTw(get("contact.address.tw"));
        item.setTextEn(get("contact.address.en"));
        return new ArrayList<>(Collections.singletonList(item));
    }

    private void saveAddresses(List<ContactAddressItem> list) {
        try {
            String json = objectMapper.writeValueAsString(list == null ? Collections.emptyList() : list);
            put(ADDRESSES_KEY, json);
        } catch (Exception e) {
            put(ADDRESSES_KEY, "[]");
        }
    }

    private List<ContactSocialAccountItem> loadSocialAccounts() {
        String json = sysConfigRepository.findByConfigKey(SOCIAL_ACCOUNTS_KEY)
                .map(SysConfig::getConfigValue)
                .orElse("");
        if (!StringUtils.hasText(json)) {
            return new ArrayList<>();
        }
        try {
            List<ContactSocialAccountItem> list = objectMapper.readValue(
                    json, new TypeReference<List<ContactSocialAccountItem>>() {});
            return normalizeSocialAccounts(list);
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    private void saveSocialAccounts(List<ContactSocialAccountItem> list) {
        try {
            String json = objectMapper.writeValueAsString(list == null ? Collections.emptyList() : list);
            put(SOCIAL_ACCOUNTS_KEY, json);
        } catch (Exception e) {
            put(SOCIAL_ACCOUNTS_KEY, "[]");
        }
    }

    private List<ContactSocialAccountItem> normalizeSocialAccounts(List<ContactSocialAccountItem> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        return source.stream()
                .filter(item -> item != null && StringUtils.hasText(item.getName()))
                .map(item -> {
                    ContactSocialAccountItem copy = new ContactSocialAccountItem();
                    copy.setName(trimTo(item.getName(), 100));
                    copy.setNameTw(trimTo(item.getNameTw(), 100));
                    copy.setNameEn(trimTo(item.getNameEn(), 100));
                    copy.setQrUrl(trimTo(item.getQrUrl(), 500));
                    return copy;
                })
                .collect(Collectors.toList());
    }

    private List<ContactAddressItem> normalizeAddresses(List<ContactAddressItem> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        return source.stream()
                .filter(item -> item != null && StringUtils.hasText(item.getText()))
                .map(item -> {
                    ContactAddressItem copy = new ContactAddressItem();
                    copy.setText(trimTo(item.getText(), 500));
                    copy.setTextTw(trimTo(item.getTextTw(), 500));
                    copy.setTextEn(trimTo(item.getTextEn(), 500));
                    return copy;
                })
                .collect(Collectors.toList());
    }

    private String trimTo(String value, int max) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() <= max ? trimmed : trimmed.substring(0, max);
    }

    private String get(String key) {
        return sysConfigRepository.findByConfigKey(key)
                .map(SysConfig::getConfigValue)
                .filter(StringUtils::hasText)
                .orElseGet(() -> {
                    String[] def = DEFAULTS.get(key);
                    return def != null ? def[0] : "";
                });
    }

    private String getRawOrDefault(String key) {
        return sysConfigRepository.findByConfigKey(key)
                .map(SysConfig::getConfigValue)
                .filter(StringUtils::hasText)
                .orElseGet(() -> {
                    String[] def = DEFAULTS.get(key);
                    return def != null ? def[0] : "";
                });
    }

    private void put(String key, String value) {
        String[] def = DEFAULTS.get(key);
        SysConfig config = sysConfigRepository.findByConfigKey(key).orElseGet(SysConfig::new);
        config.setConfigKey(key);
        config.setConfigValue(value == null ? "" : value.trim());
        if (!StringUtils.hasText(config.getRemark()) && def != null) {
            config.setRemark(def[1]);
        }
        sysConfigRepository.save(config);
    }
}
