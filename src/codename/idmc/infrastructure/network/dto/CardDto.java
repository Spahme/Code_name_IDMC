public class CardDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("diff_id")
    private int diffId;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("establishment_id")
    private int establishmentId;

    public String getName() { return name; }
    public int getDiffId() { return diffId; }
    public int getCategoryId() { return categoryId; }
    public int getEstablishmentId() { return establishmentId; }
}