package MovieUI;

import Contexts.CommandContext;
import Contexts.ContextHelper;
import EPparser.CommandParser;
import EPstorage.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import movieRequesterAPI.RetrieveRequest;
import object.MovieInfoObject;
import ui.Ui;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is main page of GUI.
 */
public class PlaylistHandler extends Controller {

    @FXML
    private ScrollPane PlaylistScrollPane;

    @FXML
    private Text text;

    @FXML VBox playlistVBox;

    @FXML Label userNameLabel;
    @FXML Label userAgeLabel;
    @FXML Label adultLabel;
    @FXML TextFlow genreListText;
    @FXML Label playlistLabel;

    private UserProfile userProfile;
    private ArrayList<String> playlists;

    private FlowPane mPlaylistFlowPane;

    @FXML
    private Label mStatusLabel;

    @FXML
    private ProgressBar mProgressBar;

    @FXML
    private TextField mSearchTextField;

    private int num = 0;


    class KeyboardClick implements EventHandler<KeyEvent> {

        private Controller control;

        KeyboardClick(Controller control) {
            this.control = control;
        }

        /**
         * Handles user's inputs and respond appropriately.
         *
         * @param event consist of user's inputs.
         */
        @Override
        public void handle(KeyEvent event) {

            System.out.println("You Pressing : " + ((KeyEvent) event).getCode() );
            if (event.getCode().equals(KeyCode.ENTER)) {
                System.out.println("Hello");
                try {
                    if (mSearchTextField.getText().equals("go back")) {
                        backToMoviesButtonClick();
//                        backToPlaylistButtonClick();
                    } else {
                        CommandParser.parseCommands(mSearchTextField.getText(), control);
                        clearSearchTextField();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getCode().equals(KeyCode.TAB)) {
                System.out.println("Tab presjenksjessed");
                event.consume();
            } else if (event.getCode().equals(KeyCode.DOWN)) {

                //mMoviesScrollPane.setOnScroll(new EventHandler<ScrollEvent>() {
                  //  @Override
                    //public void handle(ScrollEvent event) {
                      //  mMoviesScrollPane.setVvalue(mMoviesScrollPane.getVvalue() + 0.5);
                    //}
                //});
                PlaylistScrollPane.requestFocus();
            }
        }

    }

    @FXML public void setLabels() throws IOException {
        EditProfileJson editProfileJson = new EditProfileJson();
        userProfile = editProfileJson.load();
        playlists = userProfile.getPlaylistNames();
        ProfileCommands command = new ProfileCommands(userProfile);
        userNameLabel.setText(userProfile.getUserName());
        userAgeLabel.setText(Integer.toString(userProfile.getUserAge()));
        //setting adult label
        if (command.getAdultLabel().equals("allow")) {
            adultLabel.setStyle("-fx-text-fill: \"#48C9B0\";");
        }
        if (command.getAdultLabel().equals("restrict")) {
            adultLabel.setStyle("-fx-text-fill: \"#EC7063\";");
        }
        adultLabel.setText(command.getAdultLabel());
        //setting text for preference & restrictions
        Text preferences = new Text(command.convertToLabel(userProfile.getGenreIdPreference()));
        preferences.setFill(Paint.valueOf("#48C9B0"));
        Text restrictions = new Text(command.convertToLabel(userProfile.getGenreIdRestriction()));
        restrictions.setFill(Paint.valueOf("#EC7063"));
        genreListText.getChildren().clear();
        genreListText.getChildren().addAll(preferences, restrictions);
        playlistLabel.setText(Integer.toString(userProfile.getPlaylistNames().size()));
    }

    @FXML public void initialize() throws IOException {
        setLabels();
        CommandContext.initialiseContext();
//        buildPlaylistFlowPane(playlists);
        buildPlaylistVBox(playlists);

        mSearchTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                System.out.println("Tab pressed");


                setFeedbackText(ContextHelper.getAllHints(mSearchTextField.getText() , this));
                event.consume();
            } else if (event.getCode().equals(KeyCode.ENTER)) {
                System.out.println("Enter pressed");
            }
        });

        text.setText("Welcome to Entertainment Pro. Here are your playlists hehe :)");

        //Real time changes to text field
        mSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        System.out.println(text.getText());

        //Enter is Pressed
        mSearchTextField.setOnKeyPressed(new KeyboardClick(this));
        PlaylistScrollPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.UP)) {
                    double num = (double) PlaylistScrollPane.getVvalue();
                    if (num == 0) {
                        mSearchTextField.requestFocus();
                    }
                } else if (event.getCode().equals(KeyCode.DOWN)) {
                    System.out.println("yesssx");
                    playlistVBox.getChildren().get(num).requestFocus();
                    num += 1;
                    //mMoviesFlowPane.getChildren().get(num).();
                } else if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
//                        if (mSearchTextField.getText().equals("go back")) {
//                            mMainApplication.transitionBackToMoviesController();
//                            CommandParser.parseCommands(mSearchTextField.getText(), control);
////                            backToMoviesButtonClick();
////                            backToPlaylistButtonClick();
//                        } else {
                            playlistPaneClicked(new EditPlaylistJson(playlists.get(0)).load());
                            System.out.println("jajajajaajajaj");
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    moviePosterClicked(mMovies.get(num));
                }
            }
        });
    }

