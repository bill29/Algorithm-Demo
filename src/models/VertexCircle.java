package models;

import javafx.scene.Cursor;

import javafx.scene.Group;
import javafx.scene.control.Label;

import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.awt.*;

public class VertexCircle extends Circle {
    public Point point;
    public javafx.scene.control.Label distance = new javafx.scene.control.Label("Dist. : INFINITY");
	public javafx.scene.control.Label id;
    
    public VertexCircle(double x, double y, double rad, String name, Group canvasGroup) {
        super(x, y, rad);
        point = new Point((int) x, (int) y);
        id = new Label(name);
        id.setFont(Font.font(20));
        canvasGroup.getChildren().add(id);
        id.setLayoutX(x-5);
        id.setLayoutY(y-13);
        this.setOpacity(0.5);
        this.setCursor(Cursor.DEFAULT);
        
    }
}