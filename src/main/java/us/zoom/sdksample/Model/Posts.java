package us.zoom.sdksample.Model;

public class Posts {

    private int usetId ;

    public int getUsetId() {
        return usetId;
    }

    public void setUsetId(int usetId) {
        this.usetId = usetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private int id ;
    private String title;
    private String body;
    public Posts(int usetId, int id, String title, String body) {
        this.usetId = usetId;
        this.id = id;
        this.title = title;
        this.body = body;
    }


}