//    private void buildPlaylistFlowPane(ArrayList<String> playlists) throws IOException {
//        // Setup progress bar and status label
//        mProgressBar.setProgress(0.0);
//        mProgressBar.setVisible(true);
//        mStatusLabel.setText("Loading..");
//
//        // Build a flow pane layout with the width and size of the
//        mPlaylistFlowPane = new FlowPane(Orientation.VERTICAL);
//        mPlaylistFlowPane.setHgap(4);
//        mPlaylistFlowPane.setVgap(10);
//        mPlaylistFlowPane.setPadding(new Insets(10, 8, 4, 8));
//        mPlaylistFlowPane.prefWrapLengthProperty().bind(PlaylistScrollPane.widthProperty());   // bind to scroll pane width
//
//        for (String log : playlists) {
//            Playlist playlist = new EditPlaylistJson(log).load();
//            AnchorPane playlistPane = buildPlaylistPane(playlist);
//            mPlaylistFlowPane.getChildren().add(playlistPane);
//            System.out.println(playlist.getMovies().size());
//        }
//
//        PlaylistScrollPane.setContent(mPlaylistFlowPane);
//        PlaylistScrollPane.setVvalue(0);
//    }

    private void buildPlaylistVBox(ArrayList<String> playlists) throws IOException {
        // Setup progress bar and status label
        mProgressBar.setProgress(0.0);
        mProgressBar.setVisible(true);
        mStatusLabel.setText("Loading..");

        int count = 1;
        for (String log : playlists) {
            Playlist playlist = new EditPlaylistJson(log).load();
            AnchorPane playlistPane = buildPlaylistPane(playlist, count);
            playlistVBox.getChildren().add(playlistPane);
            count++;
//            mPlaylistFlowPane.getChildren().add(playlistPane);
//            System.out.println(playlist.getMovies().size());
        }

        PlaylistScrollPane.setContent(playlistVBox);
        PlaylistScrollPane.setVvalue(0);
    }

    private AnchorPane buildPlaylistPane(Playlist playlist, int i) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistPane.fxml"));
            AnchorPane playlistPane = loader.load();
//            playlistPane.setRightAnchor(00);
//            posterView.setOnScroll();
            playlistPane.setOnMouseClicked((mouseEvent) -> {
                try {
                    playlistPaneClicked(playlist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // set the movie info
            PlaylistPaneController controller = loader.getController();
            controller.setVBoxColour(i);
            controller.getPlaylistNameLabel().setText(playlist.getPlaylistName());
            if (playlist.getDescription().trim().length() == 0) {
                controller.getPlaylistDescriptionLabel().setStyle("-fx-font-style: italic");
                controller.getPlaylistDescriptionLabel().setText("*this playlist does not have a description :(*");
            } else {
                controller.getPlaylistDescriptionLabel().setText(playlist.getDescription());
            }
            controller.getPlaylistMoviesLabel().setText("No. of movies: " + Integer.toString(playlist.getMovies().size()));
            return playlistPane;
        } catch (IOException ex) {
            Ui.printLine();
        }
        return null;
    }

//    /**
//     * This function updates the progress bar as the movie poster is being displayed.
//     * @param movie Object that contains all the information about a particular movie.
//     * @param progress contains the progress value.
//     */
//    private void updateProgressBar(MovieInfoObject movie, double progress) {
//        // update the progress for that movie in the array
//        int index = mMovies.indexOf(movie);
//        if (index >= 0) {
//            mImagesLoadingProgress[index] = progress;
//        }
//
//        double currentTotalProgress = 0.0;
//        for (double value : mImagesLoadingProgress) {
//            currentTotalProgress += value;
//        }
//
//        //System.out.println("Current total progress: " + currentTotalProgress);
//        mProgressBar.setProgress((currentTotalProgress / mMovies.size()));
//
//        if (currentTotalProgress >= mMovies.size()) {
//            mProgressBar.setVisible(false);
//            mStatusLabel.setText("");
//        }
//    }

    private void playlistPaneClicked(Playlist playlist) throws IOException {
        mMainApplication.transitToPlaylistInfoController(playlist);
    }

    /**
     * This function clears the searchTextField when called.
     */
    public void clearSearchTextField() {
        mSearchTextField.setText("");
    }

    /**
     * Prints message in UI.
     * @param txt which is the string text to be printed.
     */
    public void setFeedbackText(String txt) {
        text.setText(txt);
    }

    public void updateTextField(String updateStr){
        mSearchTextField.setText(mSearchTextField.getText() + updateStr);
        mSearchTextField.positionCaret(mSearchTextField.getText().length());
    }

    // User clicks on the back button to navigate back to the movies scene
    public void backToMoviesButtonClick() {
        mMainApplication.transitionBackToMoviesController();
    }

    public void setFeedbackText(ArrayList<String> txtArr){
        String output = "";
        for(String s: txtArr){
            output += s;
            output += "\n";

        }
        text.setText(output);
    }

    @FXML private void clearSearchButtonClicked()
    {
        mSearchTextField.clear();
    }


    // Menu item events
    @FXML
    public void exitMenuItemClicked() {
        System.exit(0);
    }

    @FXML
    public void aboutMenuItemClicked() {
    }

    private void goToPlaylistListing() {
        mMainApplication.transitToPlaylistController();
    }

}