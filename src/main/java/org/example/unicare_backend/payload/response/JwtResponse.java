package org.example.unicare_backend.payload.response;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String email;
    private String displayName;
    private String role;
    private Boolean isAnonymous;

    public JwtResponse(String accessToken, String id, String email, String displayName, String role, Boolean isAnonymous) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.isAnonymous = isAnonymous;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
