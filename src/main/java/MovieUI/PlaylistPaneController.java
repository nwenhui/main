package MovieUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Controller to get and set parameters in the MoviePoster View.
 */
public class PlaylistPaneController {
    @FXML
    private Label playlistNameLabel;

    @FXML
    private Label playlistDescriptionLabel;

    @FXML
    private Label playlistMoviesLabel;

    @FXML
    private VBox individualPlaylistVBox;

    private final String C1 = "#B0E0E6";
    private final String C2 = "#D8BFD8";

    public Label getPlaylistNameLabel() {
        return playlistNameLabel;
    }

    public void setPlaylistNameLabel(Label playlistNameLabel) {
        this.playlistNameLabel = playlistNameLabel;
    }

    public Label getPlaylistDescriptionLabel() {
        return playlistDescriptionLabel;
    }

    public void setPlaylistDescriptionLabel(Label playlistDescriptionLabel) {
        this.playlistDescriptionLabel = playlistDescriptionLabel;
    }

    public Label getPlaylistMoviesLabel() {
        return playlistMoviesLabel;
    }

    public void setPlaylistMoviesLabel(Label playlistMoviesLabel) {
        this.playlistMoviesLabel = playlistMoviesLabel;
    }

    public void setVBoxColour(int i) {
        if (i % 2 == 0) {
            individualPlaylistVBox.setStyle("-fx-background-color: " + C1);
        } else {
            individualPlaylistVBox.setStyle("-fx-background-color: " + C2);
        }
    }
}