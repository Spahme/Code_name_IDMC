package codename.idmc.infrastructure.network;

public final class ApiEndPoint {

    private ApiEndPoint() {}

    public static final String BASE_URL =
            "https://sasha-daza.fr/APP/code_name/api";

    public static final String INDEX =
            BASE_URL + "/";

    public static final String DIFFICULTIES =
            BASE_URL + "/difficulties";

    public static final String CATEGORIES =
            BASE_URL + "/categories";

    public static final String CARDS =
            BASE_URL + "/cards";

    public static final String ESTABLISHMENTS =
            BASE_URL + "/establishments";

    public static String cardsSearch(
            Integer diffId,
            Integer categoryId,
            Integer establishmentId
    ) {

        StringBuilder url = new StringBuilder(BASE_URL + "/cards-search");

        boolean first = true;

        if (diffId != null) {
            url.append(first ? "?" : "&")
               .append("diff_id=")
               .append(diffId);
            first = false;
        }

        if (categoryId != null) {
            url.append(first ? "?" : "&")
               .append("category_id=")
               .append(categoryId);
            first = false;
        }

        if (establishmentId != null) {
            url.append(first ? "?" : "&")
               .append("establishment_id=")
               .append(establishmentId);
        }

        return url.toString();
    }
}