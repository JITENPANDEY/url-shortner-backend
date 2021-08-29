package com.urlshortner.urlshortner.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UrlRequest {
    private String url;
    private String expirationDate;
}
