package webgentechnologies.com.myprayerapp.model;

/**
 * Created by Satabhisha on 28-08-2017.
 */

public class PostPrayerModelClass {
    String id;
    String user_id;
    String sender_name;
    String sender_email;
    String receiver_email;
    String post_content;
    String post_description;

    String post_type;
    String answered_status;
    String sender_access_token;

    String post_priority;
    String accessibility;

    String created_date;
    String updated_date;

    @Override
    public String toString() {
        return "PostPrayerModelClass{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_email='" + sender_email + '\'' +
                ", receiver_email='" + receiver_email + '\'' +
                ", post_content='" + post_content + '\'' +
                ", post_description='" + post_description + '\'' +
                ", post_type='" + post_type + '\'' +
                ", answered_status='" + answered_status + '\'' +
                ", sender_access_token='" + sender_access_token + '\'' +
                ", post_priority='" + post_priority + '\'' +
                ", accessibility='" + accessibility + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_date='" + updated_date + '\'' +
                '}';
    }

//   ---------------------------getter--------------------------

    public String getPost_priority() {
        return post_priority;
    }

    public void setPost_priority(String post_priority) {
        this.post_priority = post_priority;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getReceiver_email() {
        return receiver_email;
    }

    public void setReceiver_email(String receiver_email) {
        this.receiver_email = receiver_email;
    }

    //--------------------------setter-----------------------------

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getAnswered_status() {
        return answered_status;
    }

    public void setAnswered_status(String answered_status) {
        this.answered_status = answered_status;
    }

    public String getSender_access_token() {
        return sender_access_token;
    }

    public void setSender_access_token(String sender_access_token) {
        this.sender_access_token = sender_access_token;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        if (updated_date != null)
            return updated_date;
        else
            return "";
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
