package codename.idmc.infrastructure.network.multiplayer.dto;

public class ChatMessageDto {

    private String author;
    private String message;
    private long timestamp;

    public ChatMessageDto() {
    }

    public ChatMessageDto(String author, String message, long timestamp) {
        this.author = author;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}