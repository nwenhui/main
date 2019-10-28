package entertainment.pro.ui;

import entertainment.pro.commons.exceptions.Exceptions;
import entertainment.pro.logic.cinemaRequesterAPI.CinemaRetrieveRequest;
import entertainment.pro.logic.contexts.CommandContext;
import entertainment.pro.logic.contexts.ContextHelper;
import entertainment.pro.logic.contexts.SearchResultContext;
import entertainment.pro.logic.execution.CommandStack;
import entertainment.pro.logic.movieRequesterAPI.RequestListener;
import entertainment.pro.logic.movieRequesterAPI.RetrieveRequest;
import entertainment.pro.logic.movieRequesterAPI.MovieResultFilter;
import entertainment.pro.model.*;
import entertainment.pro.storage.user.Blacklist;
import entertainment.pro.storage.utils.*;
import entertainment.pro.xtra.PastCommands;
import entertainment.pro.storage.utils.PastUserCommands;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import entertainment.pro.logic.parsers.CommandParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This is main page of GUI.
 */
public class MovieHandler extends Controller implements RequestListener {

    @FXML
    private ScrollPane mMoviesScrollPane;

    @FXML
    Label userAdultLabel2;
    @FXML
    Label sortAlphaOrderLabel;
    @FXML
    Label sortLatestDateLabel;
    @FXML
    Label sortHighestRatingLabel;
    @FXML
    Label userNameLabel;
    @FXML
    Label userAgeLabel;
    @FXML
    private Label mStatusLabel;
    @FXML
    private Label userPlaylistsLabel;

    @FXML
    Text autoCompleteText;
    @FXML
    Text generalFeedbackText;

    @FXML
    private TextFlow genreListText;

    @FXML
    private TextField mSearchTextField;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ProgressBar mProgressBar;

    @FXML
    private AnchorPane movieAnchorPane;


//    @FXML
//    private VBox vbox0, vBox1, vBox2, vBox3, gneresVBox, mainVBox, searchCommandVBox, generalFeedbackVBox, autoCompleteVBox;
//
//    @FXML
//    private HBox nameHBox, adultHBox, genresHBox, alphaSortHBox, latestDatesHBox, highestRatingHBox;

//    @FXML
//    private Label userPreferenceLabel, userAdultLabel1, userAdultLabel2,
//            userGenreLabel, sortAlphaOrderLabel, sortLatestDateLabel, sortHighestRatingLabel,
//            sortHighestRatingText, autoCompleteLabel, generalFeedbackLabel, userNameLabel, userAgeLabel

//    @FXML
//    private Menu fileMenu, helpMenu;

//    @FXML
//    private Text userPreferenceText, userNameText, userAgeText,
//            sortAlphaOrderText, sortLatestDateText,  autoCompleteText, generalFeedbackText;


    private boolean isViewBack = false;
    private boolean isViewBackMoreInfo = false;

    public boolean isViewBackMoreInfo() {
        return isViewBackMoreInfo;
    }

    public void setViewBackMoreInfo(boolean viewBackMoreInfo) {
        isViewBackMoreInfo = viewBackMoreInfo;
    }

    public void setViewBack(boolean viewBack) {
        isViewBack = viewBack;
    }

    private AnchorPane anchorPane;
    private static UserProfile userProfile;
    private ArrayList<String> playlists;
    private String playlistName = "";
    private MovieResultFilter filter = new MovieResultFilter(new ArrayList<>(), new ArrayList<>());
    private PageTracker pageTracker = new PageTracker();
    private FlowPane mMoviesFlowPane;
    private VBox playlistVBox = new VBox();
    private static ArrayList<MovieInfoObject> mMovies = new ArrayList<>();
    private double[] mImagesLoadingProgress;
    private static RetrieveRequest mMovieRequest;
    private static CinemaRetrieveRequest mCinemaRequest;
    private int index = 0;
    private static PastCommands pastCommands = new PastCommands();
    static String command = "";
    ArrayList<Integer> genrePreference = new ArrayList<>();
    ArrayList<Integer> genreRestriction = new ArrayList<>();
    ArrayList<String> playlist = new ArrayList<>();
    boolean isAdultEnabled = false;
    boolean sortByAlphaOrder = false;
    boolean sortByRating = false;
    boolean sortByReleaseDate = false;
    boolean isMovie = true;
    String searchEntryName = "";
    String name = "";
    int age = 0;


