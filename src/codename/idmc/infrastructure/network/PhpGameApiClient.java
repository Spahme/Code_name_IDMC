package codename.idmc.infrastructure.network;

import codename.idmc.infrastructure.network.dto.ApiResponseDto;
import codename.idmc.infrastructure.network.dto.CardDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.Arrays;
import java.util.List;

public class PhpGameApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public PhpGameApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public List<CardDto> searchCards(
            Integer diffId,
            Integer categoryId,
            Integer establishmentId
    ) throws IOException, InterruptedException {

        String url = ApiEndPoint.cardsSearch(
                diffId,
                categoryId,
                establishmentId
        );

        String json = get(url);

        ApiResponseDto<CardDto[]> response =
                mapper.readValue(json,
                        new TypeReference<ApiResponseDto<CardDto[]>>() {});

        return Arrays.asList(response.data);
    }

    private String get(String url)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "HTTP Error " + response.statusCode()
            );
        }

        return response.body();
    }
}