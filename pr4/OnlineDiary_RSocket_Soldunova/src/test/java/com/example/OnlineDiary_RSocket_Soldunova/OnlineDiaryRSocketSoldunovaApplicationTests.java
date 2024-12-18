package com.example.OnlineDiary_RSocket_Soldunova;

import com.example.OnlineDiary_RSocket_Soldunova.models.Diary;
import com.example.OnlineDiary_RSocket_Soldunova.repositories.DiaryRepository;
import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OnlineDiaryRSocketSoldunovaApplicationTests {
	@Autowired
	private DiaryRepository diaryRepository;
	private RSocketRequester requester;
	@BeforeEach
	public void setup() {
		requester = RSocketRequester.builder()
				.rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
				.rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
				.rsocketConnector(connector -> connector
						.payloadDecoder(PayloadDecoder.ZERO_COPY)
						.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
				.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
				.tcp("localhost", 5200);
	}
	@AfterEach
	public void cleanup() {
		requester.dispose();
	}
	@Test
	public void testGetDiary() {
		Diary diary = new Diary();
		diary.setTopic("My family");
		diary.setDescription("My family is my greatest support and source of joy. ");

		Diary savedDiary = diaryRepository.save(diary);
		Mono<Diary> result  =requester.route("getDiary")
				.data(savedDiary.getId())
				.retrieveMono(Diary.class);
		assertNotNull(result.block());
	}
	@Test
	public void testAddDiary() {
		Diary diary = new Diary();
		diary.setTopic("My family");
		diary.setDescription("My family is my greatest support and source of joy. ");
		Mono<Diary> result  =requester.route("addDiary")
				.data(diary)
				.retrieveMono(Diary.class);
		Diary savedDiary = result.block();
		assertNotNull(savedDiary);
		assertNotNull(savedDiary.getId());
		assertTrue(savedDiary.getId() > 0);
	}
	@Test
	public void testGetDiaries() {
		Flux<Diary> result  =requester.route("getDiaries")
				.retrieveFlux(Diary.class);
		assertNotNull(result.blockFirst());
	}
	@Test
	public void testDeleteDiary() {
		Diary diary = new Diary();
		diary.setTopic("My family");
		diary.setDescription("My family is my greatest support and source of joy. ");
		Diary savedDiary = diaryRepository.save(diary);
		Mono<Void> result  =requester.route("deleteDiary")
				.data(savedDiary.getId())
				.send();
		result.block();
		Optional<Diary> deletedDiary = diaryRepository.findById(savedDiary.getId());
		assertNotSame(deletedDiary, savedDiary);
	}
}

