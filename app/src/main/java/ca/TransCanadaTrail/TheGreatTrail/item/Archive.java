package ca.TransCanadaTrail.TheGreatTrail.item;

public class Archive {

    private int id;
    private int imageResource;
    private String label;

    public Archive(int id, int imageResource, String label) {
        this.id = id;
        this.imageResource = imageResource;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
