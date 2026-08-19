package com.liquicool.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardStatsResponse {

    private long userCount;
    private long productCount;
    private long consultationCount;
    private long newsCount;
    private Map<String, Long> consultationByStatus;
    private List<Map<String, Object>> consultationTrend7Days;
    private List<Map<String, Object>> productCategoryDistribution;
    private List<Map<String, Object>> newsViewTop;
}
