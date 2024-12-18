package com.example.OnlineDiary_RSocket_Soldunova.controllers;

import com.example.OnlineDiary_RSocket_Soldunova.models.Diary;
import com.example.OnlineDiary_RSocket_Soldunova.repositories.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
public class DiaryController {
    private final DiaryRepository diaryRepository;
    @Autowired
    public DiaryController(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }
    //Request-Response: Один запрос – один ответ
    @MessageMapping("getDiary")
    public Mono<Diary> getDiary(Long id) {
        return Mono.justOrEmpty(diaryRepository.findById(id));
    }
    //Request-Response: Один запрос – один ответ
    @MessageMapping("addDiary")
    public Mono<Diary> addDiary(Diary diary) {
        return Mono.justOrEmpty(diaryRepository.save(diary));
    }
    //Request-Stream: Один запрос – множественный ответ
    @MessageMapping("getDiaries")
    public Flux<Diary> getDiaries() {
        return Flux.fromIterable(diaryRepository.findAll());
    }
    //FireAndForget: Запрос без ответа
    @MessageMapping("deleteDiary")
    public Mono<Void> deleteDiary(Long id) {
        diaryRepository.deleteById(id);
        return Mono.empty();
    }
    //Channel: Канал
    @MessageMapping("diaryChannel")
    public Flux<Diary> diaryChannel(Flux<Diary> diaries) {
        return diaries.flatMap(diary -> Mono.fromCallable(() -> diaryRepository.save(diary)))
                .collectList()
                .flatMapMany(savedDiaries -> Flux.fromIterable(savedDiaries));
    }
}
