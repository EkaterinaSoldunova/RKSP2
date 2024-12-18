package com.example.onlinediary_webflux_soldunova.repositories;


import com.example.onlinediary_webflux_soldunova.models.Diary;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends R2dbcRepository<Diary, Long> {
}
