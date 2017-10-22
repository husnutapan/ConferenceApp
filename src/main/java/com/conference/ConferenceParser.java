package com.conference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConferenceParser {
    private String fileName;
    private PrintStream printStream;

    public ConferenceParser(String fileName, PrintStream printStream) {
        this.fileName = fileName;
        this.printStream = printStream;
    }

    public List<String> getTalksFromFile(String fileName) {
        List<String> talkList = new ArrayList<String>();
        try {
            FileReader inputFile = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(inputFile);
            String strLine = bufferedReader.readLine();
//            Read File line by line
            while (strLine !=null){
                talkList.add(strLine);
                strLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            printStream.println("Error on the file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return talkList;
    }

    public List<Talk> createValidTalks(List<String> talkList) throws Exception {
        // If talksList is null throw exception invalid list to schedule.
        if(talkList == null)
            throw new InvalidTalkException("Empty com.conference.Talk List");

        List<Talk> validTalksList = new ArrayList<Talk>();
        String minSuffix = "min";
        String lightningSuffix = "lightning";

        // Iterate list and validate time.
        for(String talk : talkList)
        {
            int lastSpaceIndex = talk.lastIndexOf(" ");
            // if talk does not have any space, means either title or time is missing.
            if(lastSpaceIndex == -1)
                throw new InvalidTalkException("Invalid talk: " + talk + ". Invalid talk time.");

            String name = talk.substring(0, lastSpaceIndex);
            String timeStr = talk.substring(lastSpaceIndex + 1);
            // If title is missing or blank.
            if(name == null || "".equals(name.trim()))
                throw new InvalidTalkException("Invalid talk name: " + talk);
                // If time is not ended with min or lightning.
            else if(!timeStr.endsWith(minSuffix) && !timeStr.endsWith(lightningSuffix))
                throw new InvalidTalkException("Invalid talk time: " + talk + ". Enter time in min or in lightning");

            int time = 0;
            // Parse time from the time string .
            try{
                if(timeStr.endsWith(minSuffix)) {
                    time = Integer.parseInt(timeStr.substring(0, timeStr.indexOf(minSuffix)));
                }
                else if(timeStr.endsWith(lightningSuffix)) {
                    String lightningTime = timeStr.substring(0, timeStr.indexOf(lightningSuffix));
                    if("".equals(lightningTime))
                        time = 5;
                    else
                        time = Integer.parseInt(lightningTime) * 5;
                }
            }catch(NumberFormatException e) {
                throw new InvalidTalkException("Unable to get time information from file.  Error on time: " + timeStr + " for talk " + talk);
            }
            // Add talk to the valid talk List.
            validTalksList.add(new Talk(talk, name, time));
        }
        return validTalksList;
    }

    public ConferenceManager buildScheduledSetUp() {
        List<String> talkList = getTalksFromFile(fileName);
        List<Talk> talksValidList = null;
        try {
            talksValidList = createValidTalks(talkList);
        } catch (InvalidTalkException e) {
            printStream.println("Invalid input from file");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConferenceManager conferenceManager = new ConferenceManager(talksValidList);
        return conferenceManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConferenceParser)) return false;

        ConferenceParser that = (ConferenceParser) o;

        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (printStream != null ? !printStream.equals(that.printStream) : that.printStream != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}



