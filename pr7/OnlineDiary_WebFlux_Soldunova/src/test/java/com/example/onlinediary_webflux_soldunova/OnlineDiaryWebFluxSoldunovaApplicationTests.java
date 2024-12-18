package com.example.onlinediary_webflux_soldunova;

import com.example.onlinediary_webflux_soldunova.controllers.DiaryController;
import com.example.onlinediary_webflux_soldunova.models.Diary;
import com.example.onlinediary_webflux_soldunova.repositories.DiaryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class OnlineDiaryWebFluxSoldunovaApplicationTests {

    @Test
    public void testGetDiaryById() {
        Diary diary = new Diary();
        diary.setId(1L);
        diary.setTopic("Topic1");
        diary.setDescription("Description1");
// Создайте мок репозитория
        DiaryRepository diaryRepository = Mockito.mock(DiaryRepository.class);
        when(diaryRepository.findById(1L)).thenReturn(Mono.just(diary));
// Создайте экземпляр контроллера
        DiaryController diaryController = new DiaryController(diaryRepository);
// Вызовите метод контроллера и проверьте результат
        ResponseEntity<Diary> response = diaryController.getDiaryById(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(diary, response.getBody());
    }
    @Test
    public void testGetAllDiaries() {
        Diary diary1 = new Diary();
        diary1.setId(2L);
        diary1.setTopic("Topic1");
        diary1.setDescription("Description1");

        Diary diary2 = new Diary();
        diary2.setId(1L);
        diary2.setTopic("Topic2");
        diary2.setDescription("Description2");
// Создайте мок репозитория
        DiaryRepository diaryRepository = Mockito.mock(DiaryRepository.class);
        when(diaryRepository.findAll()).thenReturn(Flux.just(diary1, diary2));
// Создайте экземпляр контроллера
        DiaryController diaryController = new DiaryController(diaryRepository);
// Вызовите метод контроллера и проверьте результат
        Flux<Diary> response = diaryController.getAllDiaries();
        assertEquals(2, response.collectList().block().size());
    }
    @Test
    public void testCreateDiary() {
        Diary diary = new Diary();
        diary.setId(1L);
        diary.setTopic("Topic1");
        diary.setDescription("Description1");
// Создайте мок репозитория
        DiaryRepository diaryRepository = Mockito.mock(DiaryRepository.class);
        when(diaryRepository.save(diary)).thenReturn(Mono.just(diary));
// Создайте экземпляр контроллера
        DiaryController diaryController = new DiaryController(diaryRepository);
// Вызовите метод контроллера и проверьте результат
        Mono<Diary> response = diaryController.createDiary(diary);
        assertEquals(diary, response.block());
    }
    @Test
    public void testUpdateDiary() {
        Diary diary = new Diary();
        diary.setId(1L);
        diary.setTopic("Topic1");
        diary.setDescription("Description1");

        Diary updatedDiary = new Diary();
        updatedDiary.setId(1L);
        updatedDiary.setTopic("Topic2");
        updatedDiary.setDescription("Description2");
// Создайте мок репозитория
        DiaryRepository diaryRepository = Mockito.mock(DiaryRepository.class);
        when(diaryRepository.findById(1L)).thenReturn(Mono.just(diary));
        when(diaryRepository.save(diary)).thenReturn(Mono.just(updatedDiary));
// Создайте экземпляр контроллера
        DiaryController diaryController = new DiaryController(diaryRepository);
// Вызовите метод контроллера и проверьте результат
        ResponseEntity<Diary> response = diaryController.updateDiary(1L, updatedDiary).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDiary, response.getBody());
    }
    @Test
    public void testDeleteDiary() {
        Diary diary = new Diary();
        diary.setId(1L);
        diary.setTopic("Topic1");
        diary.setDescription("Description1");
// Создайте мок репозитория
        DiaryRepository diaryRepository = Mockito.mock(DiaryRepository.class);
        when(diaryRepository.findById(1L)).thenReturn(Mono.just(diary));
        when(diaryRepository.delete(diary)).thenReturn(Mono.empty());
// Создайте экземпляр контроллера
        DiaryController diaryController = new DiaryController(diaryRepository);
// Вызовите метод контроллера и проверьте результат
        ResponseEntity<Void> response = diaryController.deleteDiary(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
