package com.example.OnlineDiary_RSocket_Client_Soldunova.controllers;

import com.example.OnlineDiary_RSocket_Client_Soldunova.models.Diary;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public DiaryController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @GetMapping("/{id}")
    public Mono<Diary> getCat(@PathVariable Long id) {
        return rSocketRequester
                .route("getDiary")//название метода, который мы указали на сервере в @MessageMapping
                .data(id) //данные, которые хотим передать
                .retrieveMono(Diary.class); //принять Mono класс от сервера
    }
    @PostMapping
    public Mono<Diary> addCat(@RequestBody Diary diary) {
        return rSocketRequester
                .route("addDiary")
                .data(diary)
                .retrieveMono(Diary.class);
    }
    @GetMapping
    public Publisher<Diary> getDiaries() {
        return rSocketRequester
                .route("getDiaries")
                .data(new Diary())
                .retrieveFlux(Diary.class); //принять Flux класс от сервера
    }
    @DeleteMapping("/{id}")
    public Publisher<Void> deleteDiary(@PathVariable Long id){
        return rSocketRequester
                .route("deleteDiary")
                .data(id)
                .send(); //после вызова данного метода, мы отправляем данные, но ничего не получаем в ответ
    }
    @PostMapping("/exp")
    public Flux<Diary> addDiariesMultiple(@RequestBody DiaryListWrapper diaryListWrapper){
        List<Diary> diaryList = diaryListWrapper.getDiaries();
        Flux<Diary> diaries = Flux.fromIterable(diaryList);
        return rSocketRequester
                .route("diaryChannel")
                .data(diaries)
                .retrieveFlux(Diary.class);
    }
}
class DiaryListWrapper{
    private List<Diary> diaries;
    public List<Diary> getDiaries() {
        return diaries;
    }
    public void setDiaries(List<Diary> diaries) {
        this.diaries = diaries;
    }
}
