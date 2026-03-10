package codename.idmc.infrastructure.network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDto {

    public String name;

    @JsonProperty("diff_id")
    public int diffId;

    @JsonProperty("category_id")
    public int categoryId;

    @JsonProperty("establishment_id")
    public int establishmentId;
}