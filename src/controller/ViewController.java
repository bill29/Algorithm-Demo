package controller;

import java.awt.Point;


import java.io.IOException;


import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;

import application.Main;
import models.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;



public class ViewController implements Initializable{
	@FXML
	private JFXToggleButton dijkstraButton, dfsButton, bfsButton;
	@FXML
	private JFXToggleButton addVertexButton, addEdgeButton;
    @FXML
    private JFXButton clearButton, resetButton;
    @FXML
    private Pane viewer;
    @FXML
    private Group canvasGroup;
    @FXML
    private JFXSlider slider;	
    @FXML
    private Button playPauseButton;	
    @FXML
	private ImageView playPauseImg;
    
    //---------End FXML----------//
	
	SequentialTransition animations ;
	int nVertex = 0;
    int time = 500;
    public boolean weighted = HomeController.weighted, unweighted = HomeController.unweighted, directed = HomeController.directed,
    		undirected = HomeController.undirected, bfs = true, dfs = true, dijkstra = true;

    boolean addVertex = true, addEdge = false, calculate = false, calculated = false;
	boolean playing = false, paused = false;
	
	// --- sourceVertex
	Vertex selectedVertex = null;
	Label sourceText = new Label("Source");
	//--- weight, edgeLine, arrow
	Label weight;
    Line edgeLine;
    EdgeArrow arrow;
    Algorithm algo = new Algorithm();
    Graph graph = new Graph();
    
    //-------------------------------------------//
    public static boolean isOverlapping(MouseEvent e, Point p) {
		double x = e.getX();
        double y = e.getY();

        double boundX = p.getX();
        double boundY = p.getY();

        return (x <= boundX + 2.5*20 && x >= boundX - 2.5*20) && (y <= boundY + 2.5*20 && y >= boundY - 2.5*20);
	}
    

    @FXML
    void forwardHandle(ActionEvent event) {
    	try{
            if (paused && animations != null) {
                if(animations.getStatus() == Animation.Status.PAUSED) {
                    animations.jumpTo(animations.getCurrentTime().add(Duration.millis(time)));
                }
                playing = false;
                paused = true;
                return;
            } else if (!paused && !playing && animations != null) {
                algo.finishAnimation(graph.source);
                animations.pause();
                playing = false;
                paused = true;
                return;
            }
        } catch(Exception e){
            clearHandle(null);
        }
    }
    @FXML
    void playPauseHandle(ActionEvent event) {
    	try{
            if (playing && animations != null && animations.getStatus() == Animation.Status.RUNNING) {
                Image image = new Image(getClass().getResourceAsStream("/view/Image/play_arrow_black_48x48.png"));
                playPauseImg.setImage(image);
                System.out.println("PAUSED");
                animations.pause();
                paused = true;
                playing = false;
                return;
            } else if (paused && animations != null) {
                Image image = new Image(getClass().getResourceAsStream("/view/Image/pause_black_48x48.png"));
                playPauseImg.setImage(image);
                if(animations.getStatus() == Animation.Status.PAUSED)
                    animations.play();
                else if(animations.getStatus() == Animation.Status.STOPPED)
                    animations.playFromStart();
                playing = true;
                paused = false;
                return;
            } else if (bfs || dfs || dijkstra && graph.isSolved) {
                if (graph.source != null) {
                    playing = true;
                    paused = false;
                    Image image = new Image(getClass().getResourceAsStream("/view/Image/pause_black_48x48.png"));
                    playPauseImg.setImage(image);
                    algo.finishAnimation(graph.source);
                }
            }
        } catch(Exception e){
            System.out.println("Error while play/pause: " + e);
            clearHandle(null);
        }
    }
    @FXML
    void backHandle(ActionEvent event) {
    	try{
            if (animations != null) {
                if(animations.getStatus() == Animation.Status.PAUSED) {
                    animations.jumpTo(animations.getCurrentTime().subtract(Duration.millis(time)));
                }
                else if(animations.getStatus() == Animation.Status.STOPPED) {
                    animations.playFromStart();
                    animations.pause();
                    animations.jumpTo(animations.getTotalDuration());
                    animations.jumpTo(animations.getCurrentTime().subtract(Duration.millis(time)));
                }
                playing = false;
                paused = true;
                return;
            }
        } catch(Exception e){
            clearHandle(null);
        }
    }
    @FXML
    void addVertexHandle(ActionEvent event) {
    	addVertex = true;
        addEdge = false;
        calculate = false;
        addVertexButton.setSelected(true);
        addEdgeButton.setSelected(false);
        selectedVertex = null;

        if (unweighted) {
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
        }
        if (weighted) {
            dijkstraButton.setDisable(false);
            dijkstraButton.setSelected(false);
        }
    }

