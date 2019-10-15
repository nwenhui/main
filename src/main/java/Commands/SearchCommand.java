package Commands;


import EPstorage.ProfileCommands;
import MovieUI.Controller;
import MovieUI.MovieHandler;
import MovieUI.MoviePosterController;
import movieRequesterAPI.MovieResultFilter;
import movieRequesterAPI.RetrieveRequest;

import java.util.ArrayList;
import java.util.TreeMap;

import java.io.IOException;


public class SearchCommand extends CommandSuper {


    public SearchCommand(Controller UIController) {
        super(COMMAND_KEYS.search, CommandStructure.cmdStructure.get(COMMAND_KEYS.search), UIController);
    }

    @Override
    public void executeCommands() throws IOException {
        switch (this.getSubRootCommand()){
            case movies:
                executeMovieSearch();
                break;
            case tvshows:
                executeTvShowSearch();
                break;
//            case all:
//                executeTvShowSearch();
//                break;
            default:
                break;
        }

    }

    /**
     * search for movie titles using keywords
     * root: search
     * sub: movies
     * payload: <keywords>
     * flag: -g (genre name -- not genre ID) [-g preferences -> to use user's preferred filters]
     */
    private void executeMovieSearch() throws IOException {
        TreeMap<String, ArrayList<String>> treeMap = getFlagMap();
        MovieHandler movieHandler = new MovieHandler();
        if (treeMap.containsKey("-c")) {
            movieHandler.showCurrentMovies();
        } else if (treeMap.containsKey("-u")) {
            movieHandler.showUpcomingMovies();
        } else if (treeMap.containsKey("-t")) {
            movieHandler.showTrendMovies();
        } else if (treeMap.containsKey("-p")) {
            movieHandler.showPopMovies();
        } else {
            if (!this.getFlagMap().containsKey("-g")) {
                if (movieHandler.getUserProfile().isAdult()) {
                    ((MovieHandler) this.getUIController()).getAPIRequester().beginMovieSearchRequest(getPayload(), true);
                } else {
                    ((MovieHandler) this.getUIController()).getAPIRequester().beginMovieSearchRequest(getPayload(), false);
                }
                movieHandler.clearSearchTextField();
            } else {
                ArrayList<Integer> inputGenrePreference = new ArrayList<>(10);
                ArrayList<Integer> inputGenreRestriction = new ArrayList<>(10);

                for (String log : this.getFlagMap().get("-g")) {
                    if (log.equalsIgnoreCase("preferences")) {
                        inputGenrePreference.addAll(movieHandler.getUserProfile().getGenreIdPreference());
                    } else if (log.equalsIgnoreCase("restrictions")) {
                        inputGenreRestriction.addAll(movieHandler.getUserProfile().getGenreIdRestriction());
                    }
                    else {
                        ProfileCommands command = new ProfileCommands(movieHandler.getUserProfile());
                        inputGenrePreference.add(command.findGenreID(log));
                    }
                }
                if (movieHandler.getUserProfile().isAdult()) {
                    ((MovieHandler) this.getUIController()).getAPIRequester().beginMovieSearchRequestWithPreference(getPayload(), inputGenrePreference, inputGenreRestriction, true);
                } else {
                    ((MovieHandler) this.getUIController()).getAPIRequester().beginMovieSearchRequestWithPreference(getPayload(), inputGenrePreference, inputGenreRestriction, false);
                }
                movieHandler.clearSearchTextField();
            }
        }
    }

    private void executeTvShowSearch() {
        MovieHandler movieHandler = new MovieHandler();

        TreeMap<String, ArrayList<String>> treeMap = getFlagMap();
        if (treeMap.containsKey("-c")) {
            movieHandler.showCurrentTV();
            //} else if (treeMap.containsKey("-[u]")){
            //  MovieHandler.showUpcomingTV();
        } else if (treeMap.containsKey("-t")) {
            movieHandler.showTrendTV();
        } else if (treeMap.containsKey("-p")) {
            movieHandler.showPopTV();
        }

    }
}

