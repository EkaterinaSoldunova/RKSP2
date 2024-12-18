package com.example.OnlineDiary_RSocket_Soldunova.repositories;

import com.example.OnlineDiary_RSocket_Soldunova.models.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
