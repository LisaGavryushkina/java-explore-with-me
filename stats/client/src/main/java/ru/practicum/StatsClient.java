package ru.practicum;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
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

import static java.util.stream.Collectors.toList;

@Service
public class StatsClient {

    private static final String STATS_SERVER_URL = "http://stats-server:9090";
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

    public List<HitForResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                 boolean unique) {

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        try {
            URIBuilder uriBuilder = new URIBuilder("http://stats-server/stats");
            uriBuilder.setPort(9090);
            uriBuilder.addParameters(parameters.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != null)
                    .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue().toString()))
                    .collect(toList()));

            URL url = uriBuilder.build().toURL();
            return get(url.getPath(), parameters);
        } catch (URISyntaxException | MalformedURLException e) {
            throw new StatsClientException("Не удалось загрузить статистику");
        }
//        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    private HitForResponseDto post(String path, HitForRequestDto body) {
        HttpEntity<HitForRequestDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<HitForResponseDto> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, HttpMethod.POST, requestEntity, HitForResponseDto.class);

        } catch (HttpStatusCodeException e) {
            throw new StatsClientException("Не удалось добавить данные для статистики");
        }
        return statsServerResponse.getBody();

    }

    private List<HitForResponseDto> get(String path, Map<String, Object> parameters) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, defaultHeaders());

        ResponseEntity<List<HitForResponseDto>> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
                    }, parameters);

        } catch (HttpStatusCodeException e) {
            throw new StatsClientException("Не удалось загрузить статистику");
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
