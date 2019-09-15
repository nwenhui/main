package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class deals with manipulating a string into a date if it is possible to.
 */
public class TimeParser {
//    static Date date;

    /**
     * Manipulate a string into an date and return it back as string.
     * If not, simply just return back the string.
     */
    public static Date convertStringToDate(String time) {
        Date date;
        String line;
        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HHmm");
            date = formatter1.parse(time);
            return date;
//            line = convertDateToLine(date);
//            return line;
        } catch (ParseException e1) {
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy HHmm");
            try {
                date = formatter2.parse(time);
                return date;
//                line = convertDateToLine(date);
//                return line;
            } catch (ParseException e2) {
                SimpleDateFormat formatter3 = new SimpleDateFormat("MMM dd yyyy HHmm");
                try {
                    date = formatter3.parse(time);
                    return date;
//                    line = convertDateToLine(date);
//                    return line;
                } catch (ParseException e3) {
                    SimpleDateFormat formatter4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        date = formatter4.parse(time);
                        return date;
//                        line = convertDateToLine(date);
//                        return line;
                    } catch (ParseException e4) {
                        SimpleDateFormat formatter5 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        try {
                            date = formatter5.parse(time);
                            return date;
//                            line = convertDateToLine(date);
//                            return line;
                        } catch (ParseException e5) {
                            SimpleDateFormat formatter6 = new SimpleDateFormat("MMM dd yyyy HHmm");
                            try {
                                date = formatter6.parse(time);
                                return date;
//                                line = convertDateToLine(date);
//                                return line;
                            } catch (ParseException e6) {

                                SimpleDateFormat formatter7 = new SimpleDateFormat("dd'st of' MMMMMMMM yyyy',' hh:mm aaa");
                                try {
                                    date = formatter7.parse(time);
                                    return date;
                                } catch (ParseException e7) {
                                    SimpleDateFormat formatter8 = new SimpleDateFormat("dd'nd of' MMMMMMMM yyyy',' hh:mm aaa");
                                    try {
                                        date = formatter8.parse(time);
                                        return date;
                                    } catch (ParseException e8) {

                                        SimpleDateFormat formatter9 = new SimpleDateFormat("dd'rd of' MMMMMMMM yyyy',' hh:mm aaa");
                                        try {
                                            date = formatter9.parse(time);
                                            return date;
                                        } catch (ParseException e9) {

                                            SimpleDateFormat formatter10 = new SimpleDateFormat("dd'th of' MMMMMMMM yyyy',' hh:mm aaa");
                                            try {
                                                date = formatter10.parse(time);
                                                return date;
                                            } catch (ParseException e10) {

                                                return null;

                                            }

                                        }

                                    }

                                }
//

                            }

                        }
                    }

                }
            }
        }
    }


    /**
     * Converts a date back to string and returns the string.
     */
    public static String convertDateToLine(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int actualDate = calendar.get(Calendar.DATE);
        if ((actualDate <= 3) || (actualDate >= 21)) {
            if (actualDate % 10 == 1) {
                return new SimpleDateFormat("dd'st of' MMMMMMMM yyyy',' hh:mm aaa").format(date);
            } else if (actualDate % 10 == 2) {
                return new SimpleDateFormat("dd'nd of' MMMMMMMM yyyy',' hh:mm aaa").format(date);
            } else if (actualDate % 10 == 3) {
                return new SimpleDateFormat("dd'rd of' MMMMMMMM yyyy',' hh:mm aaa").format(date);
            } else {
                return new SimpleDateFormat("dd'th of' MMMMMMMM yyyy',' hh:mm aaa").format(date);
            }
        } else {
            return new SimpleDateFormat("dd'th of' MMMMMMMM yyyy',' hh:mm aaa").format(date);
        }
    }

}
