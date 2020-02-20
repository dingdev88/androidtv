package com.selecttvapp.common;

/**
 * Created by babin on 2/23/2017.
 */

public class XmlDuration {
    private String _xmlDuration;
    private int _years;
    private int _months;
    private int _days;
    private int _hours;
    private int _minutes;
    private int _seconds;

    private boolean _isNegative;

    public XmlDuration(String xmlDuration) {
        try {

            _xmlDuration = xmlDuration;

            _isNegative = ((String)_xmlDuration.subSequence(0,1)).matches("[-]");

            String period;
            String time;

            int tIndex =_xmlDuration.indexOf("T");

            period = xmlDuration.substring(0, tIndex);
            time = _xmlDuration.substring(tIndex);

            String numericSection = "";

            for (int i = 0; i < period.length(); i++) {
                char[] c = new char[] {period.charAt(i)};
                String s = new String(c);

                if(s.matches("\\d"))
                {
                    numericSection += s;
                }
                else if (s.matches("[Yy]"))
                {
                    _years = Integer.parseInt(numericSection);
                    numericSection = "";
                }
                else if (s.matches("[Mm]"))
                {
                    _months = Integer.parseInt(numericSection);
                    numericSection = "";
                }
                else if (s.matches("[Dd]"))
                {
                    _days = Integer.parseInt(numericSection);
                    numericSection = "";
                }

            }

            for (int i = 0; i < time.length(); i++) {
                char[] c = new char[] {time.charAt(i)};
                String s = new String(c);

                if(s.matches("\\d"))
                {
                    numericSection += s;
                }
                else if (s.matches("[Hh]"))
                {
                    _hours = Integer.parseInt(numericSection);
                    numericSection = "";
                }
                else if (s.matches("[Mm]"))
                {
                    _minutes = Integer.parseInt(numericSection);
                    numericSection = "";
                }
                else if (s.matches("[Ss]"))
                {
                    _seconds = Integer.parseInt(numericSection);
                    numericSection = "";
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        }



    }

    public String getXmlString()
    {
        return _xmlDuration;
    }

    public int getYears()
    {
        return _years;
    }

    public int getMonth()
    {
        return _months;
    }

    public int getDays()
    {
        return _days;
    }

    public int getHours()
    {
        return _hours;
    }

    public int getMinutes()
    {
        return _minutes;
    }

    public int getSeconds()
    {
        return _seconds;
    }

    public boolean getIsNegative()
    {
        return _isNegative;
    }
}
