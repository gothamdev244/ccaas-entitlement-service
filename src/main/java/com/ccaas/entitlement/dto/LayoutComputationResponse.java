package com.ccaas.entitlement.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class LayoutComputationResponse {
    private String userId;
    private Map<String, Object> layout;
    private Map<String, Object> theme;
    private String market;
    private String computationSource;
    private long computationTimeMs;
    private LocalDateTime timestamp;

    public LayoutComputationResponse() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getLayout() {
        return layout;
    }

    public void setLayout(Map<String, Object> layout) {
        this.layout = layout;
    }

    public Map<String, Object> getTheme() {
        return theme;
    }

    public void setTheme(Map<String, Object> theme) {
        this.theme = theme;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getComputationSource() {
        return computationSource;
    }

    public void setComputationSource(String computationSource) {
        this.computationSource = computationSource;
    }

    public long getComputationTimeMs() {
        return computationTimeMs;
    }

    public void setComputationTimeMs(long computationTimeMs) {
        this.computationTimeMs = computationTimeMs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
