
package com.crud.a4.boot.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * General purpose utility class.
 */
public class Util
{
    private static final Log    log                  = LogFactory.getLog(Util.class);

    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\+[0-9]{1,20}");

    private static final Random RANDOM               = new SecureRandom();
    private static final String PASS_CHARS           =
        "abcdefghijklmnopqrstuvwxyz!@#$%^&*()-+<>ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final int     PASSWORD_LENGTH      = 10;

    /**
     * Private constructor prevents instantiation.
     */
    private Util()
    {}

    /**
     * Add all of the array's items to the collection.
     * 
     * @param collection
     * @param objects array of objects
     */
    public static <T> void addAll(Collection<T> collection, T[] objects)
    {
        for (T object : objects)
        {
            collection.add(object);
        }
    }

    /**
     * Scans all elements of a collection in search of duplicates, returns all duplicates
     * 
     * @param collection
     * @return
     */
    public static <T> Collection<T> checkForDuplicateEntriesInCollection(Collection<T> collection)
    {
        ArrayList<T> result = new ArrayList<T>();
        Set<T> entries = new HashSet<T>();
        for (T object : collection)
        {
            if (entries.contains(object))
            {
                result.add(object);
            }
            else
            {
                entries.add(object);
            }
        }
        return result;
    }

    /**
     * Change the win32 line break \r\n to \n.
     * 
     * @param string the string to check
     * @param A String without windows line breaks.
     */
    public static String checkLineBreaks(String string)
    {
        String temp = "";

        if (string == null)
        {
            return null;
        }

        StringTokenizer st = new StringTokenizer(string, "\r");

        while (st.hasMoreElements())
        {
            temp += st.nextElement();
        }

        return temp;
    }

