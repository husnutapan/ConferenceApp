package com.conference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ConferenceManager {
    private List<Talk> talksValidList;

    public ConferenceManager(List<Talk> talksValidList) {
        this.talksValidList = talksValidList;
    }

    public List<List<Talk>> scheduleConferenceWithInformationFromFile() throws Exception {
        return getScheduleConferenceTrack(talksValidList);
    }

    protected List<List<Talk>> getScheduleConferenceTrack(List<Talk> talksList) throws Exception {
        // Find the total possible days.
        int perDayMinTime = 6 * 60;
        int totalTalksTime = getTotalTalksTime(talksList);
        int totalPossibleDays = totalTalksTime/perDayMinTime;
        // Sort the talkList.
        List<Talk> talksListForOperation = new ArrayList<Talk>();
        talksListForOperation.addAll(talksList);
        Collections.sort(talksListForOperation);
        // Find possible combinations for the morning session.
        List<List<Talk>> morningSessions = findPossibleCombSession(talksListForOperation, totalPossibleDays, true);
        // Remove all the scheduled talks for morning session, from the operationList.
        for(List<Talk> talkList : morningSessions) {
            talksListForOperation.removeAll(talkList);
        }
        // Find possible combinations for the evening session.
        List<List<Talk>> eveningSessions = findPossibleCombSession(talksListForOperation, totalPossibleDays, false);
        // Remove all the scheduled talks for evening session, from the operationList.
        for(List<Talk> talkList : eveningSessions) {
            talksListForOperation.removeAll(talkList);
        }
        // check if the operation list is not empty, then try to fill all the remaining talks in evening session.
        int maxSessionTimeLimit = 240;
        if(!talksListForOperation.isEmpty()) {
            List<Talk> scheduledTalkList = new ArrayList<Talk>();
            for(List<Talk> talkList : eveningSessions) {
                int totalTime = getTotalTalksTime(talkList);

                for(Talk talk : talksListForOperation) {
                    int talkTime = talk.getTimeDuration();

                    if(talkTime + totalTime <= maxSessionTimeLimit) {
                        talkList.add(talk);
                        talk.setScheduled(true);
                        scheduledTalkList.add(talk);
                    }
                }

                talksListForOperation.removeAll(scheduledTalkList);
                if(talksListForOperation.isEmpty())
                    break;
            }
        }
        // If operation list is still not empty, its mean the conference can not be scheduled with the provided data.
        if(!talksListForOperation.isEmpty()) {
            throw new Exception("Unable to schedule all task for the conference.");
        }
        // Schedule the day event from morning session and evening session.
        return getScheduledTalksList(morningSessions, eveningSessions);
    }

    public static int getTotalTalksTime(List<Talk> talksList) {
        if(talksList == null || talksList.isEmpty())
            return 0;

        int totalTime = 0;
        for(Talk talk : talksList) {
            totalTime += talk.getTimeDuration();
        }
        return totalTime;
    }

    private List<List<Talk>> findPossibleCombSession(List<Talk> talksListForOperation, int totalPossibleDays, boolean morningSession){
        int minSessionTimeLimit = 180;
        int maxSessionTimeLimit = 240;

        if(morningSession)
            maxSessionTimeLimit = minSessionTimeLimit;

        int talkListSize = talksListForOperation.size();
        List<List<Talk>> possibleCombinationsOfTalks = new ArrayList<List<Talk>>();
        int possibleCombinationCount = 0;
        // Loop to get combination for total possible days.
        // Check one by one from each talk to get possible combination.
        for(int count = 0; count < talkListSize; count++) {
            int startPoint = count;
            int totalTime = 0;
            List<Talk> possibleCombinationList = new ArrayList<Talk>();
            // Loop to get possible combination.
            while(startPoint != talkListSize) {
                int currentCount = startPoint;
                startPoint++;
                Talk currentTalk = talksListForOperation.get(currentCount);
                if(currentTalk.isScheduled())
                    continue;
                int talkTime = currentTalk.getTimeDuration();
                // If the current talk time is greater than maxSessionTimeLimit or
                // sum of the current time and total of talk time added in list  is greater than maxSessionTimeLimit.
                // then continue.
                if(talkTime > maxSessionTimeLimit || talkTime + totalTime > maxSessionTimeLimit) {
                    continue;
                }
                possibleCombinationList.add(currentTalk);
                totalTime += talkTime;
                // If total time is completed for this session than break this loop.
                if(morningSession) {
                    if(totalTime == maxSessionTimeLimit)
                        break;
                }else if(totalTime >= minSessionTimeLimit)
                    break;
            }
            // Valid session time for morning session is equal to maxSessionTimeLimit.
            // Valid session time for evening session is less than or equal to maxSessionTimeLimit and greater than or equal to minSessionTimeLimit.
            boolean validSession;
            if(morningSession)
                validSession = (totalTime == maxSessionTimeLimit);
            else
                validSession = (totalTime >= minSessionTimeLimit && totalTime <= maxSessionTimeLimit);

            // If session is valid than add this session in the possible combination list and set all added talk as scheduled.
            if(validSession) {
                possibleCombinationsOfTalks.add(possibleCombinationList);
                for(Talk talk : possibleCombinationList){
                    talk.setScheduled(true);
                }
                possibleCombinationCount++;
                if(possibleCombinationCount == totalPossibleDays)
                    break;
            }
        }
        return possibleCombinationsOfTalks;
    }

    private List<List<Talk>> getScheduledTalksList(List<List<Talk>> morningSessionsTrack, List<List<Talk>> eveningSessionsTrack){
        List<List<Talk>> scheduledTalksList = new ArrayList<List<Talk>>();
        int totalPossibleDays = morningSessionsTrack.size();

        // for loop to schedule event for all days.
        for(int dayCount = 0; dayCount < totalPossibleDays; dayCount++) {
            List<Talk> talkList = new ArrayList<Talk>();

            // Create a date and initialize start time 09:00 AM.
            Date date = new Date( );
            SimpleDateFormat dateFormat = new SimpleDateFormat ("hh:mma ");
//            date.setTime(9);
            date.setHours(9);
            date.setMinutes(0);
            date.setSeconds(0);

            int trackCount = dayCount + 1;
            String scheduledTime = dateFormat.format(date);

            System.out.println("Track " + trackCount + ":");

            // Morning Session - set the scheduled time in the talk and get the next time using time duration of current talk.
            List<Talk> mornSessionTalkList = morningSessionsTrack.get(dayCount);
            for(Talk talk : mornSessionTalkList) {
                talk.setScheduledTime(scheduledTime);
                System.out.println(scheduledTime + talk.getTitle());
                scheduledTime = getNextScheduledTime(date, talk.getTimeDuration());
                talkList.add(talk);
            }

            // Scheduled Lunch Time for 60 min.
            int lunchTimeDuration = 60;
            Talk lunchTalk = new Talk("Lunch", "Lunch", 60);
            lunchTalk.setScheduledTime(scheduledTime);
            talkList.add(lunchTalk);
            System.out.println(scheduledTime + "Lunch");

            // Evening Session - set the scheduled time in the talk and get the next time using time duration of current talk.
            scheduledTime = getNextScheduledTime(date, lunchTimeDuration);
            List<Talk> eveSessionTalkList = eveningSessionsTrack.get(dayCount);
            for(Talk talk : eveSessionTalkList) {
                talk.setScheduledTime(scheduledTime);
                talkList.add(talk);
                System.out.println(scheduledTime + talk.getTitle());
                scheduledTime = getNextScheduledTime(date, talk.getTimeDuration());
            }

            // Scheduled Networking Event at the end of session, Time duration is just to initialize the com.conference.Talk object.
            Talk networkingTalk = new Talk("Networking Event", "Networking Event", 60);
            networkingTalk.setScheduledTime(scheduledTime);
            talkList.add(networkingTalk);
            System.out.println(scheduledTime + "Networking Event\n");
            scheduledTalksList.add(talkList);
        }
        return scheduledTalksList;
    }

    private String getNextScheduledTime(Date date, int timeDuration) {
        long timeInLong  = date.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat ("hh:mma ");

        long timeDurationInLong = timeDuration * 60 * 1000;
        long newTimeInLong = timeInLong + timeDurationInLong;

        date.setTime(newTimeInLong);
        String timeString = dateFormat.format(date);
        return timeString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConferenceManager)) return false;

        ConferenceManager that = (ConferenceManager) o;

        if (!talksValidList.equals(that.talksValidList)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
