package com.example.rksp2_pr5_Soldunova.controllers;

import com.example.rksp2_pr5_Soldunova.entities.FileEntity;
import com.example.rksp2_pr5_Soldunova.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Controller
public class FileController {
    @Autowired
    private FileRepository fileRepository;
    @GetMapping("/")
    public String listUploadedFiles(Model model){
        List<FileEntity> files = fileRepository.findAll();
        model.addAttribute("files", files);
        return "index";
    }
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filenameWithoutExtension) {
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = filenameWithoutExtension + fileExtension;
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(newFilename);
                fileEntity.setFileData(file.getBytes());
                fileEntity.setFileSize(file.getSize());
                fileRepository.save(fileEntity);
                return "redirect:/";
            } catch (IOException e) {
// Обработка ошибки
            }
        }
        return "redirect:/";
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> serveFile(@PathVariable Long id) {
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(id);
        if (fileEntityOptional.isPresent()) {
            FileEntity fileEntity = fileEntityOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"");
            return new ResponseEntity<>(fileEntity.getFileData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id) {
        fileRepository.deleteById(id);
        return "redirect:/";
    }
}