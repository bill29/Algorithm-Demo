package models;

import models.Vertex;

import javafx.scene.control.Label;
import javafx.scene.shape.Shape;



public class Edge {
    public Vertex source, target;
    public double weight;
    public Shape line;
    public Label weightLabel;
    public EdgeArrow edgeArrow;

    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.weight = 0;
    }
    // ---- Undirected ----
    public Edge(Vertex source, Vertex target, double weight, Shape line, Label weiLabel) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.line = line;
        this.weightLabel = weiLabel;
    }
    // ---- Directed -----
	public Edge(Vertex source, Vertex target, double weight, Label weightLabel, EdgeArrow edgeArrow) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.weightLabel = weightLabel;
		this.edgeArrow = edgeArrow;
	}
}
