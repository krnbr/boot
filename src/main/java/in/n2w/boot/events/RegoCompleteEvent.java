package in.n2w.boot.events;

import in.n2w.boot.entities.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Karanbir Singh on 4/18/2019.
 **/
public class RegoCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final User user;

    public RegoCompleteEvent(final User user, final String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }

}