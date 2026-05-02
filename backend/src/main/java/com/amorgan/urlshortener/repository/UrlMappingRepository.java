package com.amorgan.urlshortener.repository;

import com.amorgan.urlshortener.model.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UrlMappingRepository.
 * <p>
 * Repository interface for {@link UrlMapping} documents.
 * Provides standard CRUD operations and custom query methods for interacting with MongoDB.
 * </p>
 *
 * @author adam.morgan
 */
@Repository
public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
    Optional<UrlMapping> findByCustomAlias(String customAlias);
    void deleteByCustomAlias(String customAlias);
    boolean existsByCustomAlias(String customAlias);
}