package com.hafsaaek;

public interface FxRateProvider {
    // Option 1
    Double getFxRate(String from, String to);

    // Option 2
    CcyConversionResult getConversionResult(String from);
}
