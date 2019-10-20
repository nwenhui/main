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
    private VBox mMovieTypeVBox;

    @FXML
    private Label titleLabel;

    @FXML
    private Label titleLabel2;

    @FXML
    private Text ageText;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text text;

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
    private VBox vbox0;

    @FXML
    private ProgressBar mProgressBar;

    @FXML
    private TextField mSearchTextField;

    private ArrayList<MovieInfoObject> mMovies;

    private double[] mImagesLoadingProgress;

    private static RetrieveRequest mMovieRequest;
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
                    CommandParser.parseCommands(mSearchTextField.getText(), control);
                    clearSearchTextField();
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
//        EditPlaylistJson editPlaylistJson = new EditPlaylistJson();
//        playlists = editPlaylistJson.load();
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
//        mMovieRequest = new RetrieveRequest(this);
        CommandContext.initialiseContext();
        buildPlaylistFlowPane(playlists);

        mSearchTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                System.out.println("Tab pressed");


                setFeedbackText(ContextHelper.getAllHints(mSearchTextField.getText() , this));
                event.consume();
            } else if (event.getCode().equals(KeyCode.ENTER)) {
                System.out.println("Enter pressed");
            }
        });

//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_MOVIES, userProfile.isAdult());
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
                    mPlaylistFlowPane.getChildren().get(num).requestFocus();
                    num += 1;
                    //mMoviesFlowPane.getChildren().get(num).();
                } else if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
                        playlistPaneClicked(new EditPlaylistJson(playlists.get(0)).load());
                        System.out.println("jajajajaajajaj");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    moviePosterClicked(mMovies.get(num));
                }
            }
        });
    }

//    /**
//     * This function is called when data for the movies/tv shows has been fetched.
//     */
//    @Override
//    public void requestCompleted(ArrayList<MovieInfoObject> moviesInfo) {
//        // Build the Movie poster views and add to the flow pane on the main thread
//        System.out.print("Request received");
//        final ArrayList<MovieInfoObject> MoviesFinal = Blacklist.filter(moviesInfo);
//        for(MovieInfoObject mf: MoviesFinal){
//
//            System.out.println(mf.getTitle());
//        }
//        System.out.print("Request rsdceceived");
//        SearchResultContext.addResults(MoviesFinal);
//        mMovies = MoviesFinal;
//        mImagesLoadingProgress = new double[mMovies.size()];
//        Platform.runLater(() -> buildMoviesFlowPane(MoviesFinal));
//
//    }

//    public void displayMovies(){
//        mMovies = SearchResultContext.getMoviesToDisplay();
//        mImagesLoadingProgress = new double[mMovies.size()];
//        Platform.runLater(() -> buildMoviesFlowPane(SearchResultContext.getMoviesToDisplay()));
//    }

//    /**
//     * This function is called when data for the movies/tv shows failed to fetch due to timed out.
//     */
//    @Override
//    public void requestTimedOut() {
//        Platform.runLater(() -> showDownloadFailureAlert("Request timed out"));
//    }
//
//    /**
//     * This function is called when data for the movies/tv shows failed due to internet connection
//     */
//    @Override
//    public void requestFailed() {
//        Platform.runLater(() -> showDownloadFailureAlert("No internet connection"));
//    }
//
//    /**
//     * This funcion is called to print a message when the data for movies/tv shows is unavailable due to
//     * no internet connection.
//     * @param headerText consists of the string to be printed.
//     */
//    private void showDownloadFailureAlert(String headerText) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Failed to download");
//        alert.setHeaderText(headerText);
//        alert.setContentText("Please ensure you have an active internet connection.");
//        alert.showAndWait();
//    }

