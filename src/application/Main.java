package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	public static Stage primaryStage;

	@Override
	public void start(Stage Stage) throws Exception{
		try {
			primaryStage = Stage;
			Parent root = FXMLLoader.load(getClass().getResource("/view/GiaoDien.fxml"));
			primaryStage.setTitle("Graph_Algorithms");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
