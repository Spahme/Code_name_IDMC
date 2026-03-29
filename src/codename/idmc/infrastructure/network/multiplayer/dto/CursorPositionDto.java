package codename.idmc.infrastructure.network.multiplayer.dto;

public class CursorPositionDto {

    private double x;
    private double y;
    private String pseudo;

    public CursorPositionDto() {
    }

    public CursorPositionDto(double x, double y, String pseudo) {
        this.x = x;
        this.y = y;
        this.pseudo = pseudo;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}