//    /**
//     * This function initalises the progress bar and extracts movie posters fro every movie.
//     * @param movies is a array containing details about every movie/tv show that is being displayed.
//     */
//    private void buildMoviesFlowPane(ArrayList<MovieInfoObject> movies) {
//        // Setup progress bar and status label
//        mProgressBar.setProgress(0.0);
//        mProgressBar.setVisible(true);
//        mStatusLabel.setText("Loading..");
//
//        // Build a flow pane layout with the width and size of the
//        mPlaylistFlowPane = new FlowPane(Orientation.HORIZONTAL);
//        mPlaylistFlowPane.setHgap(4);
//        mPlaylistFlowPane.setVgap(10);
//        mPlaylistFlowPane.setPadding(new Insets(10, 8, 4, 8));
//        mPlaylistFlowPane.prefWrapLengthProperty().bind(PlaylistScrollPane.widthProperty());   // bind to scroll pane width
//
//        for (int i = 0; i < movies.size(); i++) {
//            AnchorPane posterPane = buildMoviePosterPane(movies.get(i), i + 1);
//            mPlaylistFlowPane.getChildren().add(posterPane);
//        }
//
//        PlaylistScrollPane.setContent(mPlaylistFlowPane);
//        PlaylistScrollPane.setVvalue(0);
//    }

    private void buildPlaylistFlowPane(ArrayList<String> playlists) throws IOException {
        // Setup progress bar and status label
        mProgressBar.setProgress(0.0);
        mProgressBar.setVisible(true);
        mStatusLabel.setText("Loading..");

        // Build a flow pane layout with the width and size of the
        mPlaylistFlowPane = new FlowPane(Orientation.VERTICAL);
        mPlaylistFlowPane.setHgap(4);
        mPlaylistFlowPane.setVgap(10);
        mPlaylistFlowPane.setPadding(new Insets(10, 8, 4, 8));
        mPlaylistFlowPane.prefWrapLengthProperty().bind(PlaylistScrollPane.widthProperty());   // bind to scroll pane width

        for (String log : playlists) {
            Playlist playlist = new EditPlaylistJson(log).load();
            AnchorPane playlistPane = buildPlaylistPane(playlist);
//            AnchorPane.setLeftAnchor(PlaylistScrollPane, 0.0);
//            AnchorPane.setRightAnchor(PlaylistScrollPane, 20.0);
//            playlistPane.setStyle("-fx-background-colour: #424747");
            mPlaylistFlowPane.getChildren().add(playlistPane);
            System.out.println(playlist.getMovies().size());
        }

        PlaylistScrollPane.setContent(mPlaylistFlowPane);
        PlaylistScrollPane.setVvalue(0);
    }

    private AnchorPane buildPlaylistPane(Playlist playlist) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistPane.fxml"));
            AnchorPane playlistPane = loader.load();
            //posterView.setOnScroll();
            playlistPane.setOnMouseClicked((mouseEvent) -> {
                System.out.println("hellooooooo");
                System.out.println(playlist.getMovies().get(0).getTitle());
                try {
                    playlistPaneClicked(playlist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // set the movie info
            PlaylistPaneController controller = loader.getController();
//            Image posterImage = new Image(movie.getFullPosterPath(), true);
//            posterImage.progressProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(movie, newValue.doubleValue()));

            controller.getPlaylistNameLabel().setText(playlist.getPlaylistName());
            controller.getPlaylistDescriptionLabel().setText(playlist.getDescription());
            controller.getPlaylistMoviesLabel().setText(Integer.toString(playlist.getMovies().size()));
            return playlistPane;
        } catch (IOException ex) {
            Ui.printLine();
        }

        return null;
    }


//    /**
//     * @param movie a object that contains information about a movie
//     * @param index a unique number assigned to every movie/tv show that is being displayed.
//     * @return anchorpane consisting of the movie poster, name and the unique id.
//     */
//
//    private AnchorPane buildMoviePosterPane(MovieInfoObject movie, int index) {
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getClassLoader().getResource("MoviePoster.fxml"));
//            AnchorPane posterView = loader.load();
//            //posterView.setOnScroll();
//            posterView.setOnMouseClicked((mouseEvent) -> moviePosterClicked(movie));
//
//            // set the movie info
//            MoviePosterController controller = loader.getController();
//            Image posterImage = new Image(movie.getFullPosterPath(), true);
//            posterImage.progressProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(movie, newValue.doubleValue()));
//
//            controller.getMovieTitleLabel().setText(movie.getTitle());
//            controller.getPosterImageView().setImage(posterImage);
//            controller.getMovieNumberLabel().setText(Integer.toString(index));
//            return posterView;
//        } catch (IOException ex) {
//            Ui.printLine();
//        }
//
//        return null;
//    }

    /**
     * This funcion updates the progress bar as the movie poster is being displayed.
     * @param movie Object that contains all the information about a particular movie.
     * @param progress contains the progress value.
     */
    private void updateProgressBar(MovieInfoObject movie, double progress) {
        // update the progress for that movie in the array
        int index = mMovies.indexOf(movie);
        if (index >= 0) {
            mImagesLoadingProgress[index] = progress;
        }

        double currentTotalProgress = 0.0;
        for (double value : mImagesLoadingProgress) {
            currentTotalProgress += value;
        }

        //System.out.println("Current total progress: " + currentTotalProgress);
        mProgressBar.setProgress((currentTotalProgress / mMovies.size()));

        if (currentTotalProgress >= mMovies.size()) {
            mProgressBar.setVisible(false);
            mStatusLabel.setText("");
        }
    }


//    /**
//     * Tis function is called when the user wants to see more information about a movie.
//     */
//    private void moviePosterClicked(MovieInfoObject movie) {
//
//        mMainApplication.transitToMovieInfoController(movie);
//    }

    private void playlistPaneClicked(Playlist playlist) throws IOException {
//        testPlaylist playlist = new testEditPlaylistJson.(playlistName).load();
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

    public void setFeedbackText(ArrayList<String> txtArr){
        String output = "";
        for(String s: txtArr){
            output += s;
            output += "\n";

        }
        text.setText(output);
    }

    /**
     * Retrieves the RetrieveRequest class.
     * @return the RetrieveRequest class.
     */
    public RetrieveRequest getAPIRequester() {
        return mMovieRequest;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

//    public ArrayList<testPlaylist> getPlaylists() {
//        return playlists;
//    }

    public ArrayList<MovieInfoObject> getmMovies() {
        return mMovies;
    }

//    public void showMovie(MovieInfoObject movie) {
//        moviePosterClicked(movie);
//    }

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

//    /**
//     * Displays list of current movies showing on cinemas.
//     */
//    public void showCurrentMovies() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_MOVIES, userProfile.isAdult());
//    }

//    /**
//     * Displays list of current tv shows showing.
//     */
//    public void showCurrentTV() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_TV, userProfile.isAdult());
//    }

//    /**
//     * Displays list of upcoming movies.
//     */
//    public void showUpcomingMovies() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.UPCOMING_MOVIES, userProfile.isAdult());
//    }

//    /**
//     * Displays list of upcoming tv shows.
//     */
//    public void showUpcomingTV() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.NEW_TV, userProfile.isAdult());
//    }

//    /**
//     * Displays list of popular movies.
//     */
//    public void showPopMovies() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.POPULAR_MOVIES, userProfile.isAdult());
//    }

//    /**
//     * Displays list of popular tv shows.
//     */
//    public void showPopTV() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.POPULAR_TV, userProfile.isAdult());
//    }

//    /**
//     * Displays list of trending movies.
//     */
//    public void showTrendMovies() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.TRENDING_MOVIES, userProfile.isAdult());
//    }

//    /**
//     * Displays list of trending tv shows.
//     */
//    public void showTrendTV() {
//        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.TRENDING_TV, userProfile.isAdult());
//    }


    private void goToPlaylistListing() {
        mMainApplication.transitToPlaylistController();
    }

}