package MovieUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
}