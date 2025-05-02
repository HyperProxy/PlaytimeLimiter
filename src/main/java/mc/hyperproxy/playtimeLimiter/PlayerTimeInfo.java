package mc.hyperproxy.playtimeLimiter;

public class PlayerTimeInfo {
    private double timePlayedToday;
    private double timeRemaining; //  28800 seconds in 8 hrs

    public double getRemainingTime() {
        return timeRemaining;
    }

    public void setRemainingTime(double timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public double getTimePlayedToday() {
        return timePlayedToday;
    }

    public void setTimePlayedToday(double timePlayedToday) {
        this.timePlayedToday = timePlayedToday;
    }
}
