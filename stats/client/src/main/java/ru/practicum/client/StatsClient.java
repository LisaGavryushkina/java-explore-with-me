package ru.practicum.client;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.HitForRequestDto;
import ru.practicum.dto.HitForResponseDto;

@Service
public class StatsClient {

    private static final String STATS_SERVER_URL = "http://stats-server:8080";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private final RestTemplate rest;

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(STATS_SERVER_URL))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public HitForResponseDto addHit(HitForRequestDto hitForRequestDto) {
        return post("/hit", hitForRequestDto);
    }

    public List<HitForResponseDto> getStatistics(LocalDateTime start, LocalDateTime end,
                                                 Collection<String> uris, boolean unique) {

        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://stats-server/stats")
                .port(8080)
                .queryParam("start", start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .queryParam("end", end.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .queryParam("uris", uris)
                .queryParam("unique", unique)
                .build();

        return get(uriComponents.toUri());

    }

    private HitForResponseDto post(String path, HitForRequestDto body) {
        HttpEntity<HitForRequestDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<HitForResponseDto> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, HttpMethod.POST, requestEntity, HitForResponseDto.class);

        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            throw new StatsClientException("Не удалось добавить данные для статистики", e);
        }
        return statsServerResponse.getBody();

    }

    private List<HitForResponseDto> get(URI uri) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, defaultHeaders());

        ResponseEntity<List<HitForResponseDto>> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(uri, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            throw new StatsClientException("Не удалось загрузить статистику", e);
        }
        return statsServerResponse.getBody();

    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

//    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response;
//        }
//
//        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
//
//        if (response.hasBody()) {
//            return responseBuilder.body(response.getBody());
//        }
//
//        return responseBuilder.build();
//    }
}
