package vn.com.phanbagiang.myapplication;

/**
 * ‐ @created_by giangpb on 4/1/2021
 */
public class Note {
    private int id;
    private String description;

    public Note(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
