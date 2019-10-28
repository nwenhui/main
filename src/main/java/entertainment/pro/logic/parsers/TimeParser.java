package entertainment.pro.logic.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class deals with manipulating a string into a date if it is possible to.
 */
public class TimeParser {
    static Date date;

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
            //line = convertDateToLine(date);
            // return line;
        } catch (ParseException e1) {
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy HHmm");
            try {
                date = formatter2.parse(time);
                return date;
                //line = convertDateToLine(date);
                // return line;
            } catch (ParseException e2) {
                SimpleDateFormat formatter3 = new SimpleDateFormat("MMM dd yyyy HHmm");
                try {
                    date = formatter3.parse(time);
                    return date;
                    //line = convertDateToLine(date);
                    //return line;
                } catch (ParseException e3) {
                    SimpleDateFormat formatter4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        date = formatter4.parse(time);
                        return date;
                        //line = convertDateToLine(date);
                        // return line;
                    } catch (ParseException e4) {
                        SimpleDateFormat formatter5 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        try {
                            date = formatter5.parse(time);
                            return date;
                            //line = convertDateToLine(date);
                            // return line;
                        } catch (ParseException e5) {
                            SimpleDateFormat formatter6 = new SimpleDateFormat("MMM dd yyyy HHmm");
                            try {
                                date = formatter6.parse(time);
                                return date;
                                //line = convertDateToLine(date);
                                // return line;
                            } catch (ParseException e6) {

                                SimpleDateFormat formatter7 =
                                        new SimpleDateFormat("dd'st of' MMMMMMMM yyyy',' hh:mm aaa");
                                try {
                                    date = formatter7.parse(time);
                                    return date;
                                } catch (ParseException e7) {
                                    SimpleDateFormat formatter8 =
                                            new SimpleDateFormat("dd'nd of' MMMMMMMM yyyy',' hh:mm aaa");
                                    try {
                                        date = formatter8.parse(time);
                                        return date;
                                    } catch (ParseException e8) {

                                        SimpleDateFormat formatter9
                                                = new SimpleDateFormat("dd'rd of' MMMMMMMM yyyy',' hh:mm aaa");
                                        try {
                                            date = formatter9.parse(time);
                                            return date;
                                        } catch (ParseException e9) {

                                            SimpleDateFormat formatter10
                                                    = new SimpleDateFormat("dd'th of' MMMMMMMM yyyy',' hh:mm aaa");
                                            try {
                                                date = formatter10.parse(time);
                                                return date;
                                            } catch (ParseException e10) {

                                                SimpleDateFormat formatter11 = new SimpleDateFormat("dd/MM/yyyy");
                                                try {
                                                    date = formatter11.parse(time);
                                                    return date;
                                                } catch (ParseException e11) {

                                                    return null;

                                                }


                                            }


                                        }

                                    }

                                }
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

    /**
     * This function returns difference between two dates.
     */
    public static long getDiffHours(Date date1, Date date2) {
        long diff = Math.abs(date1.getTime() - date2.getTime());
        long diffHours = diff / (60 * 60 * 1000);

        return diffHours;
    }


    /**
     * Get time portion of Date object only and convert it to String.
     */
    public static String getStringTime(Date date) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aaa");
        return targetFormat.format(date);
    }


    public static String getStringDate(Date date) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("d/MM/yyy");
        return targetFormat.format(date);
    }

    /**
     * Convert Date object to contain date only (no time).
     */
    public static Date getDateOnly(Date date) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return targetFormat.parse(targetFormat.format(date));
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * Convert string of date only to Date object.
     */
    public static Date convertToDate(String s) {
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("d/MM/yyyy");
            return sourceFormat.parse(s);
        } catch (ParseException e1) {
            try {
                SimpleDateFormat sourceFormat = new SimpleDateFormat("d-MM-yyyy");
                return sourceFormat.parse(s);
            } catch (ParseException e2) {
                return null;
            }
        }
    }
}

