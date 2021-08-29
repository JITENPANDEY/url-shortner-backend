package com.urlshortner.urlshortner.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponse {
    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}
