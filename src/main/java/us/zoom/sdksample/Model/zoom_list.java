package us.zoom.sdksample.Model;

public class zoom_list {

    int id;
    String device_id;
    String topic;
    String agenda;
    String url;
    int user_id ;
    String meeting_id;
    String meeting_password;
    String start_time;
    String end_time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_password() {
        return meeting_password;
    }

    public void setMeeting_password(String meeting_password) {
        this.meeting_password = meeting_password;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public zoom_list(String device_id,int user_id, String topic, String url, String meeting_id, String meeting_password) {
        this.device_id = device_id;
        this.topic = topic;
        this.url = url;
        this.user_id = user_id;
        this.meeting_id = meeting_id;
        this.meeting_password = meeting_password;

    }
}
