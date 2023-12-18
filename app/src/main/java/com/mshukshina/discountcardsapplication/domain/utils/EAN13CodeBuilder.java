package com.mshukshina.discountcardsapplication.domain.utils;

public class EAN13CodeBuilder {
    private final String codeStringValue;
    private final String fullCode;

    public EAN13CodeBuilder(String codeString) {
        codeStringValue = codeString;
        fullCode = createFullCode();
    }

    public String getFullCode() {
        return fullCode;
    }

    private String createFullCode() {

        int evenVal = 0;
        int oddVal = 0;
        String codeToParse = codeStringValue;

        for (int index = 0; index < 6; index++) {
            evenVal += Integer.parseInt(codeToParse.substring(
                    index * 2 + 1, index * 2 + 2));
            oddVal += Integer.parseInt(codeToParse.substring(
                    index * 2, index * 2 + 1));
        }

        evenVal *= 3;
        int controlNumber = 10 - (evenVal + oddVal) % 10;
        if (controlNumber == 10) controlNumber = 0;

        codeToParse += String.valueOf(controlNumber);

        return codeToParse;
    }
}