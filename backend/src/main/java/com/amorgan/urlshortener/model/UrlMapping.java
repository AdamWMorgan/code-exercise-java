package com.amorgan.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * UrlMapping.
 * <p>
 * Represents the persistent mapping between a shortened alias and a full URL.
 * </p>
 *
 * @author adam.morgan.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "url_mappings")
public class UrlMapping {

    @Id
    private String id;

    @Indexed(unique = true)
    private String customAlias;

    private String fullUrl;

    private String shortUrl;
}