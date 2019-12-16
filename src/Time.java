public class Time {
    private int minutes, seconds, hundredsOfSeconds;

    public String resetTime() {
        minutes = 2;
        seconds = 0;
        hundredsOfSeconds = 0;
        return getTime();
    }

    public String countTime() {
        if (seconds + minutes + hundredsOfSeconds == 0) {
        } else if (seconds == 0 && hundredsOfSeconds == 0) {
            --minutes;
            seconds = 59;
            hundredsOfSeconds = 99;
        } else if (hundredsOfSeconds == 0) {
            --seconds;
            hundredsOfSeconds = 99;
        } else {
            --hundredsOfSeconds;
        }
        return getTime();
    }

    public String getTime() {
        String textMinute = "0" + minutes;
        String textSecond = (seconds < 10) ? ("0" + seconds) : Integer.toString(seconds);
        String textHundredOfSecond  = (hundredsOfSeconds < 10) ? ("0" + hundredsOfSeconds) : Integer.toString(hundredsOfSeconds);
        return textMinute + " : " + textSecond + " : " + textHundredOfSecond;
    }

    public int getTimeLeft() {
        if (getTime().equals("00 : 00 : 00")) {
            return 0;
        } else {
            return minutes * 60 + seconds + 1;
        }
    }
}
