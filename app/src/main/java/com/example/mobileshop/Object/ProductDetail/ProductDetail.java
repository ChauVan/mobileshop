package com.example.mobileshop.Object.ProductDetail;

public class ProductDetail {
    String configName;
    String configContent;

    public ProductDetail(String configName, String configContent) {
        this.configName = configName;
        this.configContent = configContent;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }
}
