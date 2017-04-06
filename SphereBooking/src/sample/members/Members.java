package sample.members;


public class Members {

    private String id;
    private String name;
    private String membership;
    private int sessions;

    public Members(String id, String name, String memberships, int sessions) {
        this.id = id;
        this.name = name;
        this.membership = memberships;
        this.sessions = sessions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String memberships) {
        this.membership = memberships;
    }

    public int getSessions() {
        return sessions;
    }

    public void setSessions(int sessions) {
        this.sessions = sessions;
    }
}
