package com.liquicool.service;

import com.liquicool.entity.JobPosition;
import com.liquicool.repository.JobPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobPositionSeedService {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Transactional
    public void ensureDefaults() {
        if (jobPositionRepository.count() > 0) {
            return;
        }
        jobPositionRepository.save(job(
                "热管理方案工程师", "熱管理方案工程師", "Thermal Solution Engineer",
                "面向高密度数据中心设计液冷解决方案。",
                "面向高密度資料中心設計液冷方案。",
                "Design liquid cooling solutions for high-density data centers.",
                "1. 根据客户机房条件完成液冷方案设计与选型\n2. 输出技术方案、图纸与交付文档\n3. 协同产品、交付团队推进项目落地\n4. 跟踪行业标准与竞品动态，持续优化方案",
                "1. 根據客戶機房條件完成液冷方案設計與選型\n2. 輸出技術方案、圖紙與交付文檔\n3. 協同產品、交付團隊推進項目落地\n4. 跟蹤行業標準與競品動態，持續優化方案",
                "1. Design and select liquid cooling solutions for customer sites\n2. Deliver technical proposals, drawings and documentation\n3. Collaborate with product and delivery teams\n4. Track standards and competitors to improve solutions",
                "热管理相关背景\n3年以上经验\n具备现场交付能力",
                "熱管理相關背景\n3年以上經驗\n具備現場交付能力",
                "Thermal engineering background\n3+ years experience\nOn-site deployment capability",
                "深圳", "深圳", "Shenzhen", 1
        ));
        jobPositionRepository.save(job(
                "CDU 产品经理", "CDU 產品經理", "CDU Product Manager",
                "负责 CDU 产品规划与客户交付。",
                "負責 CDU 產品規劃與客戶交付。",
                "Own CDU roadmap and customer delivery.",
                "1. 制定 CDU 产品路线图与版本规划\n2. 收集客户与一线反馈，定义需求优先级\n3. 协调研发、供应链与市场推进上市\n4. 跟进重点项目交付与竞品对标",
                "1. 制定 CDU 產品路線圖與版本規劃\n2. 收集客戶與一線反饋，定義需求優先級\n3. 協調研發、供應鏈與市場推進上市\n4. 跟進重點項目交付與競品對標",
                "1. Own CDU roadmap and release planning\n2. Gather customer feedback and prioritize requirements\n3. Align R&D, supply chain and marketing for launch\n4. Support key deliveries and competitive analysis",
                "机电背景优先\nB2B 产品经验",
                "機電背景優先\nB2B 產品經驗",
                "Mechanical / electrical background\nB2B product experience",
                "深圳", "深圳", "Shenzhen", 2
        ));
        jobPositionRepository.save(job(
                "现场服务工程师", "現場服務工程師", "Field Service Engineer",
                "负责液冷系统调试与运维支持。",
                "負責液冷系統調試與運維支持。",
                "Commissioning and O&M for liquid cooling systems.",
                "1. 负责液冷系统现场安装指导与调试\n2. 处理售后故障排查与运维支持\n3. 输出服务报告并沉淀知识库\n4. 配合售前完成现场勘测与方案验证",
                "1. 負責液冷系統現場安裝指導與調試\n2. 處理售後故障排查與運維支持\n3. 輸出服務報告並沉澱知識庫\n4. 配合售前完成現場勘測與方案驗證",
                "1. Guide installation and commissioning on site\n2. Troubleshoot after-sales issues and O&M\n3. Produce service reports and knowledge base content\n4. Support pre-sales surveys and solution validation",
                "可接受出差\n故障排查能力强",
                "可接受出差\n故障排查能力強",
                "Willingness to travel\nStrong troubleshooting skills",
                "深圳 / 可出差", "深圳 / 可出差", "Shenzhen / travel", 3
        ));
    }

    private static JobPosition job(
            String title, String titleTw, String titleEn,
            String summary, String summaryTw, String summaryEn,
            String responsibilities, String responsibilitiesTw, String responsibilitiesEn,
            String requirements, String requirementsTw, String requirementsEn,
            String location, String locationTw, String locationEn,
            int sortOrder) {
        JobPosition j = new JobPosition();
        j.setTitle(title);
        j.setTitleTw(titleTw);
        j.setTitleEn(titleEn);
        j.setSummary(summary);
        j.setSummaryTw(summaryTw);
        j.setSummaryEn(summaryEn);
        j.setResponsibilities(responsibilities);
        j.setResponsibilitiesTw(responsibilitiesTw);
        j.setResponsibilitiesEn(responsibilitiesEn);
        j.setRequirements(requirements);
        j.setRequirementsTw(requirementsTw);
        j.setRequirementsEn(requirementsEn);
        j.setLocation(location);
        j.setLocationTw(locationTw);
        j.setLocationEn(locationEn);
        j.setSortOrder(sortOrder);
        j.setStatus(1);
        return j;
    }
}
