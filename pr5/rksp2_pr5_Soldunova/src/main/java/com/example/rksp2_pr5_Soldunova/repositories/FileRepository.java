package com.example.rksp2_pr5_Soldunova.repositories;

import com.example.rksp2_pr5_Soldunova.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}