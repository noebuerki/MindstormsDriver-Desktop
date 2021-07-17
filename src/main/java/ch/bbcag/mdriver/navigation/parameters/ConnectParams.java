package ch.bbcag.mdriver.navigation.parameters;

public class ConnectParams extends BaseParams {
    private final String host;

    public ConnectParams(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
