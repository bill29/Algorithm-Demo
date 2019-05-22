package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;



public class HomeController implements Initializable{
	@FXML
	RadioButton wButton, uwButton, dButton, udButton;
	public static boolean directed = false, undirected = false, weighted = false, unweighted = false;
	@FXML
	Button loginButton;
	static ViewController graphPane;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		dButton.setSelected(directed);
		udButton.setSelected(undirected);
		wButton.setSelected(weighted);
		uwButton.setSelected(unweighted);
        homeAction();		
	}
	
	public void homeAction() {
		
		dButton.setOnAction(e -> {
            directed = true;
            undirected = false;
            udButton.setSelected(false);
            System.out.println("dButton");
        });
        udButton.setOnAction(e -> {
            directed = false;
            undirected = true;
            dButton.setSelected(false);
            System.out.println("udButton");
        });
        wButton.setOnAction(e -> {
            weighted = true;
            unweighted = false;
            uwButton.setSelected(false);
            System.out.println("wButton");
        });
        uwButton.setOnAction(e -> {
            weighted = false;
            unweighted = true;
            wButton.setSelected(false);
            System.out.println("uwButton");
        });
        loginButton.setOnAction(e -> {
        	if ((directed || undirected) && (weighted|| unweighted)) {
        		try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
                    Parent root = loader.load();
                    Scene newScene = new Scene(root);
                    graphPane = loader.getController();

                    
                    Main.primaryStage.setScene(newScene);
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
        	} else {
        		Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setContentText("Please enter the graph format");

				alert.showAndWait();
        	}
        	
        });
        
	}
	
	
	
    
    
	
}
