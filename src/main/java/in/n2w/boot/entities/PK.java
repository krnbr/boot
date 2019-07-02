package in.n2w.boot.entities;

import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class PK implements Serializable {

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method")
    private HttpMethod method;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "PK{" +
                "url='" + url + '\'' +
                ", method=" + method +
                ", role=" + role +
                ", privilege=" + privilege +
                '}';
    }
}