    @FXML
    void addEdgeHandle(ActionEvent event) {
    	addVertex = false;
        addEdge = true;
        calculate = false;
        addVertexButton.setSelected(false);
        addEdgeButton.setSelected(true);

        if (unweighted) {
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
        }
        if (weighted) {
            dijkstraButton.setDisable(false);
            dijkstraButton.setSelected(false);
        }
    }
    boolean edgeExists(Vertex u, Vertex v) {
        for (Edge e : graph.getEdges()) {
            if (e.source == u && e.target == v) {
                return true;
            }
        }
        return false;
    }
    @FXML
    void backHome(ActionEvent event) {
    	try {
            resetHandle(null);
            Parent root = FXMLLoader.load(getClass().getResource("/view/GiaoDien.fxml"));

            Scene scene = new Scene(root);
            Main.primaryStage.setScene(scene);
        } catch (IOException ex) {
            
        }
    }

    @FXML
    void clickAbout(ActionEvent event) {
    	Label secondLabel = new Label("Project OOLT 20182: \n"
                + "\t Nguyễn Đình Sơn - 20163532\n"
                + "\t Nguyễn Minh Dân - 20160605");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 400, 200);


        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        newWindow.initModality(Modality.WINDOW_MODAL);


        newWindow.show();
    }

    @FXML
    void clickHelp(ActionEvent event) {
    	Label secondLabel = new Label("- Select type graph -> Login\n"
    			+ "- Add node -> Add Edge\n"
    			+ "- Select Algorithm\n"
    			+ "- Play\n"
    			+ "- Clear\n"
    			+ "- Select Algorithm"
    			+ "..........\n"
    			+ "Reset\n"
    			+ "BackHome\n"
    			+
    			"before running another algorithm \n  You must clear graph ");
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 400, 200);


        Stage newWindow = new Stage();
        newWindow.setTitle("Help");
        newWindow.setScene(secondScene);


        newWindow.initModality(Modality.WINDOW_MODAL);


        newWindow.show();
    }

    @FXML
    void dfsHandle(ActionEvent event) {
    	addVertex = false;
        addEdge = false;
        addVertexButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addVertexButton.setDisable(true);
        addEdgeButton.setDisable(true);
        playPauseButton.setDisable(false);
        calculate = true;
        clearButton.setDisable(false);
        dfs = true;
        bfs = false;
        dijkstra = false;
        bfsButton.setSelected(false);
        dijkstraButton.setSelected(false);
    }
    @FXML
    void bfsHandle(ActionEvent event) {
    	addVertex = false;
        addEdge = false;
        addVertexButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addVertexButton.setDisable(true);
        addEdgeButton.setDisable(true);
        playPauseButton.setDisable(false);
        calculate = true;
        clearButton.setDisable(false);
        bfs = true;
        dfs = false;
        dijkstra = false;
        dfsButton.setSelected(false);
        dijkstraButton.setSelected(false);
    }

    @FXML
    void dijkstraHandle(ActionEvent event) {
    	addVertex = false;
        addEdge = false;
        addVertexButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addVertexButton.setDisable(true);
        addEdgeButton.setDisable(true);
        playPauseButton.setDisable(false);
        calculate = true;
        clearButton.setDisable(false);
        bfs = false;
        dfs = false;
        dijkstra = true;
        bfsButton.setSelected(false);
        dfsButton.setSelected(false);
    }
    @FXML
    void resetHandle(ActionEvent ev) {
    	clearHandle(null);
        nVertex = 0;
        time = 500;
        canvasGroup.getChildren().clear();
        canvasGroup.getChildren().addAll(viewer);
        selectedVertex = null;
        addVertex = true;
        addEdge = false;
        calculate = false;
        calculated = false;
        slider.setDisable(false);
        addVertexButton.setSelected(true);
        addEdgeButton.setSelected(false);
        addEdgeButton.setDisable(true);
        addVertexButton.setDisable(false);
        clearButton.setDisable(true);
        algo = new Algorithm();
        Image image = new Image(getClass().getResourceAsStream("/view/Image/play_arrow_black_48x48.png"));
        playPauseImg.setImage(image);
        bfsButton.setDisable(true);
        dfsButton.setDisable(true);
        dijkstraButton.setDisable(true);
        playPauseButton.setDisable(true);
        playing = false;
        paused = false;
        graph.clear();
    }
    @FXML
    public void clearHandle(ActionEvent ev) {
    	if(animations != null && animations.getStatus() != Animation.Status.STOPPED)
            animations.stop();
        if(animations != null) animations.getChildren().clear();
        selectedVertex = null;
        calculated = false;
        for (Vertex vertex : graph.getVertexs()) {
            vertex.isSelected = false;
            vertex.visited = false;
//            vertex.previous = null;
            vertex.minDistance = Double.POSITIVE_INFINITY;

            FillTransition ft1 = new FillTransition(Duration.millis(300), vertex.getCircle());
            ft1.setToValue(Color.BLACK);
            ft1.play();
        }
        for (Edge edge : graph.getEdges()) {
            if (undirected) {
                StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), edge.line);
                ftEdge.setToValue(Color.BLACK);
                ftEdge.play();
            } else if (directed) {
                FillTransition ftEdge = new FillTransition(Duration.millis(time), edge.line);
                ftEdge.setToValue(Color.BLACK);
                ftEdge.play();
            }
        }
        canvasGroup.getChildren().remove(sourceText);
        for (Vertex vertex : graph.getVertexs()) {
            canvasGroup.getChildren().remove(vertex.getCircle().distance);
        }

        for(Vertex vertex : graph.getVertexs()) {
        	vertex.getCircle().distance.setText("Distance : INFINITY");
        }

        Image image = new Image(getClass().getResourceAsStream("/view/Image/play_arrow_black_48x48.png"));
        playPauseImg.setImage(image);
