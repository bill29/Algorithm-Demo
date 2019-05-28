package models;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable<Vertex>{

    private int id;
    public List<Edge> adjacents = new ArrayList<>();
    public Vertex previous;
    private VertexCircle circle;
    public double minDistance = Double.POSITIVE_INFINITY;
    public boolean visited;
    public boolean isSelected = false;
    
    public int getId() {
		return id;
	}


	public VertexCircle getCircle() {
		return circle;
	}

	public void setId(int id) {
		this.id = id;
	}


	public void setCircle(VertexCircle circle) {
		this.circle = circle;
	}

    public Vertex(int id, VertexCircle c) {
        this.id = id;
        this.circle = c;
        this.visited = false;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(minDistance, o.minDistance);
    }
}