package codename.idmc.infrastructure.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

    private static final ObjectMapper mapper = new ObjectMapper();

    private ApiClient() {
    }

    public static List<ApiItem> fetch(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            InputStream inputStream = connection.getInputStream();
            JsonNode root = mapper.readTree(inputStream);

            JsonNode arrayNode = extraireListe(root);
            List<ApiItem> results = new ArrayList<>();

            if (arrayNode != null && arrayNode.isArray()) {
                for (JsonNode node : arrayNode) {
                    int id = node.has("id") ? node.get("id").asInt() : 0;
                    String label = extraireLabel(node);
                    results.add(new ApiItem(id, label));
                }
            }

            return results;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static JsonNode extraireListe(JsonNode root) {
        if (root == null) {
            return null;
        }

        if (root.isArray()) {
            return root;
        }

        if (root.has("data") && root.get("data").isArray()) {
            return root.get("data");
        }

        if (root.has("results") && root.get("results").isArray()) {
            return root.get("results");
        }

        if (root.has("items") && root.get("items").isArray()) {
            return root.get("items");
        }

        return null;
    }

    private static String extraireLabel(JsonNode node) {
        if (node == null) {
            return "Inconnu";
        }

        if (node.hasNonNull("name")) {
            return node.get("name").asText();
        }

        if (node.hasNonNull("title")) {
            return node.get("title").asText();
        }

        if (node.hasNonNull("label")) {
            return node.get("label").asText();
        }

        if (node.hasNonNull("description")) {
            return node.get("description").asText();
        }

        return "Item #" + (node.has("id") ? node.get("id").asText() : "?");
    }
}