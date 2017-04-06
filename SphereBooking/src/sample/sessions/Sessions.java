package sample.sessions;


public class Sessions {

    private String bookingId;
    private String memberId;
    private String instructorName;
    private String sessionType;
    private String sessionTime;
    private String payment;

    public Sessions(String bookingId, String memberId, String instructorName, String sessionType, String sessionTime, String payment) {
        this.bookingId = bookingId;
        this.memberId = memberId;
        this.instructorName = instructorName;
        this.sessionType = sessionType;
        this.sessionTime = sessionTime;
        this.payment = payment;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
