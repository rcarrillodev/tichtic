package com.rcdev.tichtic.urlshortener.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data

public class UrlDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -884570389526626911L;
    private String shortenedUrl;
    private String originalUrl;
    private String createdAt;
    private String shortCode;
}
