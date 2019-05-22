package models;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable<Vertex>{

    public String name;
    public List<Edge> adjacents = new ArrayList<>();
    public Vertex previous;
    public VertexCircle circle;
    public double minDistance = Double.POSITIVE_INFINITY;
    public boolean visited;

    public Vertex() {

    }

    public Vertex(String name) {
        this.name = name;
        this.visited = false;
    }

    public Vertex(String name, VertexCircle c) {
        this.name = name;
        this.circle = c;
        this.visited = false;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(minDistance, o.minDistance);
    }
}