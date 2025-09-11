package com.ccaas.entitlement.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class LayoutComputationRequest {
    private String userId;
    private List<String> adGroups;

    public LayoutComputationRequest() {}

    public LayoutComputationRequest(String userId, List<String> adGroups) {
        this.userId = userId;
        this.adGroups = adGroups;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getAdGroups() {
        return adGroups;
    }

    public void setAdGroups(List<String> adGroups) {
        this.adGroups = adGroups;
    }
}
