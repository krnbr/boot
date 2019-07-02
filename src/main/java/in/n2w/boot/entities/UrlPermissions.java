package in.n2w.boot.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Karanbir Singh on 7/1/2019.
 **/
@Entity
@Table(name = "url_permissions")
public class UrlPermissions implements Serializable {

    @EmbeddedId
    private PK pk;

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    @Override
    public String toString() {
        return "UrlPermissions{" +
                ", pk=" + pk +
                '}';
    }
}
