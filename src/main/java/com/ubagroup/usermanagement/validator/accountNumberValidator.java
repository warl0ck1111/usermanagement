package com.ubagroup.usermanagement.validator;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class accountNumberValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        boolean result = false;
        if (s.length() != 10){
            result = false;
        }
        for(char c : s.toCharArray()){
            if (!Character.isDigit(c)){
                result = false;
                break;
            }
        }
        return result;
    }

}
