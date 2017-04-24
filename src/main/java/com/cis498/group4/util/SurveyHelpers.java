package com.cis498.group4.util;

import com.cis498.group4.models.Survey;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The SurveyHelpers class contains methods to assist with Survey data (verification, etc)
 */
public class SurveyHelpers {

    public static final String[] QUESTIONS = {
            "Overall, how would you rate your satisfaction with the event?",
            "How satisfied were you with the quality of the information presented?",
            "How relevant did you find the information in the presentation?",
            "How satisfied are you with the presenter's knowledge of the subject?",
            "How satisfied are you with the quality of the presentation?",
            "How likely are you to attend another event by the same presenter?",
            "How satisfied were you with the organization of the event?",
            "How satisfied were you with the staff at the event?",
            "How well did we respond to your questions and concerns?",
            "How likely are you to recommend the event to a friend or colleague?"
    };

    public static final String[] RESPONSE_TYPES = {
            "satisfied",
            "satisfied",
            "relevant",
            "satisfied",
            "satisfied",
            "likely",
            "satisfied",
            "satisfied",
            "well",
            "likely"
    };

    /**
     * Validates a survey record (e.g. survey does not already exist).
     * Use before writing to database.
     * @param survey
     * @return
     */
    public static boolean validate(Survey survey) {
        // TODO User attended event
        // TODO Survey does not already exist
        return true;
    }

    /**
     * Calculates and returns the arithmetic mean for this Survey
     * @return BigDecimal average with scale of 2
     */
    public static BigDecimal responseAverage(Survey survey) {
        BigDecimal sum = new BigDecimal(0);

        Iterator<Integer> it = survey.getResponses().values().iterator();
        while (it.hasNext()) {
            sum = sum.add(new BigDecimal(it.next()));
        }

        return sum.divide(BigDecimal.TEN, 1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Generates a sentiment string from a response value
     * @param value
     * @return
     */
    public static String responseSentiment(double value) {
        if (value < 2.5) {
            return "very negative";
        }

        if (value >= 2.5 && value < 5.0) {
            return "somewhat negative";
        }

        if (value >= 5.0 && value < 7.5) {
            return "somewhat positive";
        }

        if (value >= 7.5) {
            return "very positive";
        }

        return "ambiguous";
    }

    /**
     * Remaps survey responses to question strings
     * @param survey
     * @return
     */
    public static Map<String, Integer> getQuestionsResponses(Survey survey) {
        Map<String, Integer> questionsResponses = new HashMap<String, Integer>();
        Map<String, Integer> responses = survey.getResponses();

        for(int i = 0; i < 10; i++) {
            String responseLabel = String.format("response_%02d", i + 1);
            questionsResponses.put(QUESTIONS[i], responses.get(responseLabel));
        }

        return questionsResponses;
    }

}
