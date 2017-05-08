package com.globant.automation.trainings.texts;

import static com.globant.automation.trainings.texts.Dictionary.DICTIONARY;

public class Texts {

    private Texts() {
    }

    public static String Sample_Multi_Locale_Text() {
        return DICTIONARY.get("search.text");
    }

}
