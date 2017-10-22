package com.conference;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ConferenceParserTest {
    private ConferenceParser conferenceParser;
    private PrintStream printStream;
    private String fileName;
    private ConferenceManager conferenceManager;

    @Before
    public void setUp(){
        fileName = "Writing Fast Tests Against Enterprise Rails 60min";
        printStream = mock(PrintStream.class);
        conferenceParser = new ConferenceParser(fileName, printStream);
        conferenceManager = mock(ConferenceManager.class);
    }

    @Test
    public void shouldReturnAConferenceManagerObjectWithFileInputs() throws Exception {
        List<String> talksList = new ArrayList<String>();
        talksList.add(fileName);
        List<Talk> validTalksList = conferenceParser.createValidTalks(talksList);

        ConferenceManager conferenceManager1 = conferenceParser.buildScheduledSetUp();
        ConferenceManager conferenceManager2 = new ConferenceManager(validTalksList);

        assertEquals(conferenceManager1.hashCode(), conferenceManager2.hashCode());
    }


}