package com.hafsaaek;

import java.util.Map;

public class CcyConversionResult {
    CcyConversionMeta meta;

    public CcyConversionMeta getMeta() {
        return meta;
    }

    public void setMeta(CcyConversionMeta meta) {
        this.meta = meta;
    }

//    public CcyConversionData getData() {
//        return data;
//    }
//
//    public void setData(CcyConversionData data) {
//        this.data = data;
//    }
//
//    CcyConversionData data;

    public Map<String, CcyValue> getData() {
        return data;
    }

    public void setData(Map<String, CcyValue> data) {
        this.data = data;
    }

    Map<String, CcyValue> data;
}
