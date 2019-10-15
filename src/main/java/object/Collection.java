package object;

import object.MovieInfoObject;
import org.json.simple.JSONObject;

public class Collection {
    private String name;
    private long id;
    private String overview;
    private MovieInfoObject parts;

    public Collection(JSONObject collection) {
        name = (String) collection.get("name");
        id = (long) collection.get(id);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public MovieInfoObject getParts() {
        return parts;
    }

    public void setParts(MovieInfoObject parts) {
        this.parts = parts;
    }
}
