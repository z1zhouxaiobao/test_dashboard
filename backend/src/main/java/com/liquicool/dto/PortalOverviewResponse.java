package com.liquicool.dto;

import lombok.Data;

@Data
public class PortalOverviewResponse {

    private long productCount;
    private long caseCount;
    private long newsCount;
    private long technologyCount;
    private long honorCount;
}
