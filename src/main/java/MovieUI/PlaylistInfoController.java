package MovieUI;

import Contexts.CommandContext;
import Contexts.SearchResultContext;
import EPparser.CommandParser;
import EPstorage.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import object.MovieInfoObject;
import object.testMovieInfoObject;
import ui.Ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlaylistInfoController extends Controller {

    @FXML
    private Label playlistNameLabel;
    @FXML
    private Label playlistDescriptionLabel;
    @FXML
    private ScrollPane mMoviesScrollPane;
//    @FXML
//    private AnchorPane anchorPane;
    @FXML
    private GridPane movieGridPane;
    @FXML
    private TextField mSearchTextField;
    @FXML
    private Label mStatusLabel;
    @FXML
    private ProgressBar mProgressBar;

    private FlowPane mMoviesFlowPane;

    // private RetrieveRequest mMovieRequest = new RetrieveRequest(this);
//    private MovieHandler movieHandler = new MovieHandler();
    private ArrayList<MovieInfoObject> mMovies = new ArrayList<>();
    private double[] mImagesLoadingProgress;

//    private MovieInfoObject mMovie;
    private Playlist playlist;

    @FXML
    private Text text;

    @FXML Label userNameLabel;
    @FXML Label userAgeLabel;
    @FXML Label adultLabel;
    @FXML TextFlow genreListText;
    @FXML Label playlistLabel;

    @FXML
    VBox playlistBox;

    private UserProfile userProfile;
    private ArrayList<Playlist> playlists;

//    private static PlaylistRetrieveRequest mPlaylistMovieRequest;
    private int num = 0;

    class KeyboardClick implements EventHandler<KeyEvent> {

        private Controller control;

        KeyboardClick(Controller control) {
            this.control = control;
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                System.out.println("Hello");
                try {
                    if (mSearchTextField.getText().equals("go back")) {
                        backToMoviesButtonClick();
                    } else {
                        mMainApplication.transitionBackToMoviesController();
                        CommandParser.parseCommands(mSearchTextField.getText(), control);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getCode().equals(KeyCode.TAB)) {
                System.out.println("Tab presjenksjessed");
                event.consume();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                //if (mMoviesScrollPane.)
                mMoviesScrollPane.requestFocus();
            }
        }
    }

    // Set the playlist for this controller
    public void setPlaylist(Playlist playlist) throws IOException {
        this.playlist = playlist;
        initialize();
    }

    @FXML public void setLabels() throws IOException {
        EditProfileJson editProfileJson = new EditProfileJson();
        userProfile = editProfileJson.load();
//        EditPlaylistJson editPlaylistJson = new EditPlaylistJson();
        mProgressBar.setProgress(0.1);
//        playlists = editPlaylistJson.load();
        ProfileCommands command = new ProfileCommands(userProfile);
        mProgressBar.setProgress(0.2);
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
//        mPlaylistMovieRequest = new PlaylistRetrieveRequest(this);
        CommandContext.initialiseContext();

//        if (playlist.getMovies().isEmpty()) {
//            System.out.println("yikes");
//        } else {
//            mMovies = convert(playlist.getMovies());
//            for (int i = 0; i < playlist.getMovies().size(); i++) {
//                System.out.println("plsplspls");
//            }
//            System.out.println(playlist.getMovies().size());
//         mMovies = new ArrayList<>();
//        }


        mProgressBar.setProgress(0.3);
        //mMovieRequest = new RetrieveRequest(this);
        // Load the movie info if movie has been set
        if (playlist != null) {
            playlistNameLabel.setText(playlist.getPlaylistName());
            //movieCastLabel.setText(mMovie.getmCast());
            playlistDescriptionLabel.setText(playlist.getDescription());
//            mMoviesScrollPane.a;
            mProgressBar.setProgress(0.4);
            if (!playlist.getMovies().isEmpty()) {
                System.out.println(playlist.getMovies().size() + "hereherehere");
                for (int i = 0; i < playlist.getMovies().size(); i++) {
                    mMovies.add(convert(playlist.getMovies().get(i)));
                }
                buildMoviesFlowPane(mMovies);
//                mPlaylistMovieRequest.beginPlaylistMovieRequest(playlist.getMovieId());
//                mProgressBar.setProgress(0.5);
            } else {
                System.out.println("fk lah how");
            }

//            loadGenres();
//            loadMovieBackdrop();
//            movieSummaryLabel.setText(mMovie.getSummary());
            mMoviesScrollPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.UP)) {
                        double num = (double) mMoviesScrollPane.getVvalue();
                        num *= 10;
                        if (num == 0) {
                            mSearchTextField.requestFocus();
                        }
                    }
                    // } else if (event.getCode().equals(KeyCode.RIGHT)) {
                    //    System.out.println("yesssx");
                    //    mMoviesFlowPane.getChildren().get(num).requestFocus();
                    //   num += 1;
                    //mMoviesFlowPane.getChildren().get(num).();
                    // } else if (event.getCode().equals(KeyCode.ENTER)) {
                    //    moviePosterClicked(mMovies.get(num));
                    // }
                }
            });
        }


        mSearchTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                System.out.println("Tab pressed");
                event.consume();
            } else if (event.getCode().equals(KeyCode.ENTER)) {
                System.out.println("Enter pressed");
                //CommandParser.parseCommands(mSearchTextField.getText(), control);
//
            }
        });
        //Real time changes to text field
        mSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        System.out.println(text.getText());

        //Enter is Pressed
        mSearchTextField.setOnKeyPressed(new KeyboardClick(this));
