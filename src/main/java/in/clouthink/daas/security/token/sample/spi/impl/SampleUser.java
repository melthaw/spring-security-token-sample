package in.clouthink.daas.security.token.sample.spi.impl;

import in.clouthink.daas.security.token.core.Role;
import in.clouthink.daas.security.token.core.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "SampleUsers")
public class SampleUser implements User {
    
    @Id
    private String id;
    
    @Indexed
    private String username;
    
    private String passwordDigestMethod = "MD5";
    
    private String password;
    
    private String salt;
    
    private boolean expired = false;
    
    private boolean locked = false;
    
    private boolean enabled = true;
    
    private List<Role> roles;
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPasswordDigestMethod() {
        return passwordDigestMethod;
    }
    
    public void setPasswordDigestMethod(String passwordDigestMethod) {
        this.passwordDigestMethod = passwordDigestMethod;
    }
    
    public String getSalt() {
        return salt;
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    @Override
    public boolean isExpired() {
        return expired;
    }
    
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
    
    @Override
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public List<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
