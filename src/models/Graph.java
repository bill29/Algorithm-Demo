package models;

import java.util.ArrayList;
import java.util.List;

import models.Edge;
import models.Vertex;

public class Graph {
	private List<Vertex> vertexs = new ArrayList<Vertex>();
	private List<Edge> edges = new ArrayList<Edge>();
	public Vertex source;
	public boolean isSolved = false;
	public Graph(List<Vertex> vertexs, List<Edge> edges) {
		super();
		this.vertexs = vertexs;
		this.edges = edges;
	}
	public Graph() {
		super();
	}
	public List<Vertex> getVertexs() {
		return vertexs;
	}



	public List<Edge> getEdges() {
		return edges;
	}

	public void setVertexs(List<Vertex> vertexs) {
		this.vertexs = vertexs;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public void addEdge(Edge new_edge){

		edges.add(new_edge);
        System.out.println("Add Edge: " + new_edge.source.getId() + " and " + new_edge.target.getId());
    }  
	
    public void addVertex(Vertex vertex){
        vertexs.add(vertex);
        System.out.println("Add vertex " + vertex.getId() + " to Graph");
    }
    public void clear(){
        vertexs.clear();
        edges.clear();
        isSolved = false;
        
    }
}
