package EPstorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import object.playlistMovieInfoObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class EditPlaylistJson {
    private ObjectMapper mapper = new ObjectMapper();
    private File file;
    private JSONParser parser = new JSONParser();

    public EditPlaylistJson(String playlistName) {
        String fileName = "playlists/" + playlistName + ".json";
        file = new File(fileName);
    }

    public Playlist load() throws IOException {
        if (file.length() != 0) {
            try {
                JSONObject playlist = (JSONObject) parser.parse(new FileReader(file));
                String playlistName = (String) playlist.get("playlistName");
                String description = (String) playlist.get("description");
                JSONArray movies = (JSONArray) playlist.get("movies");
                ArrayList<playlistMovieInfoObject> playlistMovies = new ArrayList<>();
                for (int i = 0; i < movies.size(); i++) {
                    JSONObject movie = (JSONObject) movies.get(i);
                    long movieID = (long) movie.get("id");
                    String movieTitle = (String) movie.get("title");
                    String movieReleaseDate = (String) movie.get("releaseDate");
                    String movieSummary = (String) movie.get("summary");
//                    String moviePosterPath = (String) movie.get("moviePosterPath");
                    String movieFullPosterPath = (String) movie.get("fullPosterPath");
//                    String movieBackdropPath = (String) movie.get("movieBackdropPath");
                    String movieFullBackdropPath = (String) movie.get("fullBackdropPath");
                    double movieRating = (double) movie.get("rating");
                    JSONArray genreArray = (JSONArray) movie.get("genreIDs");
                    long[] movieGenreIDs = new long[genreArray.size()];
                    for (int j = 0; j < genreArray.size(); j++) {
                        movieGenreIDs[j] = (long) genreArray.get(j);
                    }
                    boolean adult = (boolean) movie.get("adult");
                    playlistMovies.add(new playlistMovieInfoObject(movieID, movieTitle, movieReleaseDate, movieSummary, movieRating, movieGenreIDs, movieFullPosterPath, movieFullBackdropPath, adult));
                }
                for (playlistMovieInfoObject log : playlistMovies) {
                    System.out.println(log.getTitle() +"choochoo");
                }
                return new Playlist(playlistName, description, playlistMovies);
            } catch (ParseException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public void createPlaylist(Playlist playlist) throws IOException {
        file.createNewFile();
        mapper.writeValue(file, playlist);
    }

    public void deletePlaylist() {
        file.delete();
    }

    public void editPlaylist(Playlist playlist) throws IOException {
        mapper.writeValue(file, playlist);
    }

    public void renamePlaylist(Playlist playlist, String newName) throws IOException {
        File oldFileName = file;
        editPlaylist(playlist);
        String fileName = "playlists/" + newName + ".json";
        File newFile = new File(fileName);
        file.renameTo(newFile);
        oldFileName.delete();
    }
}