//        Platform.runLater(() -> buildMoviesFlowPane());

    }

//    /**
//     * This function is called when data for the movies/tv shows has been fetched.
//     */
//    @Override
//    public void allRequestCompleted(ArrayList<MovieInfoObject> movies) {
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
//        mMovies = movies;
//        mImagesLoadingProgress = new double[mMovies.size()];
//        Platform.runLater(() -> buildMoviesFlowPane(mMovies));
//
//    }

    public void displayMovies(){
        mMovies = SearchResultContext.getMoviesToDisplay();
        mImagesLoadingProgress = new double[mMovies.size()];
        Platform.runLater(() -> buildMoviesFlowPane(SearchResultContext.getMoviesToDisplay()));
    }

//    @Override
//    public void requestCompleted(MovieInfoObject movie) {
//
//    }
//
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

    /**
     * This function initalises the progress bar and extracts movie posters fro every movie.
     * @param movies is a array containing details about every movie/tv show that is being displayed.
     */
    private void buildMoviesFlowPane(ArrayList<MovieInfoObject> movies) {
        // Setup progress bar and status label
        mProgressBar.setProgress(0.0);
        mProgressBar.setVisible(true);
        mStatusLabel.setText("Loading..");

        // Build a flow pane layout with the width and size of the
        mMoviesFlowPane = new FlowPane(Orientation.HORIZONTAL);
        mMoviesFlowPane.setHgap(4);
        mMoviesFlowPane.setVgap(10);
        mMoviesFlowPane.setPadding(new Insets(10, 8, 4, 8));
        mMoviesFlowPane.prefWrapLengthProperty().bind(mMoviesScrollPane.widthProperty());   // bind to scroll pane width

        for (int i = 0; i < movies.size(); i++) {
            AnchorPane posterPane = buildMoviePosterPane(movies.get(i), i + 1);
            mMoviesFlowPane.getChildren().add(posterPane);
        }

        mMoviesScrollPane.setContent(mMoviesFlowPane);
        mMoviesScrollPane.setVvalue(0);
    }

    /**
     * @param movie a object that contains information about a movie
     * @param index a unique number assigned to every movie/tv show that is being displayed.
     * @return anchorpane consisting of the movie poster, name and the unique id.
     */

    private AnchorPane buildMoviePosterPane(MovieInfoObject movie, int index) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MoviePoster.fxml"));
            AnchorPane posterView = loader.load();
            //posterView.setOnScroll();
            posterView.setOnMouseClicked((mouseEvent) -> moviePosterClicked(movie));

            // set the movie info
            MoviePosterController controller = loader.getController();
            System.out.println(movie.getFullPosterPath() + " isit???");
