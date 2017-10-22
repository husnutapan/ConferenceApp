package com.conference;

public class Talk implements Comparable{
    private String title;
    private String name;
    private int timeDuration;
    boolean scheduled = false;
    String scheduledTime;

    public Talk(String title, String name, int time) {
        this.title = title;
        this.name = name;
        this.timeDuration = time;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public int getTimeDuration() {
        return timeDuration;
    }

    public String getTitle() {
        return title;
    }


//     Sort data in descending order.



    @Override
    public int compareTo(Object obj)
    {
        Talk talk = (Talk)obj;
        if(this.timeDuration > talk.timeDuration)
            return -1;
        else if(this.timeDuration < talk.timeDuration)
            return 1;
        else
            return 0;
    }
}


