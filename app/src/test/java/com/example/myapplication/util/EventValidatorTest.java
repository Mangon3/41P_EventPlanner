package com.example.myapplication.util;

import org.junit.Test;
import java.util.Calendar;
import static org.junit.Assert.*;

public class EventValidatorTest {

    @Test
    public void validateEvent_emptyTitle_returnsFalse() {
        boolean isValid = EventValidator.isValid("   ", 1700000000000L); // Some valid future date
        assertFalse("Empty title should be invalid", isValid);
    }

    @Test
    public void validateEvent_pastDate_returnsFalse() {
        Calendar pastCal = Calendar.getInstance();
        pastCal.add(Calendar.DAY_OF_YEAR, -1);
        boolean isValid = EventValidator.isValid("Valid Title", pastCal.getTimeInMillis());
        assertFalse("Past date should be invalid", isValid);
    }

    @Test
    public void validateEvent_validInput_returnsTrue() {
        Calendar futureCal = Calendar.getInstance();
        futureCal.add(Calendar.DAY_OF_YEAR, 2);
        boolean isValid = EventValidator.isValid("Valid Title", futureCal.getTimeInMillis());
        assertTrue("Valid input should pass", isValid);
    }
}
