package my.web.site.commons.user.model;


import my.web.site.commons.user.vals.ConstValues;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document
@Component
public class UserInfo {

    public static final String F_USERNAME = "username";
    public static final String F_PASSWORD = "password";

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private ConstValues.Role role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ConstValues.Role getRole() {
        return role;
    }

    public void setRole(ConstValues.Role role) {
        this.role = role;
    }
}
