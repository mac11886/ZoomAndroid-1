package us.zoom.sdksample.Model;

public class ZoomHost {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public us.zoom.sdksample.Model.zoom_list getZoom_list() {
        return zoom_list;
    }

    public void setZoom_list(us.zoom.sdksample.Model.zoom_list zoom_list) {
        this.zoom_list = zoom_list;
    }

    private int user_id;
    private int meeting_id;
    private User user;
    private zoom_list zoom_list;
}