    SearchProfile searchProfile = new SearchProfile(name, age, genrePreference, genreRestriction, isAdultEnabled,
            playlist, sortByAlphaOrder, sortByRating, sortByReleaseDate, searchEntryName, isMovie);


    public SearchProfile getSearchProfile() {
        return searchProfile;
    }

    Controller controller;

    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    public static void updatePastCommands(String now) {
        PastCommandStructure pastCommandStructure = new PastCommandStructure(now, command);
        ArrayList<PastCommandStructure> arrayList = pastCommands.getMap();
        arrayList.add(pastCommandStructure);
        System.out.println(now + " " + command);
        pastCommands.setMap(arrayList);
        PastUserCommands.update(pastCommands);
    }

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

            System.out.println("You Pressing : " + ((KeyEvent) event).getCode());
            if ((event.getCode().equals(KeyCode.ENTER))) {
                System.out.println("Hello");
                command = mSearchTextField.getText();
                //clickEntered(command, control);
                try {
                    CommandParser.parseCommands(command, control);
                } catch (IOException | Exceptions e) {
                    e.printStackTrace();
                }
                clearSearchTextField();
            } else if (event.getCode().equals(KeyCode.TAB)) {
                System.out.println("Tab presjenksjessed");
                event.consume();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                mMoviesScrollPane.requestFocus();
                mMoviesFlowPane.getChildren().get(0).setStyle("-fx-border-color: white");
            }
        }

    }

    /**
     * This function is called when JavaFx runtime when view is loaded.
     */
    @FXML
    public void setLabels() throws IOException {
        System.out.println("called setlabels");
        EditProfileJson editProfileJson = new EditProfileJson();
        userProfile = editProfileJson.load();
        //ArrayList<Integer> arrayList = userPr
        try {
            pastCommands.setMap(PastUserCommands.load());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userNameLabel.setText(userProfile.getUserName());
        userAgeLabel.setText(Integer.toString(userProfile.getUserAge()));
        playlists = userProfile.getPlaylistNames();
        ProfileCommands command = new ProfileCommands(userProfile);

        System.out.println("changed age");

        //setting adult label
        if (command.getAdultLabel().equals("allow")) {
            userAdultLabel2.setStyle("-fx-text-fill: \"#48C9B0\";");
        }
        if (command.getAdultLabel().equals("restrict")) {
            userAdultLabel2.setStyle("-fx-text-fill: \"#EC7063\";");
        }
        userAdultLabel2.setText(command.getAdultLabel());
        //setting text for preference & restrictions
        Text preferences = new Text(command.convertToLabel(userProfile.getGenreIdPreference()));
        preferences.setFill(Paint.valueOf("#48C9B0"));
        Text restrictions = new Text(command.convertToLabel(userProfile.getGenreIdRestriction()));
        restrictions.setFill(Paint.valueOf("#EC7063"));
        genreListText.getChildren().clear();
        genreListText.getChildren().addAll(preferences, restrictions);
        genreListText.setLineSpacing(4);
        updateSortInterface();
    }


    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    @FXML
    public void initialize() throws IOException, Exceptions {
        setLabels();
        mMovieRequest = new RetrieveRequest(this);
        mMovieRequest.setSearchProfile(searchProfile);
        mCinemaRequest = new CinemaRetrieveRequest(this);
        CommandContext.initialiseContext();

        BlacklistStorage bp = new BlacklistStorage();
        System.out.println("Tgt we are winners");
        bp.load();



        HelpStorage.initialiseAllHelp();

        mSearchTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                System.out.println("Tab pressed");


                setAutoCompleteText(ContextHelper.getAllHints(mSearchTextField.getText(), this));
                event.consume();

            } else if (event.getCode().equals(KeyCode.ALT_GRAPH) || event.getCode().equals(KeyCode.ALT)) {
                System.out.println("I pressed bit");
                mSearchTextField.clear();
                String cmd = CommandStack.nextCommand();
                if (cmd == null) {
                    setAutoCompleteText("You dont have any commands in history!");
                } else {
                    mSearchTextField.clear();
                    mSearchTextField.setText(cmd);

                }

                mSearchTextField.positionCaret(mSearchTextField.getText().length());
            }
        });

        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_MOVIES);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String now = formatter.format(date);
        PastCommandStructure pastCommandStructure = new PastCommandStructure(now, "view movies current");
        ArrayList<PastCommandStructure> arrayList = pastCommands.getMap();
        arrayList.add(pastCommandStructure);
        //System.out.println(now + " " + command);
        pastCommands.setMap(arrayList);
        PastUserCommands.update(pastCommands);

        //generalFeedbackText.setText("Welcome to Entertainment Pro. Displaying currently showing movies...");

        //Real time changes to text field
        mSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        System.out.println(generalFeedbackText.getText());

        //Enter is Pressed
        mSearchTextField.setOnKeyPressed(new KeyboardClick(this));
        mMoviesScrollPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.UP)) {
                    double num = (double) mMoviesScrollPane.getVvalue();
                    num *= 10;
                    if (num == 0) {
                        mSearchTextField.requestFocus();
                    }
                    mMoviesFlowPane.getChildren().get(index).setStyle("-fx-border-color: black");
                    index = 0;
                } else if (event.getCode().equals(KeyCode.RIGHT)) {
                    int size = mMoviesFlowPane.getChildren().size();
                    if ((index + 1) != size) {
                        mMoviesFlowPane.getChildren().get(index).requestFocus();
                        index += 1;
                        if (index != 0) {
                            mMoviesFlowPane.getChildren().get(index - 1).setStyle("-fx-border-color: black");
                        }
                        mMoviesFlowPane.getChildren().get(index).setStyle("-fx-border-color: white");
                        if (index % 4 == 0) {
                            mMoviesScrollPane.setVvalue((double)(index + 1) / size);
                        }
                    }
                } else if (event.getCode().equals(KeyCode.LEFT)) {
                    System.out.println("yesssx");
                    if (index != 0) {
                        mMoviesFlowPane.getChildren().get(index - 1).requestFocus();
                        index -= 1;
                        mMoviesFlowPane.getChildren().get(index + 1).setStyle("-fx-border-color: black");
                        mMoviesFlowPane.getChildren().get(index).setStyle("-fx-border-color: white");
                        double size = mMoviesFlowPane.getChildren().size();
                        if (index % 4 == 0) {
                            mMoviesScrollPane.setVvalue((index + 1) / size);
                        }
                    } else {
                        mSearchTextField.requestFocus();
                        mMoviesFlowPane.getChildren().get(index).setStyle("-fx-border-color: black");

                    }
                } else if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
                        moviePosterClicked(mMovies.get(index));
                    } catch (Exceptions exceptions) {
                        exceptions.printStackTrace();
                    }
                    index = 0;
                }
            }
        });
    }

    /**
     * This function is called when data for the movies/tv shows has been fetched.
     */
    @Override
    public void requestCompleted(ArrayList<MovieInfoObject> moviesInfo) {
        // Build the Movie poster views and add to the flow pane on the main thread
        //System.out.print("Request received");
        ArrayList<MovieInfoObject> filteredMovies = Blacklist.filter(moviesInfo);
        filteredMovies = filter.filter(filteredMovies);
        final ArrayList<MovieInfoObject> MoviesFinal = filteredMovies;
        mMovies.clear();
        System.out.println("cleared");
        for (MovieInfoObject mf : MoviesFinal) {
            //mMovies.add(mf);
            System.out.println("yaaz" + mf.getTitle());
        }
        //System.out.print("Request rsdceceived");
        SearchResultContext.addResults(MoviesFinal);
        mMovies = MoviesFinal;

        if (isViewBackMoreInfo) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Update UI here.
                    PastCommandStructure pastCommandStructure = getPastCommands().getMap().get(
                            getPastCommands().getMap().size() - 2);
                    String command = pastCommandStructure.getQuery();
                    String[] getStrips = command.split(" ");
                    int num = 0;
                    if (getPastCommands().getMap().get(getPastCommands().getMap().size() - 2)
                            .getQuery().startsWith("view entry")) {
                        num = Integer.parseInt(getStrips[2]);
                    }
                    try {
                        showMovie(num);
                    } catch (Exceptions exceptions) {
                        exceptions.printStackTrace();
                    }
                    isViewBackMoreInfo = false;
                        getPastCommands().getMap().remove(getPastCommands().getMap().size() - 1);
                        getPastCommands().getMap().remove(getPastCommands().getMap().size() - 1);
                        PastUserCommands.update(pastCommands);
                        isViewBack = false;
                }
            });

        } else {
            //System.out.println("this is size: " + mMovies.size());
            mImagesLoadingProgress = new double[mMovies.size()];
            Platform.runLater(() -> {
                try {
                    buildMoviesFlowPane(MoviesFinal);
                } catch (Exceptions exceptions) {
                    exceptions.printStackTrace();
                }
            });
            if (isViewBack == true) {
                getPastCommands().getMap().remove(getPastCommands().getMap().size() - 1);
                getPastCommands().getMap().remove(getPastCommands().getMap().size() - 1);
                PastUserCommands.update(pastCommands);
                isViewBack = false;
            }
        }


    }

    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    public void displayMovies() {
        mMovies = SearchResultContext.getMoviesToDisplay();
        mImagesLoadingProgress = new double[mMovies.size()];
        Platform.runLater(() -> {
            try {
                buildMoviesFlowPane(SearchResultContext.getMoviesToDisplay());
            } catch (Exceptions exceptions) {
                exceptions.printStackTrace();
            }
        });
    }

    /**
     * This function is called when data for the movies/tv shows failed to fetch due to timed out.
     */
    @Override
    public void requestTimedOut() {
        Platform.runLater(() -> showDownloadFailureAlert("Request timed out"));
    }

    /**
     * This function is called when data for the movies/tv shows failed due to internet connection.
     */
    @Override
    public void requestFailed() {
        Platform.runLater(() -> showDownloadFailureAlert("No internet connection"));
    }

    /**
     * This funcion is called to print a message when the data for movies/tv shows is unavailable due to
     * no internet connection.
     *
     * @param headerText consists of the string to be printed.
     */
    private void showDownloadFailureAlert(String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed to download");
        alert.setHeaderText(headerText);
        alert.setContentText("Please ensure you have an active internet connection.");
        alert.showAndWait();
    }

    /**
     * This function initalises the progress bar and extracts movie posters fro every movie.
     *
     * @param movies is a array containing details about every movie/tv show that is being displayed.
     */
    private void buildMoviesFlowPane(ArrayList<MovieInfoObject> movies) throws Exceptions {
        // Setup progress bar and status label
        mProgressBar.setProgress(0.0);
        mProgressBar.setVisible(true);
        mStatusLabel.setText("Loading..");

        mMoviesFlowPane = new FlowPane(Orientation.HORIZONTAL);
        mMoviesFlowPane.setHgap(4);
        mMoviesFlowPane.setVgap(10);
        mMoviesFlowPane.setPadding(new Insets(10, 8, 4, 8));
        mMoviesFlowPane.prefWrapLengthProperty().bind(mMoviesScrollPane.widthProperty());   // bind to scroll pane width
        //mMoviesFlowPane.getChildren().add(generalFeedbackLabel);
        for (int i = 0; i < movies.size(); i++) {
            AnchorPane posterPane = buildMoviePosterPane(movies.get(i), i + 1);
            mMoviesFlowPane.getChildren().add(posterPane);
        }
      //  mMoviesScrollPane.setFitToWidth(true);
        mMoviesScrollPane.setContent(mMoviesFlowPane);
        mMoviesScrollPane.setVvalue(0);
    }


    /**
     * to build the movie posters.
     * @param movie a object that contains information about a movie
     * @param index a unique number assigned to every movie/tv show that is being displayed.
     * @return anchorpane consisting of the movie poster, name and the unique id.
     */

    private AnchorPane buildMoviePosterPane(MovieInfoObject movie, int index) throws Exceptions {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MoviePoster.fxml"));
            AnchorPane posterView = loader.load();
            //posterView.setOnScroll();
            posterView.setOnMouseClicked((mouseEvent) -> {
                try {
                    moviePosterClicked(movie);
                } catch (Exceptions exceptions) {
                    exceptions.printStackTrace();
                }
            });

            // set the movie info
            MoviePosterController controller = loader.getController();
            try {
                if (movie.getFullPosterPath() != null) {
                    System.out.println("sianz");
                    Image posterImage = new Image(movie.getFullPosterPath(), true);
                    posterImage.progressProperty().addListener((observable, oldValue, newValue) -> {
                        try {
                            updateProgressBar(movie, newValue.doubleValue());
                        } catch (Exceptions exceptions) {
                            exceptions.printStackTrace();
                        }
                    });
                    controller.getPosterImageView().setImage(posterImage);
                } else {
                    System.out.println("hi1");
                    Image posterImage = new Image(this.getClass().getResourceAsStream("../../../../EPdata/FakeMoviePoster.png"));
                    System.out.println("hi2");
                    posterImage.progressProperty().addListener((observable, oldValue, newValue) -> {
                        try {
                            updateProgressBar(movie, newValue.doubleValue());
                        } catch (Exceptions exceptions) {
                            exceptions.printStackTrace();
                        }
                    });
                    System.out.println("hi3");
                    controller.getPosterImageView().setImage(posterImage);
                    System.out.println("sianzzzzz");
                }
            } catch (NullPointerException ex) {

            }
            controller.getMovieTitleLabel().setText(movie.getTitle());
            controller.getMovieNumberLabel().setText(Integer.toString(index));
            return posterView;
        } catch (IOException ex) {
            //Ui.printLine();
        }

        return null;
    }

    /**
     * This funcion updates the progress bar as the movie poster is being displayed.
     *
     * @param movie    Object that contains all the information about a particular movie.
     * @param progress contains the progress value.
     */
    private void updateProgressBar(MovieInfoObject movie, double progress) throws Exceptions {
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
            if (isViewBack) {
                PastCommandStructure pastCommandStructure = getPastCommands().getMap().get(
                        getPastCommands().getMap().size() - 2);
                String command = pastCommandStructure.getQuery();
                String[] getStrips = command.split(" ");
                int num = 0;
                if (getPastCommands().getMap().get(getPastCommands().getMap().size() - 2)
                        .getQuery().startsWith("view entry")) {
                    num = Integer.parseInt(getStrips[2]);
                }
                showMovie(num);
                isViewBack = false;
                getPastCommands().getMap().remove(getPastCommands().getMap().size() - 1);
                getPastCommands().getMap().remove(getPastCommands().getMap().size() - 1);
                PastUserCommands.update(pastCommands);

            }
        }
    }

    public boolean isViewBack() {
        return isViewBack;
    }

    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    public void showMovie(int num) throws Exceptions {
        //System.out.println("this is " + mMovies.size());
        MovieInfoObject movie = mMovies.get(num - 1);
        moviePosterClicked(movie);
        System.out.println("this is it 4");
    }



    private void buildPlaylistVBox(ArrayList<String> playlists) throws IOException {
        // Setup progress bar and status label
        mProgressBar.setProgress(0.0);
        mProgressBar.setVisible(true);
        mStatusLabel.setText("Loading..");
        playlistVBox.getChildren().clear();

        int count = 1;
        if (playlists.isEmpty()) {
            Label emptyLabel = new Label("u do not have any playlist currently :( "
                    + "\n try making some using command: playlist create <playlist name>");
            playlistVBox.getChildren().add(emptyLabel);
        } else {
            for (String log : playlists) {
                Playlist playlist = new EditPlaylistJson(log).load();
                System.out.println(playlist.getPlaylistName());
                System.out.println(playlist.getMovies().size());
                AnchorPane playlistPane = buildPlaylistPane(playlist, count);
                playlistVBox.getChildren().add(playlistPane);
                count++;
            }
            mMoviesScrollPane.setContent(playlistVBox);
            mMoviesScrollPane.setVvalue(0);
            pageTracker.setToPlaylistList();
        }
    }

    private AnchorPane buildPlaylistPane(Playlist playlist, int i) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistPane.fxml"));
            AnchorPane playlistPane = loader.load();
            playlistPane.setOnMouseClicked((mouseEvent) -> {
                playlistPaneClicked(playlist);
            });
            // set the movie info
            PlaylistController controller = loader.getController();
            controller.setVBoxColour(i);
