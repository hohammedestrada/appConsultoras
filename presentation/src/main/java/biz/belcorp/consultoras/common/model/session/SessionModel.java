package biz.belcorp.consultoras.common.model.session;

public class SessionModel {

    private String countrySIM;

    private int authType;
    private String username;
    private String password;
    private String email;
    private String country;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private String issued;
    private String expires;
    private boolean tutorial;

    private boolean isLogged;

    private long started;
    private long updated;
    private boolean aceptaTerminosCondiciones;

    private int ordersCount = 0;

    public SessionModel() {
    }

    public String getCountrySIM() {
        return countrySIM;
    }

    public void setCountrySIM(String countrySIM) {
        this.countrySIM = countrySIM;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setAceptaTerminosCondiciones(boolean aceptaTerminosCondiciones) {
        this.aceptaTerminosCondiciones = aceptaTerminosCondiciones;
    }

    public boolean isAceptaTerminosCondiciones() {
        return aceptaTerminosCondiciones;
    }

    public int getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(int ordersCount) {
        this.ordersCount = ordersCount;
    }
}
