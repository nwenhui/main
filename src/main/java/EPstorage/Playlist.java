package EPstorage;

import object.playlistMovieInfoObject;

import java.util.ArrayList;

public class Playlist {
    private String playlistName;
    private String description;
    private ArrayList<playlistMovieInfoObject> movies;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        description = "";
        movies = new ArrayList<>();
    }

    public Playlist(String playlistName, String description, ArrayList<playlistMovieInfoObject> movies) {
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

    public ArrayList<playlistMovieInfoObject> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<playlistMovieInfoObject> movies) {
        this.movies = movies;
    }

    public void add(ArrayList<playlistMovieInfoObject> movies) {
        this.movies.addAll(movies);
    }

    public void remove(ArrayList<playlistMovieInfoObject> movies) {
        for (playlistMovieInfoObject log : this.movies) {
            System.out.println(log.getTitle() + "hehe");
        }
        for (playlistMovieInfoObject log : movies) {
            System.out.println(log.getTitle() + "ew");
        }
        this.movies.removeAll(movies);
        for (playlistMovieInfoObject log : this.movies) {
            System.out.println(log.getTitle());
        }
    }

    public void clear() {
        movies = new ArrayList<>();
    }
}