//

        addVertexButton.setDisable(false);
        addEdgeButton.setDisable(false);
        addVertexHandle(null);
        bfs = false;
        dfs = false;
        dijkstra = false;
        playing = false;
        paused = false;
        graph.isSolved = false;
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        addEdgeButton.setDisable(true);
        addVertexButton.setDisable(true);
        clearButton.setDisable(true);
        bfsButton.setDisable(true);
        dfsButton.setDisable(true);
        dijkstraButton.setDisable(true);
        
	}
    @FXML
    void handle(MouseEvent ev) {
    	if (addVertex) {
            if (nVertex == 1) {
                addVertexButton.setDisable(false);
            }
            if (nVertex == 2) {
                addEdgeButton.setDisable(false);
                addVertexHandle(null);
            }

            if (!ev.getSource().equals(canvasGroup)) {
                if (ev.getEventType() == MouseEvent.MOUSE_RELEASED && ev.getButton() == MouseButton.PRIMARY) {
                	for(Vertex v : graph.getVertexs()) {
                		if(isOverlapping(ev, v.getCircle().point)) {
                			Alert alert = new Alert(AlertType.WARNING);
            				alert.setTitle("Warning Dialog");
            				alert.setContentText("Overlapping Node can't be created");

            				alert.showAndWait();
        	                return;
                		}
                	}
                    nVertex++;
                    VertexCircle circle = new VertexCircle(ev.getX(), ev.getY(), 1.3, String.valueOf(nVertex), canvasGroup);
                    Vertex vertex = new Vertex(nVertex, circle);
                    graph.addVertex(vertex);
                    canvasGroup.getChildren().add(circle);

                    
					circle.setOnMousePressed(mouseHandler);
                    circle.setOnMouseReleased(mouseHandler);
                    circle.setOnMouseDragged(mouseHandler);
                    circle.setOnMouseExited(mouseHandler);
                    circle.setOnMouseEntered(mouseHandler);

                    ScaleTransition tr = new ScaleTransition(Duration.millis(100), circle);
                    tr.setByX(15f);
                    tr.setByY(15f);
                    tr.setInterpolator(Interpolator.EASE_OUT);
                    tr.play();

                }
            }
        }
    }
    
    Vertex findVertex(VertexCircle circle) {
    	for(Vertex x : graph.getVertexs()) {
        	if(x.getCircle().equals(circle))
        		return x;
        }
		return null;
    	
    }
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {
            VertexCircle circle = (VertexCircle) mouseEvent.getSource();
            Vertex vertex = findVertex(circle);
            
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (!vertex.isSelected) {
                    if (selectedVertex != null) {
                        if (addEdge && !edgeExists(selectedVertex, vertex)) {
                            weight = new Label();
                            //Adds the edge between two selected nodes
                            if (undirected) {
                                edgeLine = new Line(selectedVertex.getCircle().point.x, selectedVertex.getCircle().point.y, circle.point.x, circle.point.y);
                                edgeLine.setStrokeWidth(2);
                                canvasGroup.getChildren().add(edgeLine);
                                edgeLine.setId("line");
                                edgeLine.setCursor(Cursor.DEFAULT);
                                
                                
                            } else if (directed) {
                                arrow = new EdgeArrow(selectedVertex.getCircle().point.x, selectedVertex.getCircle().point.y, circle.point.x, circle.point.y);
                                canvasGroup.getChildren().add(arrow);
                                arrow.setId("arrow");
                                arrow.setCursor(Cursor.DEFAULT);
                                arrow.setStrokeWidth(2);
                            	
                            }

                            //Adds weight between two selected nodes
                            if (weighted) {
                                weight.setLayoutX(((selectedVertex.getCircle().point.x) + (circle.point.x)) / 2);
                                weight.setLayoutY(((selectedVertex.getCircle().point.y) + (circle.point.y)) / 2);

                                TextInputDialog dialog = new TextInputDialog("0");
                                dialog.setTitle(null);
                                dialog.setHeaderText("Enter Weight of the Edge :");
                                dialog.setContentText(null);

                                Optional<String> result = dialog.showAndWait();
                                if (result.isPresent()) {
                                    weight.setText(result.get());
                                } else {
                                    weight.setText("0");
                                }
                                canvasGroup.getChildren().add(weight);
                            } else if (unweighted) {
                                weight.setText("1");
                            }

                            if (undirected) {
                            	Edge new_edge = new Edge(selectedVertex, vertex, Double.valueOf(weight.getText()), edgeLine, weight);
                            	graph.addEdge(new_edge);
                            	Edge new_edge1 = new Edge(vertex, selectedVertex,Double.valueOf(weight.getText()), edgeLine, weight);
                            	graph.addEdge(new_edge1);
                                selectedVertex.adjacents.add(new_edge);
                                vertex.adjacents.add(new_edge1);
                                

                            } else if (directed) {
                            	Edge new_edge = new Edge(selectedVertex, vertex, Double.valueOf(weight.getText()), arrow, weight);
                            	graph.addEdge(new_edge);
                                
                                selectedVertex.adjacents.add(new_edge);
                                
                            }
                        }
                        if (addVertex || addEdge) {
                            selectedVertex.isSelected = false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300), selectedVertex.getCircle(), Color.GREEN, javafx.scene.paint.Color.BLACK);
                            ft1.play();
                        }
                        selectedVertex = null;
                        return;
                    }
                    if(!calculate) {
                        FillTransition ft = new FillTransition(Duration.millis(300), circle, javafx.scene.paint.Color.BLACK, Color.GREEN);
                        ft.play();
                        vertex.isSelected = true;
                        selectedVertex = vertex;
                    }

                    // WHAT TO DO WHEN SELECTED ON ACTIVE ALGORITHM
                    if (calculate && !calculated) {
                        if (bfs) {
                            FillTransition ft1 = new FillTransition(Duration.millis(time), vertex.getCircle());
                            ft1.setToValue(Color.RED);
                            ft1.play();
                            
                            graph.source = vertex;
                            graph.isSolved = true;
                            algo.BFS(graph.source);
                        }
                        else if (dfs) {
                            FillTransition ft1 = new FillTransition(Duration.millis(time), vertex.getCircle());
                            ft1.setToValue(Color.RED);
                            ft1.play();
                            graph.source = vertex;
                            graph.isSolved = true;
                            algo.DFS(graph.source);
                        }
                        else if (dijkstra) {
                            FillTransition ft1 = new FillTransition(Duration.millis(time), vertex.getCircle());
                            ft1.setToValue(Color.RED);
                            ft1.play();
                            graph.source = vertex;
                            graph.isSolved = true;
                            algo.Dijkstra(graph.source);
                        }

                        calculated = true;
                    }
                    else if (calculate && calculated) {

                        for (Vertex n : graph.getVertexs()) {
                            n.isSelected = false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300), n.getCircle());
                            ft1.setToValue(javafx.scene.paint.Color.BLACK);
                            ft1.play();
                        }
                        List<Vertex> path = algo.getShortestPathTo(vertex);
                        for (Vertex n : path) {
                            FillTransition ft1 = new FillTransition(Duration.millis(300), n.getCircle());
                            ft1.setToValue(Color.RED);
                            ft1.play();
                        }
                    }
                } else {
                    vertex.isSelected = false;
                    FillTransition ft1 = new FillTransition(Duration.millis(300), vertex.getCircle(), Color.GREEN, Color.BLACK);
                    ft1.play();
                    selectedVertex = null;
                }

            }
        }

    };

