package com.example.discountcardsapplication.utils;

public class EAN13CodeBuilder {
    private String codeStringValue;
    private String generatedCode;
    private String fullCode;

    public EAN13CodeBuilder(String codeString)
    {
        codeStringValue = codeString;
        parse();
    }

    public String getFullCode()
    {
        return fullCode;
    }
    public String getCode()
    {
        return generatedCode;

    }

    ////////////////////////////////////////////////////////////////
    // this method generates EAN 13 control number ans returns full
    // string to encode
    private String createFullCode()
    {

        int chetVal = 0, nechetVal = 0;
        String codeToParse = codeStringValue;

        for( int index = 0;index<6;index++ )
        {
            chetVal += Integer.valueOf(codeToParse.substring(
                    index*2+1,index*2+2)).intValue();
            nechetVal += Integer.valueOf(codeToParse.substring(
                    index*2,index*2+1)).intValue();
        }

        chetVal *= 3;
        int controlNumber = 10 - (chetVal+nechetVal)%10;
        if( controlNumber == 10 ) controlNumber  = 0;

        codeToParse += String.valueOf(controlNumber);

        return codeToParse;

    }

    private String DigitToUpperCase( String digit)
    {
        String letters  = "ABCDEFGHIJ";
        int position = Integer.valueOf(digit).intValue();

        String retVal = letters.substring(position,position+1);

        return retVal;

    }

    private String DigitToLowerCase( String digit)
    {
        String letters  = "abcdefghij";
        int position = Integer.valueOf(digit).intValue();

        String retVal = letters.substring(position,position+1);

        return retVal;

    }
    //////////////////////////////////////////////
    // this method generates EAN 13 encoded string
    // algorithm can be found at http://en.wikipedia.org/wiki/EAN-13
    private String createEAN13Code(String rawCode)
    {
        int firstFlag = Integer.valueOf(

                rawCode.substring(0,1)

        ).intValue();

        String leftString = rawCode.substring(1,7);
        String rightString = rawCode.substring(7);

        String rightCode = "";
        String leftCode = "";

        for( int i=0;i<6;i++)
        {
            rightCode += DigitToLowerCase( rightString.substring(i,i+1) );
        }



        if( firstFlag == 0 )
        {
            leftCode = "#!"+leftString.substring(0,1)
                    +leftString.substring(1,2)
                    +leftString.substring(2,3)
                    +leftString.substring(3,4)
                    +leftString.substring(4,5)
                    +leftString.substring(5);
        }
        if( firstFlag == 1 )
        {

            leftCode = "$!"+leftString.substring(0,1)
                    +leftString.substring(1,2)
                    +DigitToUpperCase(leftString.substring(2,3))
                    +leftString.substring(3,4)
                    +DigitToUpperCase(leftString.substring(4,5))
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 2 )
        {
            leftCode = "%!"+leftString.substring(0,1)
                    +leftString.substring(1,2)
                    +DigitToUpperCase(leftString.substring(2,3))
                    +DigitToUpperCase(leftString.substring(3,4))
                    +leftString.substring(4,5)
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 3 )
        {
            leftCode = "&!"+leftString.substring(0,1)
                    +leftString.substring(1,2)
                    +DigitToUpperCase(leftString.substring(2,3))
                    +DigitToUpperCase(leftString.substring(3,4))
                    +DigitToUpperCase(leftString.substring(4,5))
                    +leftString.substring(5);
        }
        if( firstFlag == 4 )
        {
            leftCode = "'!"+leftString.substring(0,1)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +leftString.substring(2,3)
                    +leftString.substring(3,4)
                    +DigitToUpperCase(leftString.substring(4,5))
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 5 )
        {
            leftCode = "(!"+leftString.substring(0,1)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +DigitToUpperCase(leftString.substring(2,3))
                    +leftString.substring(3,4)
                    +leftString.substring(4,5)
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 6 )
        {
            leftCode = ")!"+leftString.substring(0,1)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +DigitToUpperCase(leftString.substring(2,3))
                    +DigitToUpperCase(leftString.substring(3,4))
                    +leftString.substring(4,5)
                    +leftString.substring(5);
        }
        if( firstFlag == 7 )
        {
            leftCode = "*!"+leftString.substring(0,1)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +leftString.substring(2,3)
                    +DigitToUpperCase(leftString.substring(3,4))
                    +leftString.substring(4,5)
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 8 )
        {
            leftCode = "+!"+leftString.substring(0,1)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +leftString.substring(2,3)
                    +DigitToUpperCase(leftString.substring(3,4))
                    +DigitToUpperCase(leftString.substring(4,5))
                    +leftString.substring(5);
        }
        if( firstFlag == 9 )
        {
            leftCode = ",!"+leftString.substring(0,1)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +DigitToUpperCase(leftString.substring(2,3))
                    +leftString.substring(3,4)
                    +DigitToUpperCase(leftString.substring(4,5))
                    +leftString.substring(5);
        }



        String retVal = leftCode + "-" + rightCode + "!";

        return retVal;
    }

    private void parse()
    {
        fullCode = createFullCode();
        System.out.println( "Full code: " + fullCode );

        generatedCode = createEAN13Code(fullCode);

        System.out.println( "Generated code: " + generatedCode );

    }
}
