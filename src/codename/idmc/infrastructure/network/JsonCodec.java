package codename.idmc.infrastructure.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonCodec {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonCodec() {}

    public static String encode(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON encode error", e);
        }
    }

    public static <T> T decode(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON decode error", e);
        }
    }
}