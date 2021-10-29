package us.zoom.sdksample.Model;

import java.sql.Timestamp;

public class Device {

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZoom_email() {
        return zoom_email;
    }

    public void setZoom_email(String zoom_email) {
        this.zoom_email = zoom_email;
    }

    public String getZoom_api_key() {
        return zoom_api_key;
    }

    public void setZoom_api_key(String zoom_api_key) {
        this.zoom_api_key = zoom_api_key;
    }

    public String getZoom_api_secret() {
        return zoom_api_secret;
    }

    public void setZoom_api_secret(String zoom_api_secret) {
        this.zoom_api_secret = zoom_api_secret;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    private int device_id;
    private String name ;
    private String location;
    private String zoom_email;
    private String zoom_api_key;
    private String zoom_api_secret;
    private Timestamp created_at;
    private Timestamp updated_at;

}
