package su.mboh.asu.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smktiufa on 16/12/16.
 */

public class New {
    private int id = -1;
    private String title;
    private String content;
    private long createDate;
    private int status;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

