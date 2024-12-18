package com.example.onlinediary_webflux_soldunova.controllers;

import com.example.onlinediary_webflux_soldunova.models.Diary;
import com.example.onlinediary_webflux_soldunova.repositories.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.onlinediary_webflux_soldunova.exception.CustomException;

@RestController
@RequestMapping("/diaries")
public class DiaryController {
    private final DiaryRepository diaryRepository; //Солдунова ИКБО-20-21
    @Autowired
    public DiaryController(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Diary>> getDiaryById(@PathVariable Long id) {
        return diaryRepository.findById(id)
                .map(diary -> ResponseEntity.ok(diary))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping
    public Flux<Diary> getAllDiaries() {
        return diaryRepository.findAll()
                .onErrorResume(e -> {
// Обработка ошибок
                    return Flux.error(new CustomException("Failed to fetch diary", e));
                })
                .onBackpressureBuffer(); // Указывает работать в режиме backpressure buffer, что позволяет буферизовать данные в случае, если клиент не успевает их обработать.
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Diary> createDiary(@RequestBody Diary diary) {
        return diaryRepository.save(diary);
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Diary>> updateDiary(@PathVariable Long id, @RequestBody Diary updatedDiary) {
        return diaryRepository.findById(id)
                .flatMap(existingDiary -> {
                    existingDiary.setTopic(updatedDiary.getTopic());
                    existingDiary.setDescription((updatedDiary.getDescription()));
                    return diaryRepository.save(existingDiary);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDiary(@PathVariable Long id) {
        return diaryRepository.findById(id)
                .flatMap(existingDiary ->
                        diaryRepository.delete(existingDiary)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
