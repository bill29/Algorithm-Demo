package models;

import models.Vertex;
import javafx.scene.control.Label;
import javafx.scene.shape.Shape;


public class Edge {
    public Vertex source, target;
    public double weight;
    public Shape line;
    public Label weightLabel;

    public Shape getLine() {
        return line;
    }

    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.weight = 0;
    }

    public Edge(Vertex source, Vertex target, double weight, Shape line, Label weiLabel) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.line = line;
        this.weightLabel = weiLabel;
    }
}
