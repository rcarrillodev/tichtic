package com.rcdev.tichtic.urlshortener;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UrlRepository extends CrudRepository<UrlModel, Long> {
    boolean existsByShortCode(String shortCode);
    UrlModel getByShortCode(String shortCode);
    @Query("SELECT url FROM UrlModel url WHERE url.expiresOn <= :day")
    List<UrlModel> findAllWithExpiresOnAfterToday(@Param("day")LocalDateTime day);
}