//            movie.setPosterRootPath();
            Image posterImage = new Image(movie.getPosterPath(), true);
            posterImage.progressProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(movie, newValue.doubleValue()));

            controller.getMovieTitleLabel().setText(movie.getTitle());
            controller.getPosterImageView().setImage(posterImage);
            controller.getMovieNumberLabel().setText(Integer.toString(index));
            return posterView;
        } catch (IOException ex) {
            Ui.printLine();
        }

        return null;
    }

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

    /**
     * Tis function is called when the user wants to see more information about a movie.
     */
    private void moviePosterClicked(MovieInfoObject movie) {
        mMainApplication.transitToMovieInfoController(movie);
    }

    private MovieInfoObject convert(testMovieInfoObject testmovie) {
//        for (int i = 0; i < movies.size(); i++) {
//            System.out.println("teehee");
//        }
//        System.out.println("here");
//        if (movies.equals(null)) {
//            System.out.println("oopsie");
//            return new ArrayList<>();
//        } else {
//            ArrayList<MovieInfoObject> returnMovies = new ArrayList<>();
//            for (testMovieInfoObject log : movies) {
//                if (log != null) {
//                    String string = log.getReleaseDate();
//                    try {
//                        Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(string);
//                        returnMovies.add(new MovieInfoObject(log.getID(), log.getTitle(), date, log.getSummary(), log.getRating(), log.getGenreIDs(), log.getFullPosterPath(), log.getFullBackdropPath(), log.isAdult()));
//                    } catch (ParseException e) {
//                        System.out.println("fk lah cb");
//                        return new ArrayList<>();
//                    }
//                    System.out.println("hiiiii");
//                }
//            }
//            return returnMovies;
//        }
        String string = testmovie.getReleaseDate();
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(string);
            System.out.println("is this the error??? " + testmovie.getFullPosterPath());
            return new MovieInfoObject(testmovie.getID(), testmovie.getTitle(), date, testmovie.getSummary(), testmovie.getRating(), testmovie.getGenreIDs(), testmovie.getFullPosterPath(), testmovie.getFullBackdropPath(), testmovie.isAdult());
        } catch (ParseException e) {
            System.out.println("fk lah cb");
        }
        return new MovieInfoObject();
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

//    /**
//     * Retrieves the RetrieveRequest class.
//     * @return the RetrieveRequest class.
//     */
//    public PlaylistRetrieveRequest getAPIRequester() {
//        return mPlaylistMovieRequest;
//    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

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

//    private void buildMoviesFlowPane() {
//        // Setup progress bar and status label
//        mMoviesFlowPane = new FlowPane(Orientation.HORIZONTAL);
//        mMoviesFlowPane.setHgap(4);
//        mMoviesFlowPane.setVgap(10);
//        mMoviesFlowPane.setPadding(new Insets(10, 8, 4, 8));
//        mMoviesFlowPane.prefWrapLengthProperty().bind(mMoviesScrollPane.widthProperty());   // bind to scroll pane width
//        mMoviesFlowPane.getChildren().add(anchorPane);
//        mMoviesScrollPane.setContent(mMoviesFlowPane);
//        mMoviesScrollPane.setVvalue(0);
//
//    }


    // User clicks on the back button to navigate back to the movies scene
    public void backToMoviesButtonClick() {
        mMainApplication.transitionBackToMoviesController();
    }


//    // Loads the movie backdrop image asynchronously
//    private void loadMovieBackdrop() {
//        if (mMovie.getFullBackdropPath() != null) {
//            Image backdropImage = new Image(mMovie.getFullBackdropPath(), true);
//            mProgressBar.setProgress(0.9);
//            //backdropImage.progressProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(mMovie, newValue.doubleValue()));
//            movieBackdropImageView.setImage(backdropImage);
//            mProgressBar.setProgress(1.0);
//            mProgressBar.setVisible(false);
//            mStatusLabel.setText("");
//        }
//    }


//    private void loadGenres() {
//        movieGenresLabel.setText("");
//        movieCastLabel.setText("");
//        movieCertLabel.setText("");
//
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String[] genres = RetrieveRequest.getGenreStrings(mMovie);
//                StringBuilder builder = new StringBuilder();
//
//                try {
//                    for (String genre : genres) {
//                        builder.append(genre);
//                        System.out.println(genre + "  " + genres.length);
//                        // if not last string in array, append a ,
//                        if (genres.length == 0) {
//                            System.out.println("no genres");
//                        } else if (!genres[genres.length - 1].equals(genre)) {
//                            builder.append(", ");
//                        }
//                    }
//                } catch (NullPointerException ex) {
//
//                }
//                System.out.println("this is builder " + builder.toString());
//                mProgressBar.setProgress(0.6);
//
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        movieGenresLabel.setText(builder.toString());
//                        movieCastLabel.setText(RetrieveRequest.getCastStrings(mMovie));
//                        movieCertLabel.setText(RetrieveRequest.getCertStrings(mMovie));
//                        mProgressBar.setProgress(0.8);
//                    }
//                });
//            }
//        });
//
//        t.start();
//    }

    private void clearText(TextField textField) {
        textField.setText("");
    }

    // Menu item events
    @FXML
    public void exitMenuItemClicked() {
        System.exit(0);
    }

    @FXML
    public void aboutMenuItemClicked() {
    }

}