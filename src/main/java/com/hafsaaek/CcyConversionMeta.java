package com.hafsaaek;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class CcyConversionMeta {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    private Instant last_updated_at;

    public Instant getLast_updated_at() {
        return last_updated_at;
    }

    public void setLast_updated_at(Instant last_updated_at) {
        this.last_updated_at = last_updated_at;
    }

}
