package EPstorage;

import object.MovieInfoObject;
import object.testMovieInfoObject;

import java.util.ArrayList;

public class Playlist {
    private String playlistName;
    private String description;
    private ArrayList<testMovieInfoObject> movies;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        description = "";
        movies = new ArrayList<>();
    }

    public Playlist(String playlistName, String description, ArrayList<testMovieInfoObject> movies) {
        this.playlistName = playlistName;
        this.description = description;
        this.movies = movies;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<testMovieInfoObject> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<testMovieInfoObject> movies) {
        this.movies = movies;
    }

    public void add(ArrayList<testMovieInfoObject> movies) {
        this.movies.addAll(movies);
    }

    public void remove(ArrayList<testMovieInfoObject> movies) {
        for (testMovieInfoObject log : this.movies) {
            System.out.println(log.getTitle() + "hehe");
        }
        for (testMovieInfoObject log : movies) {
            System.out.println(log.getTitle() + "ew");
        }
        this.movies.removeAll(movies);
        for (testMovieInfoObject log : this.movies) {
            System.out.println(log.getTitle());
        }
    }

    public void clear() {
        movies = new ArrayList<>();
    }
}
