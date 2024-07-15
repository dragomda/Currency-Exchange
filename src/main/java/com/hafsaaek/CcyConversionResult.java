package com.hafsaaek;

import java.util.Map;

public class CcyConversionResult {
    private CcyConversionMeta meta;

    private Map<String, CcyValue> data;

    public CcyConversionMeta getMeta() {
        return meta;
    }

    public void setMeta(CcyConversionMeta meta) {
        this.meta = meta;
    }

    public Map<String, CcyValue> getData() {
        return data;
    }

    public void setData(Map<String, CcyValue> data) {
        this.data = data;
    }
}
