package su.mboh.asu.job;


/**
 * Created by smktiufa on 17/12/16.
 */

public class EventMessage {

    public static int ADD = 100;
    public static int SUCCESS = 200;
    public static int ERROR = 400;

    private int id;
    private  int status;
    private Object content;

    public EventMessage(int id, int status, Object content) {
        this.id = id;
        this.status = status;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Object getContent() {
        return content;
    }
}
