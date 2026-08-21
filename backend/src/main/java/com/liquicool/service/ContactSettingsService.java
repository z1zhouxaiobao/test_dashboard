package com.liquicool.service;

import com.liquicool.dto.ContactSettingsDto;
import com.liquicool.entity.SysConfig;
import com.liquicool.repository.SysConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ContactSettingsService {

    public static final Map<String, String[]> DEFAULTS = new LinkedHashMap<>();

    static {
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
        DEFAULTS.put("contact.support_heading", new String[]{"获取产品和服务支持", "联系页-支持区标题"});
        DEFAULTS.put("contact.support_heading.tw", new String[]{"獲取產品和服務支持", "联系页-支持区标题-繁"});
        DEFAULTS.put("contact.support_heading.en", new String[]{"Get Product & Service Support", "联系页-支持区标题-英"});
        DEFAULTS.put("contact.email", new String[]{"contact@liquicool.com", "联系页-邮箱"});
        DEFAULTS.put("contact.address", new String[]{"北京市海淀区科技园区", "联系页-地址"});
        DEFAULTS.put("contact.address.tw", new String[]{"北京市海淀區科技園區", "联系页-地址-繁"});
        DEFAULTS.put("contact.address.en", new String[]{"Haidian Science Park, Beijing", "联系页-地址-英"});
        DEFAULTS.put("contact.company_phone", new String[]{"400-888-0000", "联系页-公司电话"});
    }

    @Autowired
    private SysConfigRepository sysConfigRepository;

    public ContactSettingsDto getSettings() {
        ContactSettingsDto dto = new ContactSettingsDto();
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
        dto.setSupportHeading(get("contact.support_heading"));
        dto.setSupportHeadingTw(get("contact.support_heading.tw"));
        dto.setSupportHeadingEn(get("contact.support_heading.en"));
        dto.setEmail(get("contact.email"));
        dto.setAddress(get("contact.address"));
        dto.setAddressTw(get("contact.address.tw"));
        dto.setAddressEn(get("contact.address.en"));
        dto.setCompanyPhone(get("contact.company_phone"));
        return dto;
    }

    @Transactional
    public ContactSettingsDto saveSettings(ContactSettingsDto dto) {
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
        put("contact.support_heading", dto.getSupportHeading());
        put("contact.support_heading.tw", dto.getSupportHeadingTw());
        put("contact.support_heading.en", dto.getSupportHeadingEn());
        put("contact.email", dto.getEmail());
        put("contact.address", dto.getAddress());
        put("contact.address.tw", dto.getAddressTw());
        put("contact.address.en", dto.getAddressEn());
        put("contact.company_phone", dto.getCompanyPhone());
        return getSettings();
    }

    @Transactional
    public void ensureDefaults() {
        for (Map.Entry<String, String[]> e : DEFAULTS.entrySet()) {
            if (!sysConfigRepository.findByConfigKey(e.getKey()).isPresent()) {
                SysConfig c = new SysConfig();
                c.setConfigKey(e.getKey());
                c.setConfigValue(e.getValue()[0]);
                c.setRemark(e.getValue()[1]);
                sysConfigRepository.save(c);
            }
        }
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
