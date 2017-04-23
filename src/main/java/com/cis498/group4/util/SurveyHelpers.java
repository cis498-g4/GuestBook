package com.cis498.group4.util;

import com.cis498.group4.models.Survey;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * The SurveyHelpers class contains methods to assist with Survey data (verification, etc)
 */
public class SurveyHelpers {

    /**
     * Validates a survey record (e.g. survey does not already exist).
     * Use before writing to database.
     * @param survey
     * @return
     */
    public static boolean validate(Survey survey) {
        //TODO
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

}
