package duxeye.com.entourage.model;

/**
 * Created by User on 20-07-2016.
 */
public class State {
    private String id;
    private String name;

    public State(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
