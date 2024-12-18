package com.example.rksp2_pr5_Soldunova.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileEntity { //Солдунова ИКБО-20-21
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String fileName;
    private Long fileSize;
    @Lob // аннотация @Lob (Long Object) используется для создания поля длинного бинарного файла в базе данных
    private byte[] fileData;
}