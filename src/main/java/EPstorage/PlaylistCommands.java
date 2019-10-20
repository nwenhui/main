package EPstorage;

import object.MovieInfoObject;
import object.playlistMovieInfoObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class PlaylistCommands {
    String playlistName;
    private EditPlaylistJson editPlaylistJson;

    public PlaylistCommands(String name) {
        playlistName = name;
        editPlaylistJson = new EditPlaylistJson(name);
    }

    public void create() throws IOException {
        Playlist playlist = new Playlist(playlistName);
        editPlaylistJson.createPlaylist(playlist);
    }

    public void delete() {
        editPlaylistJson.deletePlaylist();
    }

    public void add(TreeMap<String, ArrayList<String>> flagMap, ArrayList<MovieInfoObject> mMovies) throws IOException {
        ArrayList<Long> userMovies = new ArrayList<>(20);
        ArrayList<MovieInfoObject> playlistMovies = new ArrayList<>(20);
        for (String log : flagMap.get("-m")) {
            int index = Integer.parseInt(log.trim());
            System.out.println(index);
            userMovies.add((mMovies.get(--index)).getID());
            playlistMovies.add(mMovies.get(index));
            System.out.println("hello looky here " + mMovies.get(index).getFullPosterPath());
        }
        Playlist playlist = editPlaylistJson.load();
        ArrayList<playlistMovieInfoObject> newPlaylistMovies = convert(playlistMovies);
        playlist.add(newPlaylistMovies);
        editPlaylistJson.editPlaylist(playlist);
    }

    public void remove(TreeMap<String, ArrayList<String>> flagMap, ArrayList<MovieInfoObject> mMovies) throws IOException {
        ArrayList<Long> userMovies = new ArrayList<>(20);
        ArrayList<MovieInfoObject> playlistMovies = new ArrayList<>(20);
        for (String log : flagMap.get("-m")) {
            int index = Integer.parseInt(log.trim());
            System.out.println(index);
            userMovies.add((mMovies.get(--index)).getID());
            playlistMovies.add(mMovies.get(index));
        }
        Playlist playlist = editPlaylistJson.load();
        ArrayList<playlistMovieInfoObject> newPlaylistMovies = convert(playlistMovies);
        if (playlist.getMovies().get(1).equals(newPlaylistMovies.get(0))) {
            System.out.println("yes");
        } else {
            System.out.println("no");
            System.out.println(newPlaylistMovies.get(0).getTitle());
        }
            playlist.remove(newPlaylistMovies);
//        playlist.remove(removeMovies);
        editPlaylistJson.editPlaylist(playlist);
        System.out.println("hehe");
    }

    public void clear() throws IOException {
        Playlist playlist = editPlaylistJson.load();
        playlist.clear();
        editPlaylistJson.editPlaylist(playlist);
    }

    private ArrayList<playlistMovieInfoObject> convert(ArrayList<MovieInfoObject> movies) {
        ArrayList<playlistMovieInfoObject> testMovies = new ArrayList<>();
        for (MovieInfoObject log : movies) {
            Date date = log.getReleaseDate();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String string = dateFormat.format(date);
            System.out.println("help " + log.getTitle() + " " + log.getFullPosterPath());
            playlistMovieInfoObject testMovie = new playlistMovieInfoObject(log.getID(), log.getTitle(), string, log.getSummary(), log.getRating(), log.getGenreIDs(), log.getFullPosterPath(), log.getFullBackdropPath(), log.isAdult());
            testMovies.add(testMovie);
        }
        return testMovies;
    }

    /**
     * set name/description to particular Playlist object
     */
    public void setToPlaylist(TreeMap<String, ArrayList<String>> flagMap) throws IOException {
        if (flagMap.containsKey("-n") && !flagMap.containsKey("-d")) {
            setPlaylistName(appendFlagMap(flagMap.get("-n")));
        }
        if (flagMap.containsKey("-d") && !flagMap.containsKey("-n")) {
            setPlaylistDescription(appendFlagMap(flagMap.get("-d")));
        }
        if (flagMap.containsKey("-d") && flagMap.containsKey("-n")) {
            setAll(appendFlagMap(flagMap.get("-n")), appendFlagMap(flagMap.get("-d")));
        }
    }

    public String appendFlagMap(ArrayList<String> flagMapArrayList) {
        String appends = "";
        boolean flag = true;
        for (String log : flagMapArrayList) {
            if (!flag) {
                appends += ", ";
            }
            appends += log.trim();
            flag = false;
        }
        return appends;
    }

    /**
     * change name of particular Playlist object
     */
    public void setPlaylistName(String newName) throws IOException {
        Playlist playlist = editPlaylistJson.load();
        playlist.setPlaylistName(newName);
        editPlaylistJson.renamePlaylist(playlist, newName);
    }

    /**
     * change description of particular Playlist object
     */
    public void setPlaylistDescription(String description) throws IOException {
        Playlist playlist = editPlaylistJson.load();
        playlist.setDescription(description);
        editPlaylistJson.editPlaylist(playlist);
    }

    /**
     * to allow setting of both name and description at the same time
     */
    public void setAll(String newName, String description) throws IOException {
        Playlist playlist = editPlaylistJson.load();
        playlist.setPlaylistName(newName);
        playlist.setDescription(description);
        editPlaylistJson.editPlaylist(playlist);
    }
}
