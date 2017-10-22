package com.conference;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TalkTest {
    private Talk talk;

    @Before
    public void setUp(){
        talk = new Talk("title", "name", 60);
    }

    @Test
    public void shouldReturnTheTimeDurationScheduleInConference(){
        assertEquals(60, talk.getTimeDuration());
    }

    @Test
    public void shouldReturnTitleScheduleInConference(){
        assertEquals("title", talk.getTitle());
    }

    @Test
    public void shouldReturnTrueIfSetScheduledInConference(){
        talk.setScheduled(true);
        assertEquals(true, talk.isScheduled() );
    }

    @Test
    public void shouldReturnSomething(){
        talk.setScheduledTime("30");
        assertEquals("30", talk.scheduledTime);
    }

}