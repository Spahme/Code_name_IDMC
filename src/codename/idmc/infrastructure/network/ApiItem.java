package codename.idmc.infrastructure.network;

public class ApiItem {

    private final int id;
    private final String name;

    public ApiItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}