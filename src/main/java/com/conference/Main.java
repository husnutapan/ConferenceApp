package com.conference;

import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        PrintStream printStream = System.out;
	    String fileName = "resources/source.txt";

        ConferenceParser conferenceParser = new ConferenceParser(fileName, printStream);
        ConferenceManager conferenceManager = conferenceParser.buildScheduledSetUp();

        try {
            conferenceManager.scheduleConferenceWithInformationFromFile();
        } catch (InvalidTalkException e) {
            printStream.println("Error trying to schedule your events. Please review your file");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
