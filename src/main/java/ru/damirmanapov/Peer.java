package ru.damirmanapov;

public class Peer {

    private String host;
    private int port;
    private String id;

    public Peer() {
    }

    public Peer(int port, String id) {
        this("localhost", port, id);
    }

    public Peer(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Peer{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", id='" + id + '\'' +
                '}';
    }
}
