package commands;

import EPstorage.EditProfileJson;
import EPstorage.ProfileCommands;
import EPstorage.PlaylistCommands;
import MovieUI.Controller;
import MovieUI.MovieHandler;

import java.io.IOException;

public class PlaylistCommand extends CommandSuper {

    public PlaylistCommand(Controller uicontroller) {
        super(COMMANDKEYS.playlist, CommandStructure.cmdStructure.get(COMMANDKEYS.playlist) , uicontroller);
    }

    @Override
    public void executeCommands() throws IOException {
        switch (this.getSubRootCommand()){
            case create:
                executeCreatePlaylist();
                break;
            case delete:
                executeDeletePlaylist();
                break;
            case add:
                executeAddToPlaylist();
                break;
            case remove:
                executeRemoveFromPlaylist();
                break;
            case set:
                executeSetToPlaylist();
                break;
            case clear:
                executeClearPlaylist();
                break;
            case list:
                executePlaylistListing();
                break;
            default:
                break;
        }
    }

    /**
     * create new playlist.
     * root: playlist
     * sub: create
     * payload: <playlist name>
     * flag: none
     */
    private void executeCreatePlaylist() throws IOException {
        MovieHandler movieHandler = ((MovieHandler) this.getUIController());
//        PlaylistCommands command = new PlaylistCommands(movieHandler.getPlaylists());
//        ProfileCommands profileCommands = new ProfileCommands(new EditProfileJson().load());
//        profileCommands.addPlaylist(this.getPayload());
//        command.newPlaylist(this.getPayload());
        PlaylistCommands testCommand = new PlaylistCommands(this.getPayload());
        testCommand.create();
        movieHandler.clearSearchTextField();
        movieHandler.setLabels();
    }

    /**
     * delete playlist.
     * root: playlist
     * sub: delete
     * payload: <playlist name>
     * flag: none
     */
    private void executeDeletePlaylist() throws IOException {
        MovieHandler movieHandler = ((MovieHandler) this.getUIController());
//        PlaylistCommands command = new PlaylistCommands(movieHandler.getPlaylists());
        ProfileCommands profileCommands = new ProfileCommands(new EditProfileJson().load());
        profileCommands.deletePlaylist(this.getPayload());
//        command.deletePlaylist(this.getPayload());
        PlaylistCommands testCommand = new PlaylistCommands(this.getPayload());
        testCommand.delete();
        movieHandler.clearSearchTextField();
        movieHandler.setLabels();
    }

    /**
     * add movie titles to playlist.
     * root: playlist
     * sub: add
     * payload: <playlist name>
     * flag: -m (movie number -- not movie ID)
     */
    private void executeAddToPlaylist() throws IOException {
        MovieHandler movieHandler = ((MovieHandler) this.getUIController());
//        PlaylistCommands command = new PlaylistCommands(movieHandler.getPlaylists());
//        command.addToPlaylist(this.getPayload(), this.getFlagMap(), movieHandler.getmMovies());
        PlaylistCommands testCommand = new PlaylistCommands(this.getPayload());
        testCommand.add(this.getFlagMap(), movieHandler.getmMovies());
        movieHandler.clearSearchTextField();
    }

    /**
     * remove movie titles from playlist.
     * root: playlist
     * sub: remove
     * payload: <playlist name>
     * flag: -m (movie number -- not movie ID)
     */
    private void executeRemoveFromPlaylist() throws IOException {
        MovieHandler movieHandler = ((MovieHandler) this.getUIController());
//        PlaylistCommands command = new PlaylistCommands(movieHandler.getPlaylists());
//        command.removeFromPlaylist(this.getPayload(), this.getFlagMap(), movieHandler.getmMovies());
        PlaylistCommands testCommand = new PlaylistCommands(this.getPayload());
        testCommand.remove(this.getFlagMap(), movieHandler.getmMovies());
        movieHandler.clearSearchTextField();
    }

    /**
     * edit playlist's name and description.
     * root: playlist
     * sub: set
     * payload: <playlist name>
     * flag: -n (for new playlist name) -d (for new playlist description)
     */
    private void executeSetToPlaylist() throws IOException {
        MovieHandler movieHandler = ((MovieHandler) this.getUIController());
//        PlaylistCommands command = new PlaylistCommands(movieHandler.getPlaylists());
//        command.setToPlaylist(this.getPayload(), this.getFlagMap());
        PlaylistCommands testCommand = new PlaylistCommands(this.getPayload());
        testCommand.setToPlaylist(this.getFlagMap());
        movieHandler.clearSearchTextField();
    }

    /**
     * clear out all movies in particular playlist
     * root: playlist
     * sub: clear
     * payload: <playlist name>
     * flag: none
     */
    private void executeClearPlaylist() throws IOException {
        MovieHandler movieHandler = ((MovieHandler)this.getUIController());
//        PlaylistCommands command = new PlaylistCommands(movieHandler.getPlaylists());
//        command.clearPlaylist(this.getPayload());
        PlaylistCommands testCommand = new PlaylistCommands(this.getPayload());
        testCommand.clear();
        movieHandler.clearSearchTextField();
    }

    private void executePlaylistListing() {
        MovieHandler movieHandler = ((MovieHandler)this.getUIController());
        movieHandler.goToPlaylistListing();
        movieHandler.clearSearchTextField();
    }
}