/*Vertex chua duoc duyet thi mau xam
 * SourceVertex red
 * Vertex duoc chon mau blue
 * 
 * */

    public class Algorithm {
        LinkedList<Vertex> list;

        public Algorithm() {
            animations = new SequentialTransition();
            list = new LinkedList<>();
        }

        public void BFS(Vertex source) {
        	System.out.println("-----------------BFS------------");
        	System.out.println("1");
            changeVertexColor(source, Color.BLUE);
            source.minDistance = 0;
            source.visited = true;
            list.push(source);
            while (!list.isEmpty()) {
            	System.out.println("*");
                Vertex u = list.removeLast();
                changeVertexColor(u,Color.RED);
                for (Edge e: u.adjacents) {
                    if (e != null) {
                        Vertex v = e.target;
                        if (!v.visited) {
                            v.visited = true;
                            list.push(v);
                            v.previous = u;
                            changeEdgeColor(e,Color.BLUE);
                            VertexVisitingFinish(v,Color.BLUE);
                            
                        }
                    }
                }
                changeVertexColor(u,Color.GREEN);
                
            }

        }

        public void DFS(Vertex source) {
        	System.out.println("---------------DFS-------------------");
            source.minDistance = 0;
            source.visited = true;
            list.push(source);
            DFSRecursion(source);
        }

        public void DFSRecursion(Vertex source) {
            
            changeVertexColor(source,Color.RED);

            for (Edge e : source.adjacents) {
                if (e != null) {
                    Vertex v = e.target;
                    if (!v.visited) {
                        v.visited = true;
                        v.previous = source;
                        changeEdgeColor(e,Color.BLUE);
                        DFSRecursion(v);

                        VertexVisitingFinish(v,Color.GREEN);
                        changeEdgeColor(e,Color.GREEN);
                        
                    }
                }
            }
            changeVertexColor(source,Color.GREEN);


        }

        public void Dijkstra(Vertex source) {
        	System.out.println("------------Dijkstra-------------");
            for (Vertex vertex : graph.getVertexs()) {    
                vertex.getCircle().distance.setLayoutX(vertex.getCircle().point.x + 20);
                vertex.getCircle().distance.setLayoutY(vertex.getCircle().point.y);
                canvasGroup.getChildren().add(vertex.getCircle().distance);
            }
            //IN RA DISTANCE CUA CAC DUONG DI.
            sourceText.setLayoutX(source.getCircle().point.x + 20);
            sourceText.setLayoutY(source.getCircle().point.y + 10);
            canvasGroup.getChildren().add(sourceText);
            source.getCircle().distance.setText("Dist. : " + 0);
            //</editor-fold>
            source.minDistance = 0;
            list.push(source);
            while (!list.isEmpty()) {
                Vertex u = list.removeLast();
                changeVertexColor(u, Color.RED);
                for (Edge e : u.adjacents) {
                    if (e != null) {
                        Vertex v = e.target;
                        if (u.minDistance + e.weight < v.minDistance) {
                            //list.remove(v);
                            v.minDistance = u.minDistance + e.weight;
                            v.previous = u;
                            list.push(v);
                            changeEdgeColor(e,Color.FORESTGREEN);
                            VertexVisitingFinish(v,Color.BLUE);
                        }
                    }
                }
                changeVertexColor(u,Color.GREEN);
                
            }
        }

        public void finishAnimation(Vertex source) {
            animations.setOnFinished(ev -> {
                for (Vertex vertex : graph.getVertexs()) {
                    FillTransition ft1 = new FillTransition(Duration.millis(time), vertex.getCircle());
                    ft1.setToValue(Color.BLACK);
                    ft1.play();
                }
                if (directed) {
                    for (Edge edge :  graph.getEdges()) {
                        edge.line.setFill(Color.BLACK);
                    }
                } else if (undirected) {
                    for (Edge edge : graph.getEdges()) {
                        edge.line.setStroke(Color.BLACK);
                    }
                }
                FillTransition ft1 = new FillTransition(Duration.millis(time), graph.source.getCircle());
                ft1.setToValue(Color.RED);
                ft1.play();
                Image image1 = new javafx.scene.image.Image(getClass().getResourceAsStream("/view/Image/play_arrow_black_48x48.png"));
                playPauseImg.setImage(image1);
                paused = true;
                playing = false;
            });
            animations.onFinishedProperty();
            animations.play();
            playing = true;
            paused = false;
        } 
        //thao tac de chuyen canh
        public void changeEdgeColor( Edge e, Color color) {
            if (undirected) {
                StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                ftEdge.setToValue(color);
                animations.getChildren().add(ftEdge);
            } else if (directed) {
                FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                ftEdge.setToValue(color);
                animations.getChildren().add(ftEdge);
            }

        }

        public void changeVertexColor( Vertex v, Color color) {
            FillTransition ftVertex = new FillTransition(Duration.millis(time),v.getCircle());
            ftVertex.setToValue(color);
            animations.getChildren().add(ftVertex);
            
        }

        public void VertexVisitingFinish(Vertex v, Color color) {
            FillTransition ft = new FillTransition(Duration.millis(time), v.getCircle());
            ft.setToValue(color);
            ft.setOnFinished(ev -> {
                v.getCircle().distance.setText("Dist. : " + v.minDistance);
            });
            ft.onFinishedProperty();
            animations.getChildren().add(ft);
            System.out.println("Vertex " + v.getId() + " finised");
        }

        public List<Vertex> getShortestPathTo(Vertex target) {
            List<Vertex> shortestPath = new ArrayList<>();
            for (Vertex i = target; i != null; i = i.previous) {
                shortestPath.add(i);
            }
            Collections.reverse(shortestPath);
            return shortestPath;
        }
    }
	

}

