package MovieUI;

/**
 * This class handles application wide tasks and services.
 */

import EPstorage.Playlist;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import object.MovieInfoObject;
import ui.Ui;

import java.io.IOException;

public class Main extends Application {
    private Stage mainWindow;
    private Scene mainMoviesScene;
    private BorderPane mainLayout;


    public void start(Stage primaryStage) {
        mainWindow = primaryStage;
        setUp();

        // Display window and the root scene
        mainMoviesScene = new Scene(mainLayout);
        mainWindow.setTitle("Entertainment Pro");
        mainWindow.setScene(mainMoviesScene);
        mainWindow.show();
    }

    /**
     * This function sets the scene for the window.
     */
    private void setUp() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MainPage.fxml"));
            mainLayout = loader.load();

            // setup the controller's window and reference to this main application class
            MovieHandler controller = loader.getController();
            controller.setMainApplication(this);
            controller.setWindow(mainWindow);
        } catch (IOException e) {
            Ui.printLine();
            e.printStackTrace();
        }
    }

    /**
     * This function pass over the control to the MovieInfoController class.
     * To view more information on movies or tv shows.
     */
    public void transitToMovieInfoController(MovieInfoObject movie) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MoreInfo.fxml"));
            Pane layout = loader.load();

            // setup controller
            MovieInfoController controller = loader.getController();
            controller.setWindow(mainWindow);
            controller.setMainApplication(this);
            controller.setMovie(movie);
            mainWindow.setScene(new Scene(layout));
        } catch (IOException e) {
            Ui.printLine();
            e.printStackTrace();
        }
    }

    /**
     * This function pass over the control to the Main class from the MovieInfoController class.
     */
    public void transitionBackToMoviesController() {
        mainWindow.setScene(mainMoviesScene);
    }

    public void transitToPlaylistController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistListings.fxml"));
            mainLayout = loader.load();

            // setup the controller's window and reference to this main application class
            PlaylistHandler controller = loader.getController();
            controller.setMainApplication(this);
            controller.setWindow(mainWindow);
            mainWindow.setScene(new Scene(mainLayout));
        } catch (IOException e) {
            Ui.printLine();
            e.printStackTrace();
        }
    }

    public void transitToPlaylistInfoController(Playlist playlist) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistInfo.fxml"));
            Pane layout = loader.load();

            // setup controller
            PlaylistInfoController controller = loader.getController();
            controller.setWindow(mainWindow);
            controller.setMainApplication(this);
            controller.setPlaylist(playlist);
            mainWindow.setScene(new Scene(layout));
        } catch (IOException e) {
            Ui.printLine();
            e.printStackTrace();
        }
    }
}