    public static <T> T[] concatenateArrays(T[] first, T[] second)
    {
        if (first == null)
        {
            return second;
        }
        if (second == null)
        {
            return first;
        }

        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Check if the first collection contains any elements from the second
     * 
     * @param coll1 collection to look in
     * @param coll2 collection of objects to look for in first collection
     * @return if any object in the second collection is contained in the first
     */
    public static boolean containsAny(Collection<?> coll1, Collection<?> coll2)
    {
        if (coll2.isEmpty())
        {
            return true;
        }
        for (Iterator<?> i = coll2.iterator(); i.hasNext();)
        {
            Object object = i.next();
            if (coll1.contains(object))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param encodedChars DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String convertToUTF(String encodedChars)
    {
        if (encodedChars == null)
        {
            return new String();
        }

        int index = 0;
        boolean unicode = true;

        if (encodedChars.indexOf("&#") != -1)
        {
            String parsedString = "";
            StringTokenizer tokens = new StringTokenizer(encodedChars, ";");
            int numTokens = tokens.countTokens();

            for (int x = 0; x < numTokens; x++)
            {
                unicode = true;

                String charact = tokens.nextToken();
                int charactIndex = charact.indexOf("&#");

                if (charactIndex != -1)
                {
                    try
                    {
                        index = Integer.parseInt(charact.substring(charactIndex + 2, charact.length()));
                    }
                    catch (Exception e)
                    {
                        unicode = false;
                    }
                }
                else
                {
                    unicode = false;
                }

                if (unicode)
                {
                    parsedString += charact.substring(0, charactIndex);
                    parsedString += ("\\u" + Integer.toHexString(index));
                }
                else
                {
                    parsedString += charact;
                }
            }

            encodedChars = loadConvert(parsedString);

            return encodedChars;
        }
        return encodedChars;
    }

    /**
     * Encodes special reserved characters when sending to the distribution list or when queuing a
     * notification
     * 
     * @param pString The String to be encoded.
     * 
     * @return The string encoded to be used when calling sendtodistributionlist and queue methods.
     */
    public static String eDocHTMLEncode(String pString)
    {
        if (pString == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < pString.length(); i++)
        {
            char c = pString.charAt(i);

            switch (c) {
                case ',' :
                    sb.append("&#x002C;");

                    break;

                case '$' :
                    sb.append("&#x0024;");

                    break;

                default :
                    sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Encodes Scandinavian letters and " " and "," Used to encode strings passed to eDoc server
     * side Documentum methods
     * 
     * @param pString The String to be encoded.
     * 
     * @return The string encoded to be used in server side Documentum method calls.
     */
    public static String eDocURLEncode(String pString)
    {
        if (isNullOrEmpty(pString))
        {
            return "%20";
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < pString.length(); i++)
        {
            char c = pString.charAt(i);

            switch (c) {
                case 229 : // \u00E5 &aring;
                    sb.append("%E5");

                    break;

                case 228 : // \u00E4 &auml;
                    sb.append("%E4");

                    break;

                case 246 : // \u00F6 &ouml;
                    sb.append("%F6");

                    break;

                case 197 : // \u00C5 &Aring;
                    sb.append("%C5");

                    break;

                case 196 : // \u00C4 &Auml;
                    sb.append("%C4");

                    break;

                case 214 : // \u00D6 &Ouml;
                    sb.append("%D6");

                    break;

                case ' ' : //
                    sb.append("%20");

                    break;

                case ',' : // \u002C
                    sb.append("%2C");

                    break;

                case '"' : // \u0022
                    sb.append("%22");

                    break;

                case '$' : // \u0024
                    sb.append("%24");

                    break;

                default :
                    sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Return a blank string if null parameter is given, otherwise return Object.toString().
     * 
     * @param object
     * @return object.toString() if parameter wasn't null, otherwise a blank string
     */
    public static String emptyIfNull(Object object)
    {
        if (object == null)
            return "";

        return object.toString();
    }

    public static String encodeIfNullOrEmpty(String pString)
        throws UnsupportedEncodingException
    {
        if (isNullOrEmpty(pString))
        {
            return "%20";
        }

        return URLEncoder.encode(pString, "UTF-8");
    }

    public static boolean equals(String string1, String string2)
    {
        if (string1 == null && string2 == null)
        {
            return true;
        }
        if ((string1 == null && string2 != null) || (string1 != null && string2 == null))
        {
            return false;
        }
        return string1.equals(string2);
    }

    /**
     * Overloaded version that doesn't break the lines.
     * 
     * @param pString DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String escape(String pString)
    {
        return escape(pString, false);
    }

    /**
     * Escape (some) characters on string that is meant to be displayed on a HTML page. This is a
     * modified version of the CIF-one, because it used XML-entities that are not 100% compatible
     * with HTML-entities. This escapes the following characters: &lt; &gt; &quot; &amp; &auml;
     * &ouml; &Auml; &Ouml; and optionally converts new lines into HTML-breakline tags.
     * 
     * @param pString The String to be escaped.
     * @param pBreakLines Should the Carriage Returns and New Lines (Ascii characters 13 and 10) be
     *            converted to &lt;BR&gt; tags?
     * 
     * @return The string escaped with entity references.
     */
    public static String escape(String pString, boolean pBreakLines)
    {
        if (pString == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer((int) (pString.length() * 1.10));
        char[] chars = pString.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];

            switch (c) {
                case 60 :
                    sb.append("&lt;"); // <

                    break;

                case 62 :
                    sb.append("&gt;"); // >

                    break;

                case 34 :
                    sb.append("&quot;"); // "

                    break;

                case 38 :
                    sb.append("&amp;"); // &

                    break;

                case 228 : // \u00E4
                    sb.append("&auml;"); // Do these really work/are these
                    // needed at all?

                    break;

                case 246 : // \u00F6
                    sb.append("&ouml;");

                    break;

                case 252 : // \u00fc
                    sb.append("&uuml;");

                    break;

                case 196 : // \u00C4
                    sb.append("&Auml;");

                    break;

                case 214 : // \u00D6
                    sb.append("&Ouml;");

                    break;

                case 220 : // \u00dc
                    sb.append("&Uuml;");

                    break;

                case 10 :
                case 13 :

                    if (pBreakLines)
                    {
                        sb.append("<br />");

                        break;
                    }

                default :
                    sb.append(c);
            }
        }

        return sb.toString();
    }

    public static <T> List<T> filterDuplicates(List<T> list)
    {
        Set<T> noDuplicates = new LinkedHashSet<T>(list);
        return new ArrayList<T>(noDuplicates);
    }

    /**
     * Takes a Date and time (as a <code>java.util.Date</code>) formats it to the specified pattern
     * as a String.
     * 
     * @param pDate The date to be formatted.
     * @param pPattern Pattern according to which the date should be reformatted.
     * 
     * @return The date formatted accordingly to the specified pattern.
     */
    public static String formatDate(Date pDate, String pPattern)
    {
        DateFormat df = new SimpleDateFormat(pPattern, Locale.US);

        return df.format(pDate);
    }

    /**
     * Overloaded version of <code>formatDate</code> that takes separate date and time patterns and
     * concatenates them together (with an added space), calling the overloaded method.
     * 
     * @param pDate DOCUMENT ME!
     * @param pDatePattern DOCUMENT ME!
     * @param pTimePattern DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String formatDate(Date pDate, String pDatePattern, String pTimePattern)
    {
        return formatDate(pDate, pDatePattern + " " + pTimePattern);
    }

    /*
     * This can be swithced to java.util.UUID when project starts to use JDK 5.0
     */
    public static String generateUniqueId()
    {
        return "" + System.currentTimeMillis() + Thread.currentThread().hashCode();
    }

    public static String getAsSingleQuotedList(Collection<String> strings)
    {
        if (strings == null)
        {
            return "''";
        }

        StringBuffer sb = new StringBuffer();
        for (String string : strings)
        {
            sb.append("'" + string + "',");
        }
        if (sb.length() > 0)
        {
            sb.deleteCharAt(sb.length() - 1);
        }

        log.debug(strings + " --> " + sb.toString());
        return sb.toString();
    }

    public static String getListOfLongAsCommaSeparatedString(List<Long> ids)
    {
        if (ids == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (Long id : ids)
        {
            sb.append("'" + id + "',");
        }
        if (sb.length() > 0)
        {
            sb.deleteCharAt(sb.length() - 1);
        }

        log.debug(ids + " --> " + sb.toString());
        return sb.toString();
    }

    public static String getListOfLongAsCommaSeparated(List<Long> ids)
    {
        if (ids == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (Long id : ids)
        {
            sb.append(id + ",");
        }
        if (sb.length() > 0)
        {
            sb.deleteCharAt(sb.length() - 1);
        }

        log.debug(ids + " --> " + sb.toString());
        return sb.toString();
    }

    public static String getBasename(String string)
    {
        if (string == null)
        {
            return null;
        }
        if (string.lastIndexOf(".") < 0)
        {
            return string;
        }
        return string.substring(string.lastIndexOf(".") + 1);
    }

    public static <T> Comparator<T> getDefaultComparator()
    {
        return new Comparator<T>() {
            public int compare(T o1, T o2)
            {
                String s1 = o1 == null ? "" : o1.toString();
                String s2 = o2 == null ? "" : o2.toString();
                return s1.compareTo(s2);
            }
        };
    }

    public static Float getFloat(ResultSet rs, int n)
        throws SQLException
    {
        return rs.getString(n) == null ? null : new Float(rs.getString(n));
    }

    /**
     * Same as getIndexToOrderedValues except that this assumes that the map's values implement the
     * Comparable interface and compares values using Comparable.compareTo
     * 
     * @param map a map with String keys and Comparable values
     * @return a list of keys
     */
    public static <K, V extends Comparable<V>> List<K> getIndexToOrderedComparableValues(final Map<K, V> map)
    {
        List<K> index = new ArrayList<K>();
        index.addAll(map.keySet());
        Collections.sort(index, new Comparator<K>() {
            public int compare(K key1, K key2)
            {
                V value1 = map.get(key1);
                V value2 = map.get(key2);
                return value1.compareTo(value2);
            }
        });
        return index;
    }

    /**
     * Same as getIndexToOrderedComparableValues except that this compares values using the civen
     * Comparator implementation.
     * 
     * @param map a map with String keys and Comparable values
     * @param comparator a Comparator implementation that is used for comparing objects
     * @return a sorted list of keys
     */
    public static <K, V> List<K> getIndexToOrderedComparableValues(final Map<K, V> map, final Comparator<V> comparator)
    {
        List<K> index = new ArrayList<K>();
        index.addAll(map.keySet());
        Collections.sort(index, new Comparator<K>() {
            public int compare(K key1, K key2)
            {
                V value1 = map.get(key1);
                V value2 = map.get(key2);
                return comparator.compare(value1, value2);
            }
        });
        return index;
    }

    public static <K, V> List<K> getIndexToOrderedComparableValuesUsingKeys(Map<K, V> map,
        final Comparator<K> comparator)
    {
        List<K> index = new ArrayList<K>();
        index.addAll(map.keySet());
        Collections.sort(index, comparator);
        return index;
    }

    /**
     * Get a list of a map's keys sorted in such a way that when the map's values are retrieved in
     * linear order of the keys in the list, the returned values are in sorted order. Values are
     * compared to each other by taking value.toString() and making canonical string comparison.
     * 
     * @param map a map with String keys and values for which toString() is applicable
     * @return a list of keys
     */
    public static <K, V> List<K> getIndexToOrderedValues(Map<K, V> map)
    {
        final Map<K, V> _map = map;
        List<K> index = new ArrayList<K>();
        index.addAll(map.keySet());
        Collections.sort(index, new Comparator<K>() {
            public int compare(K o1, K o2)
            {
                String s1 = _map.get(o1).toString();
                String s2 = _map.get(o2).toString();
                return s1.compareToIgnoreCase(s2);
            }
        });
        return index;
    }

    public static Integer getInteger(ResultSet rs, int n)
        throws SQLException
    {
        return rs.getString(n) == null ? null : new Integer(rs.getString(n));
    }

    /**
     * Return a Long corresponding to a String, null if the String is not a valid Long
     * 
     * @param string
     * @return
     */
    public static Long getLong(String string)
    {
        try
        {
            return Long.valueOf(string);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    /**
     * Return a Double corresponding to a String, null if the String is not a valid Double
     * 
     * @param string
     * @return
     */
    public static Double getDouble(String string)
    {
        try
        {
            return Double.valueOf(string);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    /**
     * Return a BigDecimal corresponding to a String, null if the String is not a valid BigDecimal
     * 
     * @param string
     * @return
     */
    public static BigDecimal getBigDecimal(String string)
    {
        try
        {
            return new BigDecimal(string);
        }
        catch (NumberFormatException nfe)
        {
            return null;
        }
    }

    public static String getMessageAndStackTraceAsString(Throwable aThrowable)
    {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Returns the the classname without the package name, mainly used for beautifying log messages.
     * 
     * @param clazz DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String getPlainClassname(Class<?> clazz)
    {
        String name = clazz.getName();

        return name.substring(name.lastIndexOf('.') + 1);
    }

    private static String getPropertyName(String propertyString)
    {
        int equalsSignIndex = propertyString.indexOf('=');

        if (equalsSignIndex < 0)
            return propertyString;

        if (equalsSignIndex == 0)
            return "";

        return propertyString.substring(0, equalsSignIndex);
    }

    private static String getPropertyValue(String propertyString)
    {
        int equalsSignIndex = propertyString.indexOf('=');

        if (equalsSignIndex < 0)
            return "";

        if (equalsSignIndex == 0)
            return propertyString.substring(1);

        return propertyString.substring(equalsSignIndex + 1);
    }

    /**
     * Get a reverse comparator
     * 
     * @param comparatorToReverse the comparator to reverse
     * @return the new comparator
     */
    public static <T> Comparator<T> getReverseComparator(final Comparator<T> comparatorToReverse)
    {
        return new Comparator<T>() {
            public int compare(T o1, T o2)
            {
                return -1 * comparatorToReverse.compare(o1, o2);
            }
        };
    }

    /**
     * This method will sort the Map in Ascending order of its Values.It will do this by using
     * LinkedHashMap
     * 
     * @param Map map return Map(LinkedHashMap)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map getSortedMapOnValues(Map map, final boolean ascending)
    {
        log.debug("Sorting of the Map started");
        List<Entry> list = new ArrayList<Entry>(map.entrySet());
        Collections.sort(list, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2)
            {
                Comparable value1 = (Comparable) o1.getValue();
                Comparable value2 = (Comparable) o2.getValue();
                return (ascending ? value1.compareTo(value2) : value2.compareTo(value1));
            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Entry entry : list)
        {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if ((key instanceof String) && (value instanceof String))
            {
                key = Util.escape((String) key);
                value = Util.escape((String) value);
            }

            sortedMap.put(key, value);
        }

        log.debug("Sorting of the Map done");
        return sortedMap;
    }

    /**
     * Locates the index of a given element in the given array, or -1 if the element is not found
     * from the array.
     */
    public static int indexOfIgnoreCase(String needle, String[] haystack)
    {
        for (int i = 0; i < haystack.length; i++)
        {
            String x = haystack[i];
            if (x == null && needle == null)
            {
                return i;
            }
            else if (x == null && needle != null)
            {
                continue;
            }
            else if (needle == null)
            {
                continue;
            }
            if (needle.toLowerCase().equals(haystack[i].toLowerCase()))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns whether a given date is greater than tomorrow (today midnight or later)
     * 
     * @param date the date to inspect
     * @return true if it is greater than tomorrow, false if earlier
     */
    public static boolean isGreaterThanTomorrow(Date date)
    {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 48);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        long aDateMs = date.getTime();
        long midnightMs = midnight.getTime().getTime();

        return aDateMs >= midnightMs;
    }

    /**
     * @param collection a collection
     * @return true if the collection is neither null nor empty, otherwise false
     */
    public static boolean isNeitherNullNorEmpty(Collection<?> collection)
    {
        return !isNullOrEmpty(collection);
    }

    /**
     * @param map a map
     * @return true if the map is neither null nor empty, otherwise false
     */
    public static boolean isNeitherNullNorEmpty(Map<?, ?> map)
    {
        return !isNullOrEmpty(map);
    }

    /**
     * @param array an array
     * @return true if the array is neither null nor has length of 0, otherwise false
     */
    public static boolean isNeitherNullNorEmpty(Object[] array)
    {
        return !isNullOrEmpty(array);
    }

    /**
     * @param string a string
     * @return true if the string is not null and has non-whitespace length at least one, else false
     */
    public static boolean isNeitherNullNorEmpty(String string)
    {
        return string != null && string.trim().length() > 0;
    }

    /**
     * @param collection a collection
     * @return true if the collection is null or empty, otherwise false
     */
    public static boolean isNullOrEmpty(Collection<?> collection)
    {
        return (collection == null || collection.isEmpty());
    }

    /**
     * @param map a map
     * @return true if the map is null or empty, otherwise false
     */
    public static boolean isNullOrEmpty(Map<?, ?> map)
    {
        return (map == null || map.isEmpty());
    }

    /**
     * @param array an array of objects
     * @return true if the array is null or has length of 0, otherwise false
     */
    public static boolean isNullOrEmpty(Object[] array)
    {
        return (array == null || array.length == 0);
    }

    /**
     * If - the given String is null - or equal to the empty String - or contains only spaces
     * returns true.
     * 
     * If the String has non-space content, returns false.
     * 
     * Useful for parameter checking in requests and elsewhere.
     * 
     * @param String
     * @return boolean
     * @author Jonathan Brown
     */
    public static boolean isNullOrEmpty(String target)
    {
        if (target == null)
        {
            return true;
        }
        if ("".equals(target.trim()))
        {
            return true;
        }
        return false;
    }

    /**
     * Returns whether a given date is tomorrow (today midnight or later)
     * 
     * @param date the date to inspect
     * @return true if it is at least midnight today (24:00:00.000) or later, false if earlier
     */
    public static boolean isTomorrowOrLater(Date date)
    {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 24);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        long aDateMs = date.getTime();
        long midnightMs = midnight.getTime().getTime();

        return aDateMs >= midnightMs;
    }

    /**
     * Returns whether a given date is day after tomorrow (tomorrow midnight or later)
     * 
     * @param date the date to inspect
     * @return true if it is at least midnight tomorrow (24:00:00.000) or later, false if earlier
     */
    public static boolean isDayAfterTomorrowOrLater(Date date)
    {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 24);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DATE, 1);

        long aDateMs = date.getTime();
        long midnightMs = midnight.getTime().getTime();

        return aDateMs >= midnightMs;
    }

    /**
     * Returns whether a given date is more than one week back
     * 
     * @param date the date to inspect
     * @return true if it is not more than one week back, false if earlier
     */
    public static boolean isMoreThanOneWeekBack(Date date)
    {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 24);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DATE, -9);

        long aDateMs = date.getTime();
        long midnightMs = midnight.getTime().getTime();

        return aDateMs <= midnightMs;
    }

    /**
     * Checks if a String is a valid Long (i.e. no funny characters, only numeric characters)
     * 
     * @param string
     * @return
     */
    public static boolean isValidLong(String string)
    {
        Long aLong = getLong(string);
        return aLong != null;
    }

    /**
     * Checks if a String is a valid Long (i.e. no funny characters, only numeric characters)
     * 
     * @param string
     * @return
     */
    public static boolean isValidDouble(String cost)
    {
        if (cost.indexOf(".") < 0)
            cost = cost + ".00";
        Double aDouble = getDouble(cost);
        return aDouble != null;
    }

    /**
     * Checks if a String is a valid BigDecimal (i.e. no funny characters, only numeric characters)
     * 
     * @param string
     * @return
     */
    public static boolean isValidBigDecimal(String cost)
    {
        if (cost.indexOf(".") < 0)
            cost = cost + ".00";
        BigDecimal aBigDecimal = getBigDecimal(cost);
        return aBigDecimal != null;
    }

    /**
     * Left-pad the number with zeros to the requested length.
     * 
     * @param number the number to left-pad
     * @param length the end-length to pad up to
     * @return
     */
    public static String leftPad(Long number, int length)
    {
        if (number == null)
        {
            return null;
        }
        String string = String.valueOf(number);
        int diff = length - string.length();
        if (diff > 0)
        {
            String padding = "";
            for (int i = 0; i < diff; i++)
            {
                padding += "0";
            }
            string = padding + string;
        }
        return string;
    }

    private static String loadConvert(String unicodeString)
    {
        char aChar;
        int len = unicodeString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;)
        {
            aChar = unicodeString.charAt(x++);

            if (aChar == '\\')
            {
                aChar = unicodeString.charAt(x++);

                if (aChar == 'u')
                {
                    int value = 0;

                    for (int i = 0; i < 4; i++)
                    {
                        aChar = unicodeString.charAt(x++);

                        switch (aChar) {
                            case '0' :
                            case '1' :
                            case '2' :
                            case '3' :
                            case '4' :
                            case '5' :
                            case '6' :
                            case '7' :
                            case '8' :
                            case '9' :
                                value = ((value << 4) + aChar) - '0';

                                break;

                            case 'a' :
                            case 'b' :
                            case 'c' :
                            case 'd' :
                            case 'e' :
                            case 'f' :
                                value = ((value << 4) + 10 + aChar) - 'a';

                                break;

                            case 'A' :
                            case 'B' :
                            case 'C' :
                            case 'D' :
                            case 'E' :
                            case 'F' :
                                value = ((value << 4) + 10 + aChar) - 'A';

                                break;

                            default :
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }

                    outBuffer.append((char) value);
                }
                else
                {
                    if (aChar == 't')
                    {
                        aChar = '\t';
                    }
                    else if (aChar == 'r')
                    {
                        aChar = '\r';
                    }
                    else if (aChar == 'n')
                    {
                        aChar = '\n';
                    }
                    else if (aChar == 'f')
                    {
                        aChar = '\f';
                    }

                    outBuffer.append(aChar);
                }
            }
            else
            {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }

    /**
     * Creates a comma-separated-list (each entry enclosed in single-quotes) from a Collection of
     * Strings. A null element will be discarded. An empty element will be included.
     * 
     * @param strings
     * @return
     */
    public static String makeQuotedCommaSeparatedList(Collection<?> strings)
    {
        StringBuffer result = new StringBuffer("");
        if (strings != null)
        {
            Iterator<?> iter = strings.iterator();
            while (iter.hasNext())
            {
                Object next = iter.next();
                if (next != null)
                {
                    result.append("'" + next.toString() + "'");
                    if (iter.hasNext())
                    {
                        result.append(", ");
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * This method converts a String into an HTML-compatible String that has no spaces, only
     * non-breaking spaces, in other words the HTML entity '&nbsp;'.
     * 
     * @param String s The string to process.
     * 
     * @return String The same string, with spaces replaced with nbsp.
     * 
     * @author Jonathan Brown
     */
    public static String makeSpacesNonBreakingSpaces(String original)
    {
        Character space = new Character(' ');
        Character c;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < original.length(); i++)
        {
            c = new Character(original.charAt(i));
            if (c.equals(space))
            {
                sb.append("&nbsp;");
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String maskFloat(String number)
    {
        if (Util.isNullOrEmpty(number))
        {
            return number;
        }

        String formattedNumber = null;
        DecimalFormat decimalFormat = new DecimalFormat("####.00", new DecimalFormatSymbols(Locale.US));
        try
        {
            Float aNumber = Float.valueOf(number.replace(',', '.'));
            if (!aNumber.isInfinite())
            {
                formattedNumber = decimalFormat.format(aNumber.floatValue());
            }
        }
        catch (NumberFormatException ex)
        {
            log.debug("Not able to mask float value: " + number);
        }

        return formattedNumber;
    }

    /**
     * @return null if a given string is blank, otherwise return the string
     */
    public static String nullIfEmpty(String string)
    {
        if (string != null && string.trim().equals(""))
        {
            return null;
        }
        return string;
    }

    public static String pad(String str, int maxLength)
    {
        if (str == null)
        {
            str = "";
        }

        String whiteSpace = "";
        for (int i = str.length(); i < maxLength; i++)
        {
            whiteSpace += " ";
        }
        return whiteSpace + str;
    }

    /**
     * Split a string of comma-separated substrings into a list of those substrings. For example,
     * "foo,bar,buz" will give you a list { "foo", "buz", "bar" }, and " ,foo ,, bar," will give you
     * { " ", "foo ", "", " bar", "" }.
     * 
     * @param the string to parse
     * @return a List of strings that were separated by commas
     */
    public static List<String> parseCommaSeparatedList(String string)
    {
        if (string == null)
        {
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(string, ",");
        while (st.hasMoreTokens())
        {
            list.add(st.nextToken());
        }
        return list;
    }

    public static Float parseFloat(String string)
    {
        Float obj = null;
        try
        {
            obj = (string != null ? new Float(string) : null);
        }
        catch (NumberFormatException nfe)
        {
            log.debug("Not able to parse string to float: " + string);
        }
        return obj;
    }

    public static int parseInt(String string)
    {
        if (string == null)
        {
            return -1;
        }
        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    public static int parseInt(String string, int defaultValue)
    {
        if (string == null)
        {
            return defaultValue;
        }
        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public static Integer parseInteger(String string)
    {
        final boolean acceptEmptyString = false;
        return parseInteger(string, acceptEmptyString);
    }

    public static Integer parseInteger(String string, boolean acceptEmptyString)
    {
        if (acceptEmptyString && "".equals(string))
        {
            return new Integer(0);
        }

        Integer integer = null;
        try
        {
            integer = (string != null ? new Integer(string) : null);
        }
        catch (NumberFormatException nfe)
        {
            log.debug("Not able to parse string to integer: " + string);
        }
        return integer;
    }

    public static Integer parseInteger(String value, int fallback)
    {
        return new Integer(parseInt(value, fallback));
    }

    public static Long parseLong(String string)
    {
        return parseLong(string, false);
    }

    public static Long parseLong(String string, boolean acceptEmptyString)
    {
        if (acceptEmptyString && "".equals(string))
        {
            return new Long(0);
        }

        Long value = null;
        try
        {
            value = (string != null ? new Long(string) : null);
        }
        catch (NumberFormatException nfe)
        {
            log.debug("Not able to parse string to long: " + string);
        }
        return value;
    }

    /**
     * Convert a string of the form <code>foo=bar;ho=hum<code> into a Properties object with
     * properties { foo => bar, ho => hum }.
     * 
     * @param The string to parse
     * @return the parsed Properties
     */
    public static Properties parseProperties(String string)
    {
        Properties properties = new Properties();
        if (string == null)
        {
            return properties;
        }
        StringTokenizer st = new StringTokenizer(string, ";");
        while (st.hasMoreTokens())
        {
            String token = st.nextToken();
            if (token.trim().length() == 0)
            {
                continue;
            }

            String key = getPropertyName(token);
            String value = getPropertyValue(token);
            properties.put(key, value);
        }
        return properties;
    }

    public static <T> T rangeSafeGet(List<T> result, int i)
    {
        if (result == null || i < 0 || i >= result.size())
        {
            return null;
        }
        return result.get(i);
    }

    /**
     * Return a Map containing only the non-null/non-empty values of a given Map (where values are
     * String)
     * 
     * @param inputMap
     * @return
     */
    public static <K> Map<K, String> removeEmptyValuesFromStringMap(Map<K, String> inputMap)
    {
        Map<K, String> result = new HashMap<K, String>();
        for (K key : inputMap.keySet())
        {
            String value = inputMap.get(key);
            if (value != null && value.trim().length() > 0)
            {
                result.put(key, value);
            }
        }
        return result;
    }

    /**
     * Add an escape char \ in front of the illegal characters also returns an empty string if the
     * string is empty
     */
    public static String removeIllegalChar(String str)
    {
        if (str == null || str.length() == 0)
            return "";

        if (str.indexOf('\'') > -1)
            str = str.replaceAll("'", "\\\\'");

        if (str.indexOf('\"') > -1)
            str = str.replaceAll("\"", "\\\\\"");

        return str;
    }

    /**
     * Checks if a String is a valid Long. If yes, returns the string with any leading zeroes
     * stripped out. If not, returns the input string "as is".
     * 
     * @param string
     * @return
     */
    public static String removeLeadingZeroesIfStringIsValidLong(String string)
    {
        if (isValidLong(string))
            return parseLong(string).toString();

        return string;
    }

    /**
     * Removes all line breaks \r and \n that the browser inserts into text in a textarea.
     * 
     * @param str
     * @return
     */
    public static String removeLineBrake(String str)
    {
        if (str == null)
        {
            return null;
        }

        StringBuffer formattedString = new StringBuffer(str);

        int inserted = 0;

        for (int i = 0; i < str.length(); i++)
        {
            Character c = new Character(str.charAt(i));

            Character ca = new Character('\n');
            Character cb = new Character('\r');

            if (c.compareTo(ca) == 0)
            {
                formattedString.deleteCharAt(i + inserted);
                inserted--;
            }

            if (c.compareTo(cb) == 0)
            {
                formattedString.deleteCharAt(i + inserted);
                inserted--;
            }
        }

        return formattedString.toString();
    }

    /**
     * Replace asterisks with percentage signs
     * 
     * @param string a string
     * @return parsed string
     */
    public static String replaceAsteriskWithPercentage(String string)
    {
        if (string == null)
        {
            return string;
        }

        StringBuffer result = new StringBuffer(string.length());

        StringTokenizer st = new StringTokenizer(string, "*", true);
        while (st.hasMoreTokens())
        {
            String token = st.nextToken();
            if (token.equals("*"))
            {
                result.append("%");
            }
            else
            {
                result.append(token);
            }
        }
        return result.toString();
    }

    public static <T> Comparator<T> reverse(Comparator<T> comp)
    {
        return Collections.reverseOrder(comp);
    }

    /**
     * Split a string by a separator.
     * 
     * @param string the string to split
     * @param token the separator token
     * @return a List of strings
     */
    public static List<String> split(String string, String token)
    {
        List<String> list = new ArrayList<String>();
        if (string != null)
        {
            StringTokenizer st = new StringTokenizer(string, token);
            while (st.hasMoreTokens())
            {
                list.add(st.nextToken());
            }
        }
        return list;
    }

    /**
     * Strip the last character from a string
     * 
     * @param string
     * @return the string minus its last character
     */
    public static String stripLastCharacter(String string)
    {
        if (string == null)
        {
            return null;
        }
        if (string.length() > 0)
        {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

    /**
     * Trim a string safely, ie. <code>null</code> is accepted and returned.
     * 
     * @param string the string to trim
     * @return the trimmed string or <code>null</code> if the given string was also
     *         <code>null</code>
     */
    public static String trim(String string)
    {
        return string == null ? null : string.trim();
    }

    /**
     * This method is for cutting string to lenght 32 characters and replace these chars: - , . : ;
     * 
     * @param chain
     * @return cutted and char replaced string
     */
    public static String trimString(String chain)
    {
        chain = chain.replace('-', '_');
        chain = chain.replace(',', '_');
        chain = chain.replace(';', '_');
        chain = chain.trim();
        chain = chain.replace(' ', '_');

        String chain2 = "";
        boolean replaceNext = true;
        while (replaceNext)
        {
            chain2 = chain.replaceAll("__", "_");
            if (chain2.equals(chain))
                replaceNext = false;
            else
                chain = chain2;
        }

        if (chain.length() > 32)
        {
            chain = chain.substring(0, 32);
        }

        int b = chain.length();

        if ("_".equals(chain.substring(b - 1, b)))
            return chain.substring(0, b - 1);

        return chain;
    }

    public static List<Long> convertStringsToLongs(List<String> listOfStrings)
    {
        List<Long> resultList = new ArrayList<Long>();
        if (Util.isNullOrEmpty(listOfStrings))
            return resultList;

        for (String string : listOfStrings)
            resultList.add(parseLong(string));

        return resultList;
    }

    /**
     * Converts list of Long values to list of String values.
     * 
     * @param listOfLongs
     * @return
     */
    public static List<String> convertLongsToStrings(List<Long> listOfLongs)
    {
        List<String> resultList = new ArrayList<String>();
        if (Util.isNullOrEmpty(listOfLongs))
            return resultList;

        for (Long longValue : listOfLongs)
            resultList.add(longValue.toString());

        return resultList;
    }

    public static boolean isTrue(Boolean value)
    {
        return value != null && value;
    }

    /**
     * This method should be used when one knows for 100% that input parameter is not null
     * 
     * @return True if given string is null else returns false
     */

    public static boolean isEmpty(String str)
    {
        try
        {
            return (str.trim().equals(""));
        }
        catch (Exception ex)
        {
            log.error("The input string is null wrong usage of isEmpty method ");
        }
        return true;// Though it is null, it will return true

    }

    /**
     * This method checks if a string array in neither null nor empty
     * 
     * @param string
     * @return {@link Boolean}
     */
    public static boolean arrayContainsAtLeastOneNonWhitespaceString(String[] array)
    {
        if (array != null)
        {
            for (String string : array)
            {
                if (isNeitherNullNorEmpty(string))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method will checks if a List<String> is having null or empty values. If such values
     * exist then the method will refine those values and return a null or empty value free list.
     * 
     * example. If the list values provided may be [null,"","A",null,null,null] this method will
     * return a list as ["A"]
     * 
     * @param newlist
     * @return List<String>
     */
    public static List<String> removeNullFromList(List<String> list)
    {
        if (Util.isNeitherNullNorEmpty(list))
        {
            for (int i = 0; i < list.size(); i++)
            {
                if (Util.isNullOrEmpty(list.get(i)))
                {
                    list.remove(i);
                    i--;
                }
            }
        }
        return list;
    }

    /**
     * This method is used to validate the telephone number for international standard numbers i.e
     * "+" followed by 20 numbers
     * 
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber)
    {
        return isNeitherNullNorEmpty(phoneNumber) && PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * This method will return the first N characters from a string where N = length param
     * 
     * @param string
     * @param length
     * @return String
     */
    public static String getFirstNCharactersFromString(String string, int length)
    {
        if (isNeitherNullNorEmpty(string) && string.length() > length)
        {
            return string.substring(0, length);
        }
        else
        {
            return emptyIfNull(string);
        }
    }

    /**
     * The method will merge two part of date e.g. date and time and create date string as per
     * financial period format if date string is null the method will return null, if date is not
     * null but time is null then "00:00" will be appended after date string
     * 
     * @param date
     * @param time
     * @return String
     */
    public static String mergeDateAndTime(String date, String time)
    {
        if (Util.isNullOrEmpty(date) || Util.isNullOrEmpty(time))
        {
            return null;
        }

        return date + " " + time;
    }

    /**
     * Return false if null parameter is given, otherwise return Boolean.booleanValue().
     * 
     * @param booleanObject
     * @return booleanObject.booleanValue() if parameter wasn't null, otherwise false
     */
    public static boolean falseIfNull(Boolean booleanObject)
    {
        if (booleanObject == null)
            return false;
        return booleanObject.booleanValue();
    }

    /**
     * This method will convert Long to BigInteger
     * 
     * @param lngValue
     * @return
     */
    public static BigInteger convertLongToBigInteger(Long lngValue)
    {
        BigInteger bigInt = null;

        if (lngValue != null)
        {
            bigInt = BigInteger.valueOf((Long) lngValue);
        }

        return bigInt;
    }

    /**
     * @author maria.p.chinnappan
     * 
     *         Returns the Search Pattern from the GenericSearchCriteria.
     * 
     * @param GenericSearchCriteria
     * @return SearchPattern - String.
     */
   /* public static String getSearchPattern(GenericSearchCriteria criteria)
    {
        if (StringUtils.hasText(criteria.getSearchString()))
        {
            return "'%" + criteria.getSearchString().toLowerCase().replace('*', '%') + "%'";
        }
        else
        {
            return "'%'";
        }
    }*/

    public static String appendZeros(Long number, String type)
    {
        String numberString = "";
        if (String.valueOf(number).length() == 1)
        {
            numberString = type.concat("0000".concat(String.valueOf(number)));
        }
        else if (String.valueOf(number).length() == 2)
        {
            numberString = type.concat("000".concat(String.valueOf(number)));
        }
        else if (String.valueOf(number).length() == 3)
        {
            numberString = type.concat("00".concat(String.valueOf(number)));
        }
        else if (String.valueOf(number).length() == 4)
        {
            numberString = type.concat("0".concat(String.valueOf(number)));
        }
        else
        {
            numberString = type.concat(String.valueOf(number));
        }
        return numberString;

    }

    public static String getIncreasedNumber(String releaseNoteNumber, String releaseNoteType)
    {
        Long rnNumber = getLong(releaseNoteNumber.substring(2, 7)) + 1;
        return appendZeros(rnNumber, releaseNoteType);
    }

    public static String generateRandomPassword()
    {
        String password = "";
        for (int i = 0; i < PASSWORD_LENGTH; i++)
        {
            int index = (int) (RANDOM.nextDouble() * PASS_CHARS.length());
            password += PASS_CHARS.substring(index, index + 1);
        }
        return password;
    }
    
    public static void main(String[] args) {
    	System.out.println("Test");
	}
}


