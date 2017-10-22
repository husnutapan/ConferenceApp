package com.conference;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ConferenceManagerTest {
    private PrintStream printStream;
    private String filename;
    private Talk talk;
    private ConferenceManager conferenceManager;
    private ConferenceParser conferenceParser;
    private List<Talk> validListTalks;

    @Before
    public void setUp() throws Exception {
        filename = "string";
        printStream = mock(PrintStream.class);
        talk = mock(Talk.class);
        conferenceParser = new ConferenceParser(filename,printStream);
        validListTalks = new ArrayList<Talk>();
        validListTalks.add(talk);
        conferenceManager = new ConferenceManager(validListTalks);
    }

    @Test
    public void shouldReturnEmptyListIfAllTalksWereScheduledInConference() throws Exception {
        validListTalks.remove(talk);
        conferenceManager.getScheduleConferenceTrack(validListTalks);
        assertTrue(conferenceManager.scheduleConferenceWithInformationFromFile().isEmpty());
    }

}