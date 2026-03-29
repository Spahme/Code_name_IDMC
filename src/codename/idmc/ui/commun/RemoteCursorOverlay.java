package codename.idmc.ui.common;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

public class RemoteCursorOverlay extends Pane {

    private final Map<String, StackPane> remoteCursors = new HashMap<>();

    public RemoteCursorOverlay() {
        setPickOnBounds(false);
        setMouseTransparent(true);
    }

    public void updateCursor(String playerId, String pseudo, double x, double y) {
        StackPane cursorNode = remoteCursors.get(playerId);

        if (cursorNode == null) {
            cursorNode = createCursorNode(pseudo);
            remoteCursors.put(playerId, cursorNode);
            getChildren().add(cursorNode);
        }

        cursorNode.setLayoutX(x);
        cursorNode.setLayoutY(y);
    }

    public void removeCursor(String playerId) {
        StackPane node = remoteCursors.remove(playerId);

        if (node != null) {
            getChildren().remove(node);
        }
    }

    public void clearAll() {
        remoteCursors.clear();
        getChildren().clear();
    }

    private StackPane createCursorNode(String pseudo) {
        Circle circle = new Circle(6);
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);

        Label label = new Label(pseudo);
        label.setStyle(
            "-fx-background-color: rgba(255,255,255,0.9);" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 2 4 2 4;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );
        label.setTranslateX(14);
        label.setTranslateY(-8);

        StackPane container = new StackPane();
        container.setMouseTransparent(true);
        container.getChildren().addAll(circle, label);

        return container;
    }
}