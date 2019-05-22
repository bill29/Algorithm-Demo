package models;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;

import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.List;

public class VertexCircle extends Circle {
    public Vertex vertex;
    public Point point;
    public javafx.scene.control.Label distance = new javafx.scene.control.Label("Dist. : INFINITY");
//    public Label visitTime = new javafx.scene.control.Label("Visit : 0");
//    public Label lowTime = new javafx.scene.control.Label("Low : 0");
    public javafx.scene.control.Label id;
    public boolean isSelected = false;

    public VertexCircle(double x, double y, double rad, String name, Group canvasGroup, List<VertexCircle> circles) {
        super(x, y, rad);
        vertex = new Vertex(name, this);
        point = new Point((int) x, (int) y);
        id = new Label(name);
        id.setFont(Font.font(20));
        canvasGroup.getChildren().add(id);
        id.setLayoutX(x-5);
        id.setLayoutY(y-13);
        this.setOpacity(0.5);
        this.setId("vertex");
        this.setCursor(Cursor.DEFAULT);
        circles.add(this);
        System.out.println("ADDED: " + circles.size());
    }
}