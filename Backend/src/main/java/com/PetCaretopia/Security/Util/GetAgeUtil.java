package com.PetCaretopia.Security.Util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class GetAgeUtil {
    public String getAge(LocalDate birthDate) {
        if (birthDate == null) {
            return "Unknown";
        }

        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);

        if (period.getYears() > 0) {
            return period.getYears() + (period.getYears() == 1 ? " Year" : " Years");
        } else if (period.getMonths() > 0) {
            return period.getMonths() + (period.getMonths() == 1 ? " Month" : " Months");
        } else {
            return period.getDays() + (period.getDays() == 1 ? " Day" : " Days");
        }
    }
}