//            controller.setTextColour();
            controller.getPlaylistNameLabel().setText(playlist.getPlaylistName());
            if (playlist.getDescription().trim().length() == 0) {
                controller.getPlaylistDescriptionLabel().setText("*this playlist does not have a description :(*");
            } else {
                controller.getPlaylistDescriptionLabel().setText(playlist.getDescription());
            }
            controller.getPlaylistMoviesLabel()
                    .setText("No. of movies: " + Integer.toString(playlist.getMovies().size()));
            System.out.println("no lei here");
            return playlistPane;
        } catch (IOException ex) {
//            Ui.printLine();
        }
        System.out.println("fk lah here");
        return null;
    }

    private void playlistPaneClicked(Playlist playlist) {
        buildPlaylistInfo(playlist);
        playlistName = playlist.getPlaylistName();
    }

    private void buildPlaylistInfo(Playlist playlist) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistInfo.fxml"));
            AnchorPane playlistPane = loader.load();
            PlaylistInfoController controller = loader.getController();
            controller.getPlaylistNameLabel().setText(playlist.getPlaylistName());
            if (playlist.getDescription().trim().length() == 0) {
                controller.getPlaylistDescriptionLabel().setStyle("-fx-font-style: italic");
                controller.getPlaylistDescriptionLabel().setText("*this playlist does not have a description :(*");
            } else {
                controller.getPlaylistDescriptionLabel().setText(playlist.getDescription());
            }
            if (playlist.getMovies().size() != 0) {
                controller.getPlaylistInfoVBox().getChildren().add(buildPlaylistMoviesFlowPane(playlist.getMovies()));
            } else {
                Label emptyMoviesLabel = new Label(playlist.getPlaylistName() + " does not contain any movies :(");
                controller.getPlaylistInfoVBox().getChildren().add(2, emptyMoviesLabel);
            }
            mMoviesScrollPane.setContent(controller.getPlaylistInfoVBox());
            pageTracker.setToPlaylistInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FlowPane buildPlaylistMoviesFlowPane(ArrayList<PlaylistMovieInfoObject> movies) {
        // Setup progress bar and status label
        mProgressBar.setProgress(0.0);
        mProgressBar.setVisible(true);
        mStatusLabel.setText("Loading..");
        mMovies = convert(movies);

        mMoviesFlowPane = new FlowPane(Orientation.HORIZONTAL);
        mMoviesFlowPane.setHgap(4);
        mMoviesFlowPane.setVgap(10);
        mMoviesFlowPane.setPadding(new Insets(10, 8, 4, 8));
        mMoviesFlowPane.prefWrapLengthProperty().bind(mMoviesScrollPane.widthProperty());   // bind to scroll pane width

        for (int i = 0; i < movies.size(); i++) {
            AnchorPane posterPane = buildPlaylistMoviePosterPane(movies.get(i), i + 1);
            mMoviesFlowPane.getChildren().add(posterPane);
        }
        return mMoviesFlowPane;
    }

    private AnchorPane buildPlaylistMoviePosterPane(MovieInfoObject movie, int index) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MoviePoster.fxml"));
            AnchorPane posterView = loader.load();
            //posterView.setOnScroll();
            posterView.setOnMouseClicked((mouseEvent) -> {
                try {
                    playlistMoviePosterClicked(movie);
                } catch (Exceptions exceptions) {
                    exceptions.printStackTrace();
                }
            });

            // set the movie info
            MoviePosterController controller = loader.getController();
            try {
                File fakePoster = new File("./FakeMoviePoster.png");
                Image posterImage = new Image(fakePoster.toURI().toString());
                posterImage.progressProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        updateProgressBar(movie, newValue.doubleValue());
                    } catch (Exceptions exceptions) {
                        exceptions.printStackTrace();
                    }
                });
                controller.getPosterImageView().setImage(posterImage);
            } catch (NullPointerException ex) {

            }
            controller.getMovieTitleLabel().setText(movie.getTitle());
            controller.getMovieNumberLabel().setText(Integer.toString(index));
            return posterView;
        } catch (IOException ex) {
            //Ui.printLine();
        }

        return null;
    }

    public void showPlaylistList() throws IOException {
        buildPlaylistVBox(playlists);
    }

    private ArrayList<MovieInfoObject> convert(ArrayList<PlaylistMovieInfoObject> toConvert) {
        ArrayList<MovieInfoObject> converted = new ArrayList<>();
        boolean isMovie = false;
        for (PlaylistMovieInfoObject log : toConvert) {
            converted.add(new MovieInfoObject(isMovie, log.getID(), log.getTitle(),
                    log.getReleaseDate(), log.getSummary(), log.getRating(), log.getGenreIDs(),
                    log.getFullPosterPath(), log.getFullBackdropPath(), log.isAdult()));
        }
        return converted;
    }


    /**
     * Tis function is called when the user wants to see more information about a movie.
     */
    public void moviePosterClicked(MovieInfoObject movie) throws Exceptions {
        try {
            //mMainApplication.transitToMovieInfoController(movie);
            mMoviesFlowPane.getChildren().clear();
            mMoviesFlowPane = new FlowPane(Orientation.HORIZONTAL);
            mMoviesFlowPane.setHgap(4);
            mMoviesFlowPane.setVgap(10);
            mMoviesFlowPane.setPadding(new Insets(10, 8, 4, 8));
            mMoviesFlowPane.prefWrapLengthProperty().bind(mMoviesScrollPane.widthProperty());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MoreInfo.fxml"));
            AnchorPane posterView = loader.load();
            InfoController controller = loader.getController();

            controller.getMovieTitleLabel().setText(movie.getTitle());
            controller.getMovieRatingLabel().setText(String.format("%.2f", movie.getRating()));
            if (movie.getReleaseDate() != null) {
                Date date = movie.getReleaseDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String printDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                controller.getMovieReleaseDateLabel().setText(printDate);
            } else {
                controller.getMovieReleaseDateLabel().setText("N/A");
            }
            try {
                Image posterImage = new Image(movie.getFullBackdropPath(), true);
                controller.getMovieBackdropImageView().setImage(posterImage);
            } catch (NullPointerException ex) {

            }

            controller.getMovieSummaryLabel().setText(movie.getSummary());
            controller.getMovieCastLabel().setText(RetrieveRequest.getCastStrings(movie));
            controller.getMovieCertLabel().setText(RetrieveRequest.getCertStrings(movie));
            String[] genres = RetrieveRequest.getGenreStrings(movie);
            StringBuilder builder = new StringBuilder();
            try {
                for (String genre : genres) {
                    builder.append(genre);
                    System.out.println(genre + "  " + genres.length);
                    // if not last string in array, append a ,
                    if (genres.length == 0) {
                        System.out.println("no genres");
                    } else if (!genres[genres.length - 1].equals(genre)) {
                        builder.append(", ");
                    }
                }
            } catch (NullPointerException ex) {

            }
            controller.getMovieGenresLabel().setText(builder.toString());
            mMoviesFlowPane.getChildren().add(posterView);
            mMoviesScrollPane.setContent(mMoviesFlowPane);
            mMoviesScrollPane.setVvalue(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tis function is called when the user wants to see more information about a movie.
     */
    public void playlistMoviePosterClicked(MovieInfoObject movie) throws Exceptions {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PlaylistMoreInfo.fxml"));
            AnchorPane posterView = loader.load();
            PlaylistMovieController controller = loader.getController();

            controller.getMovieTitleLabel().setText(movie.getTitle());
            controller.getMovieRatingLabel().setText(String.format("%.2f", movie.getRating()));
            if (movie.getReleaseDate() != null) {
                Date date = movie.getReleaseDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String printDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                controller.getMovieDateLabel().setText(printDate);
            } else {
                controller.getMovieDateLabel().setText("N/A");
            }
            controller.getMovieSummaryLabel().setText(movie.getSummary());
            String[] genres = RetrieveRequest.getGenreStrings(movie);
            StringBuilder builder = new StringBuilder();
            try {
                for (String genre : genres) {
                    builder.append(genre);
                    System.out.println(genre + "  " + genres.length);
                    // if not last string in array, append a ,
                    if (genres.length == 0) {
                        System.out.println("no genres");
                    } else if (!genres[genres.length - 1].equals(genre)) {
                        builder.append(", ");
                    }
                }
            } catch (NullPointerException ex) {

            }
            controller.getMovieGenresLabel().setText(builder.toString());
            mMoviesScrollPane.setContent(controller.getPlaylistMovieInfoAnchorPane());
            mMoviesScrollPane.setVvalue(0);
            pageTracker.setToPlaylistMovieInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function clears the searchTextField when called.
     */
    public void clearSearchTextField() {
        mSearchTextField.setText("");
    }






    public void updateTextField(String updateStr) {
        mSearchTextField.setText(mSearchTextField.getText() + updateStr);
        mSearchTextField.positionCaret(mSearchTextField.getText().length());
    }

    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    public void setFeedbackText(ArrayList<String> txtArr) {
        String output = "";
        for (String s : txtArr) {
            output += s;
            output += "\n";

        }
        generalFeedbackText.setText(output);
    }


    /**
     * Prints message in UI.
     *
     * @param txt which is the string text to be printed.
     */
    public void setFeedbackText(String txt) {
        generalFeedbackText.setText(txt);
    }


    public void setAutoCompleteText(String text) {
        autoCompleteText.setText(text);
    }

    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    public void setAutoCompleteText(ArrayList<String> txtArr) {
        String output = "";
        Set<String> hashSet = new HashSet<String>();
        for (String s : txtArr) {
            hashSet.add(s);
        }

        for (String s:hashSet) {
            output += s;
            output += "\n";

        }
        autoCompleteText.setText(output);
    }

    /**
     * Retrieves the RetrieveRequest class.
     *
     * @return the RetrieveRequest class.
     */
    public RetrieveRequest getAPIRequester() {
        return mMovieRequest;
    }

    /**
     * Retrieves the cinemaRetrieveRequest class.
     * @return the cinemaRetrieveRequest class
     */
    public CinemaRetrieveRequest getCinemaAPIRequester() {
        return mCinemaRequest;
    }

    public static String getCommands() {
        return command;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public static PastCommands getPastCommands() {
        return pastCommands;
    }

    public ArrayList<MovieInfoObject> getmMovies() {
        return mMovies;
    }

    @FXML
    private void clearSearchButtonClicked() {
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

    /**
     * Displays list of current movies showing on cinemas.
     */
    public static void showCurrentMovies() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_MOVIES);
    }

    /**
     * Displays list of current tv shows showing.
     */
    public static void showCurrentTV() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_TV);
    }

    /**
     * Displays list of upcoming movies.
     */
    public static void showUpcomingMovies() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.UPCOMING_MOVIES);
    }

    /**
     * Displays list of upcoming tv shows.
     */
    public static void showUpcomingTV() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.CURRENT_TV);
    }

    /**
     * Displays list of popular movies.
     */
    public static void showPopMovies() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.POPULAR_MOVIES);
    }

    /**
     * Displays list of popular tv shows.
     */
    public static void showPopTV() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.POPULAR_TV);
    }


    /**
     * Displays list of trending movies.
     */
    public static void showTrendMovies() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.TRENDING_MOVIES);
        ;
    }

    /**
     * Displays list of trending tv shows.
     */
    public static void showTrendTV() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.TRENDING_TV);
    }



    public static void showSearchMovie() throws Exceptions {
        mMovieRequest.beginMovieRequest(RetrieveRequest.MoviesRequestType.TRENDING_TV);
    }

    public static void setSearchProfile(SearchProfile searchProfile) {
        mMovieRequest.setSearchProfile(searchProfile);
    }

    /**
     * checkstyle made me put javadoc here >:( whoever made this function pls edit the the javadoc tqtq -wh.
     */
    public void updateSortInterface() {
        if (userProfile.isSortByAlphabetical()) {
            sortAlphaOrderLabel.setText("Y");
            sortLatestDateLabel.setText("N");
            sortHighestRatingLabel.setText("N");
        } else if (userProfile.isSortByLatestRelease()) {
            sortAlphaOrderLabel.setText("N");
            sortLatestDateLabel.setText("Y");
            sortHighestRatingLabel.setText("N");
        } else if (userProfile.isSortByHighestRating()) {
            sortAlphaOrderLabel.setText("N");
            sortLatestDateLabel.setText("N");
            sortHighestRatingLabel.setText("Y");
        } else {
            sortAlphaOrderLabel.setText("N");
            sortLatestDateLabel.setText("N");
            sortHighestRatingLabel.setText("N");
        }
    }


    public void setPlaylistName(String name) {
        playlistName = name;
    }

    /**
     * to refresh the gui page so it reflects user's changes.
     */
    public void refresh() throws IOException {
        switch (pageTracker.getCurrentPage()) {
        case "playlistList":
            EditProfileJson editProfileJson = new EditProfileJson();
            buildPlaylistVBox(editProfileJson.load().getPlaylistNames());
            break;
        case "playlistInfo":
            EditPlaylistJson editPlaylistJson = new EditPlaylistJson(playlistName);
            buildPlaylistInfo(editPlaylistJson.load());
            break;
        default:
            break;
        }
    }

    public MovieResultFilter getFilter() {
        return filter;
    }

    public void setFilter(MovieResultFilter filter) {
        this.filter = filter;
    }

    public PageTracker getPageTracker() {
        return pageTracker;
    }

    /**
     * to go back to playlist info page from playlistmovieinfo page.
     */
    public void backToPlaylistInfo() throws IOException {
        if (pageTracker.isPlaylistMovieInfo()) {
            pageTracker.setToPlaylistInfo();
            refresh();
        }
    }
}