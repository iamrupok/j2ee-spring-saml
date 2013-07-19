/*
 * @(#) StringUtils.java Jul 20, 2005
 * Copyright 2005 Frequency Marketing, Inc. All rights reserved.
 * Frequency Marketing, Inc. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.frequencymarketing.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Matcher;

import com.frequencymarketing.common.app.closure.ClosureChar;

/**
 * @author Edward Sumerfield
 */
public class StringUtils
{
    /**
     * The string escape character
     */
    public static final char ESCAPE_CHAR = '\\';

    public static final String[] SPECIAL_CHAR_UNIX_SAFE =
        { "/", "&", "*", "?", ":", "!", "~", ">", "<", "|", "®", ".", "™", "’",
         " ", "(", ")", "™" };
    public static final String[] SPECIAL_CHAR =
        { "/", "&", "*", "?", ":", "!", "~", ">", "<", "|", "." };
    public static final String REPLACE_CHAR = "_";

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

    /**
     * Sequence through the string passing each character to the closure.
     * @param a_text
     * @param a_closure
     */
    public static void each(String a_text, ClosureChar a_closure)
    {
        if (null != a_text)
        {
            int textLength = a_text.length();
            for (int index = 0; index < textLength; index++)
            {
                a_closure.call(a_text.charAt(index));
            }
        }
    }

    /**
     * Truncate a string at a word boundary
     * @param a_text the text to be truncated
     * @param a_length the maximum number of characters to remain in the string.
     * @return the truncated string
     */
    public static String truncAtWord(String a_text, int a_length)
    {
        String text = a_text;
        if (null != a_text && text.length() > a_length)
        {
            /*
             * Search backwards for a word boundary a maximum of 20 characters.
             */
            int backEnd = 20;
            if (backEnd > a_length)
            {
                backEnd = a_length - 1;
            }

            int finalLength = a_length;
            char character;
            for (int backTrip = 1; backTrip <= backEnd
                                   && a_length - backTrip > 0; backTrip++)
            {
                character = text.charAt(a_length - backTrip);
                if (Character.isWhitespace(character))
                {
                    finalLength = a_length - backTrip;
                    break;
                }
            }

            if (finalLength > 0)
            {
                text = text.substring(0, finalLength);
            }
        }
        return text;
    }

    /**
     * Splits a line on a delimiter
     * @param a_text the text string to be split
     * @param a_delimiter the delimiter to split on.
     * @return an array of strings. This alternate implementation had some
     *         problems: Perl5Util util = new Perl5Util(); List partList = new
     *         ArrayList(); util.split(partList, "m/" + a_delimiter + "/",
     *         a_text); return (String[])partList.toArray(new String[0]);
     */
    public static String[] split(String a_text, String a_delimiter)
    {
        return split(a_text, new String[]
            { a_delimiter });
    }

    /**
     * @param a_text
     * @param a_delimiters
     * @return an array of strings.
     */
    public static String[] split(String a_text, String[] a_delimiters)
    {
        List partList = splitToList(a_text, a_delimiters);
        return (String[])partList.toArray(new String[0]);
    }

    /**
     * Splits a line on a delimiter, removing any escape characters.
     * @param a_text the text string to be split
     * @param a_delimiter the delimiter to split on.
     * @return a list of strings.
     */
    public static List splitToList(String a_text, String a_delimiter)
    {
        return splitToList(a_text, new String[]
            { a_delimiter });
    }

    /**
     * Split a string into segments delimited by any of the delimiters
     * specified in the a_delimiters array.
     * @param a_text
     * @param a_delimiters
     * @return a list of the strings found between each of the delimiters.
     */
    public static List splitToList(String a_text, String[] a_delimiters)
    {
        List partList = new ArrayList();
        if (null != a_text)
        {
            String part;
            int partStart = 0;
            int searchPos;
            int index = 0;
            while (index != -1)
            {
                int delimLength = -1;

                /*
                 * Find the next delimiter being sure to skip over any escaped
                 * delimiters that may exist.
                 */
                boolean foundDelimOrAtEnd = false;
                searchPos = partStart;
                while (!foundDelimOrAtEnd)
                {
                    int nextDelimiter;
                    int currentClosestDelimiter = Integer.MAX_VALUE;
                    for (int delimIndex = 0; delimIndex < a_delimiters.length; delimIndex++)
                    {
                        String delimiter = a_delimiters[delimIndex];
                        nextDelimiter = a_text.indexOf(delimiter, searchPos);
                        if (nextDelimiter != -1
                            && nextDelimiter < currentClosestDelimiter)
                        {
                            currentClosestDelimiter = nextDelimiter;
                            delimLength = delimiter.length();
                        }
                    }

                    index = -1;
                    if (currentClosestDelimiter != Integer.MAX_VALUE)
                    {
                        index = currentClosestDelimiter;
                    }

                    if (index > 0)
                    {
                        if (a_text.charAt(index - 1) == ESCAPE_CHAR)
                        {
                            searchPos = index + delimLength;
                            index = -1;
                        }
                        else
                        {
                            foundDelimOrAtEnd = true;
                        }
                    }
                    else
                    {
                        foundDelimOrAtEnd = true;
                    }
                }

                if (index == -1)
                {
                    part = a_text.substring(partStart);
                    partList.add(removeEscape(part));
                }
                else
                {
                    part = a_text.substring(partStart, index);
                    partList.add(removeEscape(part));

                    partStart = index + delimLength;
                }
            }
        }

        return partList;
    }

    /**
     * Split a string using the delimiter and return the last token.
     * @param a_text
     * @param a_delimiter 
     * @return the last token of the string or the whole string if the 
     * delimiter does not exist.
     */
    public static String splitLastToken(String a_text, String a_delimiter)
    {
        String text = a_text;

        if (null != a_text && null != a_delimiter && a_delimiter.length() > 0)
        {
            int lastDelimiter = a_text.lastIndexOf(a_delimiter);
            if (lastDelimiter != -1)
            {
                text = text.substring(lastDelimiter + a_delimiter.length());
            }
        }

        return text;
    }

    /**
     * Add escape characters (\) before each of the characters to escape and
     * each of the escape characters themselves.
     * @param a_unescaped
     * @return the escaped text string
     */
    public static String addEscape(String a_unescaped)
    {
        String escaped = null;

        if (null != a_unescaped)
        {
            StringBuffer text = new StringBuffer();

            char charToEscape = ',';

            char character;
            int length = a_unescaped.length();
            for (int index = 0; index < length; index++)
            {
                character = a_unescaped.charAt(index);

                if (character == charToEscape || character == ESCAPE_CHAR)
                {
                    text.append(ESCAPE_CHAR);
                }

                text.append(character);
            }

            escaped = text.toString();
        }

        return escaped;
    }

    /**
     * Remove escape characters from the string. Any escape characters that are
     * themselves escaped end up being unescaped leaving a single escape
     * characters.
     * <pre>
     *  , = ,
     *  \, = ,
     *  \\ = \
     * </pre>
     * @param a_escaped the string to work on
     * @return the string without its escape characters
     */
    public static String removeEscape(String a_escaped)
    {
        String unescaped = null;

        if (null != a_escaped)
        {
            StringBuffer text = new StringBuffer();

            boolean foundEscapeChar = false;

            char character;
            int length = a_escaped.length();
            for (int index = 0; index < length; index++)
            {
                character = a_escaped.charAt(index);

                if (character == ESCAPE_CHAR)
                {
                    if (foundEscapeChar)
                    {
                        text.append(character);
                        foundEscapeChar = false;
                    }
                    else
                    {
                        foundEscapeChar = true;
                    }
                }
                else
                {
                    text.append(character);
                    foundEscapeChar = false;
                }
            }

            unescaped = text.toString();
        }

        return unescaped;
    }

    /**
     * Replaces the first occurance of a string with another string.
     * @param a_text is a string that contains part to be replaced.
     * @param a_regex the string to replace.
     * @param a_with the new string to be inserted/
     * @return a new string that contains the replacements.
     */
    public static String replaceFirst(String a_text, String a_regex,
        String a_with)
    {
        Perl5Util util = new Perl5Util();
        return util.substitute("s/" + a_regex + "/" + a_with + "/", a_text);
    }

    /**
     * Determines if the string matches the regular expression using Apache
     * Perl5Util regular expression library.
     * 
     * @param a_text
     * @param a_regex
     * @return true if it matches and false otherwise.
     */
    public static boolean matches(String a_text, String a_regex)
    {
        Perl5Util util = new Perl5Util();
        return util.match("m/" + a_regex + "/", a_text);
    }

    /**
     * Determines if the string matches the regular expression using JDK 1.4
     * regular expression library.  It looks like the
     * Perl5Util library is not valid, but since there are already some passing
     * tests using that library then create another match function.
     * 
     * @param a_text
     * @param a_regex
     * @return true if it matches and false otherwise.
     */
    public static boolean matchesJDK14(String a_text, String a_regex)
    {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(a_regex);
        Matcher m = p.matcher(a_text);
        return m.matches();
    }

    /**
     * Determines if the string matches the regular expression and returns the
     * result group.
     * @param a_text
     * @param a_regex
     * @return the group of extracted strings
     */
    public static MatchResult parse(String a_text, String a_regex)
    {
        Perl5Util util = new Perl5Util();
        util.match("m/" + a_regex + "/", a_text);
        return util.getMatch();
    }

    /**
     * Determines if the string matches the pre-compiled regular expression and
     * returns the extraccted result groups.
     * @param a_text
     * @param a_pattern
     * @return the groups of extracted strings.
     */
    public static MatchResult parse(String a_text, Pattern a_pattern)
    {
        Perl5Matcher matcher = new Perl5Matcher();
        matcher.matches(a_text, a_pattern);
        return matcher.getMatch();
    }

    /**
     * Replace all occurances of each string in the corresponding string.
     * @param a_text where the replaces are to occur.
     * @param a_replace the array of strings to replace.
     * @param a_with the array of string to be inserted.
     * @return a new string with the replacements completed.
     */
    public static String replaceAll(String a_text, String[] a_replace,
        String[] a_with)
    {
        String text = a_text;

        if (null != a_replace && null != a_with)
        {
            if (a_replace.length != a_with.length)
            {
                throw new RuntimeException(
                        "ReplaceAll takes two arrays of equal length");
            }

            for (int index = 0; index < a_replace.length; index++)
            {
                text = replaceAll(text, a_replace[index], a_with[index]);
            }
        }

        return text;
    }

    /**
     * Replace all occurances of each string in another string.
     * @param a_text where the replaces are to occur.
     * @param a_replace the array of strings to replace.
     * @param a_with the string to be inserted.
     * @return a new string with the replacements completed.
     */
    public static String replaceAll(String a_text, String[] a_replace,
        String a_with)
    {
        String text = a_text;

        if (null != a_replace && null != a_with)
        {
            for (int index = 0; index < a_replace.length; index++)
            {
                text = replaceAll(text, a_replace[index], a_with);
            }
        }

        return text;
    }

    /**
     * Replace all occurances of a string in another string.
     * @param a_text where the replaces are to occur.
     * @param a_replace the string to replace.
     * @param a_with the string to be inserted.
     * @return a new string with the replacements completed.
     */
    public static String replaceAll(String a_text, String a_replace,
        String a_with)
    {
        String text = a_text;

        if (null != a_text && null != a_replace && null != a_with)
        {
            StringBuffer buffer = new StringBuffer();

            String part;
            int replaceLength = a_replace.length();
            int partStart = 0;
            int index = 0;
            while (index != -1)
            {
                index = a_text.indexOf(a_replace, partStart);
                if (index == -1)
                {
                    part = a_text.substring(partStart);
                    buffer.append(part);
                }
                else
                {
                    part = a_text.substring(partStart, index);
                    buffer.append(part);
                    buffer.append(a_with);

                    partStart = index + replaceLength;
                }
            }

            text = buffer.toString();
        }

        return text;
    }

    /**
     * Determine if the string is empty by checking for null, no length or just
     * a single space.
     * @param a_text
     * @return true if the string is empty and false otherwise.
     */
    public static boolean isEmpty(String a_text)
    {
        boolean isEmpty = false;

        if (null == a_text)
        {
            isEmpty = true;
        }
        else if (a_text.length() == 0)
        {
            isEmpty = true;
        }
        else if (a_text.trim().length() == 0)
        {
            isEmpty = true;
        }

        return isEmpty;
    }

    /**
     * Comma separate all the items in the list. Exclude any of the list entries
     * that are null.
     * @param a_list is a list of items to be combined contatenated into a comma
     *        separated list.
     * @return the string of comma separated items
     */
    public static String commaSeparate(List a_list)
    {
        return delimitList(a_list, 0, a_list.size(), ",");
    }

    /**
     * Separeate all items in a list defining a delimiter.
     * @param a_list list of items to be combined
     * @param a_delimiter delimiter to separate list elements
     * @return the string of delimited items
     */
    public static String delimitList(List a_list, String a_delimiter)
    {
        return delimitList(a_list, 0, a_list.size(), a_delimiter);
    }

    /**
     * Comma separate all the items in the list. Exclude any of the list entries
     * that are null.
     * @param a_list the list to be formatted
     * @param a_startIndex the index into the list to start
     * @param a_length the number of items in the list to format
     * @return the string of comman separated items
     */
    public static String commaSeparate(List a_list, int a_startIndex,
        int a_length)
    {
        return delimitList(a_list, a_startIndex, a_length, ",");
    }

    /**
     * Comma separate all the items in the list. Exclude any of the list entries
     * that are null.
     * @param a_list the list to be formatted
     * @param a_startIndex the index into the list to start
     * @param a_length the number of items in the list to format
     * @param a_delimiter defined field delimiter
     * @return the string of comman separated items
     */
    public static String delimitList(List a_list, int a_startIndex,
        int a_length, String a_delimiter)
    {
        StringBuffer text = new StringBuffer();

        Object entry;
        boolean foundFirst = false;
        int size = a_list.size();

        if (a_length < 0)
        {
            throw new IndexOutOfBoundsException("Nagitive length to copy");
        }
        else if (a_startIndex + a_length > size)
        {
            throw new IndexOutOfBoundsException("Subsection too long for copy");
        }

        for (int index = 0; index < a_length; index++)
        {
            entry = a_list.get(a_startIndex + index);
            if (null != entry)
            {
                if (foundFirst)
                {
                    text.append(a_delimiter);
                }
                text.append(entry);
                foundFirst = true;
            }
        }
        return text.toString();
    }

    /**
     * Count the number of a single character that occur in a string.
     * @param a_text the string to search
     * @param a_character the character to count.
     * @return the number of characters in the string.
     */
    public static int countChar(String a_text, char a_character)
    {
        int count = 0;

        String text = a_text;

        int pos = text.indexOf(a_character);
        while (pos != -1)
        {
            count++;

            pos = text.indexOf(a_character, pos + 1);
        }

        return count;
    }

    /**
     * Pad the string on the right to the specified length with spaces.
     * @param a_text
     * @param a_length
     * @return the string with right hand padding to the specified length.
     */
    public static String padRight(String a_text, int a_length)
    {
        String paddedText = a_text;

        if (a_text.length() < a_length)
        {
            paddedText = a_text
                         + StringUtils.repeat(' ', a_length - a_text.length());
        }

        return paddedText;
    }

    /**
     * Pad the string on the right to the specified length with spaces.
     * @param a_text
     * @param a_pad
     * @param a_length
     * @return the string with left hand padding to the specified length.
     */
    public static String padLeft(String a_text, char a_pad, int a_length)
    {
        String paddedText;

        if (a_text.length() < a_length)
        {
            paddedText = StringUtils.repeat(a_pad, a_length - a_text.length());
            paddedText += a_text;
        }
        else
        {
            paddedText = a_text;
        }

        return paddedText;
    }

    /**
     * Create a string with a single repeated character in it of a specified
     * length.
     * @param a_character
     * @param a_repeatCount
     * @return the character repeated the number of times.
     */
    public static String repeat(char a_character, int a_repeatCount)
    {
        return repeat(String.valueOf(a_character), a_repeatCount);
    }

    /**
     * @param a_text
     * @param a_repeatCount
     * @return the string repeated the number of times.
     */
    public static String repeat(String a_text, int a_repeatCount)
    {
        return repeat(a_text, "", a_repeatCount);
    }

    /**
     * Generate strings like X,X,X where 'X' is the string
     * being repeated and the comma is the delimiter.
     * @param a_text the text to be repeated.
     * @param a_delimiter the string that should appear between each repeated
     * character
     * @param a_repeatCount the number of times to repeat.
     * @return the string repeated the number of times with the delimiter
     * between each repeat.
     */
    public static String repeat(String a_text, String a_delimiter,
        int a_repeatCount)
    {
        StringBuffer text = new StringBuffer(a_repeatCount);
        for (int count = 0; count < a_repeatCount; count++)
        {
            if (count > 0)
            {
                text.append(a_delimiter);
            }
            text.append(a_text);
        }
        return text.toString();
    }

    /**
     * Create a repeated character string with a vertical bar located at the
     * beginning of a tab width. This can be used to generate output that needs
     * to corrolate lines in an indented list.
     * <pre>
     *     aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     *     | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     *     | | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     *     | | | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     *     | | | | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     *     | | | | | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     *     | | | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     * </pre>
     * @param a_character
     * @param a_maxCharacters
     * @param a_tabWidth
     * @return the nested reprentation of the list.
     */
    public static String repeatGrid(char a_character, int a_maxCharacters,
        int a_tabWidth)
    {
        StringBuffer text = new StringBuffer(a_maxCharacters);
        for (int count = 0; count < a_maxCharacters; count++)
        {
            if (count % a_tabWidth == 0)
            {
                text.append('|');
            }
            else
            {
                text.append(a_character);
            }
        }

        return text.toString();
    }

    /**
     * @param a_quotedArgs
     * @return The strings in the list joined with spaces.
     */
    public static String join(List a_quotedArgs)
    {
        StringBuffer joined = new StringBuffer();

        int quotedSize = a_quotedArgs.size();
        for (int index = 0; index < quotedSize; index++)
        {
            if (index > 0)
            {
                joined.append(' ');
            }
            joined.append(a_quotedArgs.get(index));
        }

        return joined.toString();
    }

    /**
     * Output a byte array as tabularized numbers with a specific column
     * width.
     * @param a_bytes
     */
    public static void printBytes(byte[] a_bytes)
    {
        for (int i = 0; i < a_bytes.length; i++)
        {
            System.out
                .print(StringUtils.padLeft("" + a_bytes[i], ' ', 3) + " ");
        }
        System.out.println();
    }

    /**
     * Reflect through an object and append to a string the toString values
     * of all the no arg getters. This will format a tostring output for the
     * values of the object.
     * @param a_object the instance of the object to be formatted into a string
     * @return a string the represents this object.
     */
    public static String toStringObject(Object a_object)
    {
        List properties = new ArrayList();

        Method method;
        String methodName;
        int numParameters;

        Method[] methods = a_object.getClass().getMethods();
        for (int index = 0; index < methods.length; index++)
        {
            method = methods[index];
            methodName = method.getName();
            numParameters = method.getParameterTypes().length;

            if (methodName.startsWith("get") && numParameters == 0
                && !methodName.equals("getClass"))
            {
                try
                {
                    Object object = method.invoke(a_object, EMPTY_OBJECT_ARRAY);
                    if (null != object)
                    {
                        properties.add(methodName + "=" + object.toString());
                    }
                }
                catch (IllegalArgumentException e)
                {
                    // Ignore problems with method access
                }
                catch (IllegalAccessException e)
                {
                    // Ignore problems with method access
                }
                catch (InvocationTargetException e)
                {
                    // Ignore problems with method access
                }
            }
        }

        Collections.sort(properties);

        String text = getClassName(a_object.getClass());
        text += " ";
        text += properties.toString();

        return text;
    }

    public static String getClassName(Class a_class)
    {
        return splitLastToken(a_class.getName(), ".");
    }

    /**
     * Convert the a string into a new string with the first letter uppper-cased.
     * @param a_text
     * @return the converted new string
     */
    public static String upperCaseFirst(String a_text)
    {
        String capitalCase = a_text;

        if (!isEmpty(capitalCase))
        {
            char character = Character.toUpperCase(a_text.charAt(0));
            capitalCase = character + a_text.substring(1);
        }

        return capitalCase;
    }

    /**
     * Convert the a string into a new string with the first letter lower-cased.
     * @param a_text
     * @return the converted new string
     */
    public static String lowerCaseFirst(String a_text)
    {
        String capitalCase = a_text;

        if (!isEmpty(capitalCase))
        {
            char character = Character.toLowerCase(a_text.charAt(0));
            capitalCase = character + a_text.substring(1);
        }

        return capitalCase;
    }

    /**
     * Replaces special characters.
     * @param a_text
     * @return the converted new string
     */
    public static String replaceAllSpecialChars(String a_text)
    {
        return replaceSpecialChars(a_text, REPLACE_CHAR);
    }

    public static String removeAllSpecialChars(String a_text)
    {
        return replaceSpecialChars(a_text, "");
    }

    private static String replaceSpecialChars(String a_text,
        String a_replacement)
    {
        if (a_text != null)
        {
            for (int i = 0; i < StringUtils.SPECIAL_CHAR.length; i++)
            {
                if (a_text.indexOf(SPECIAL_CHAR[i]) > -1)
                {
                    a_text = replaceAll(a_text, SPECIAL_CHAR[i], a_replacement);
                }
            }
        }
        return a_text;
    }

    /**
     * Replaces special characters.
     * @param a_text
     * @return the converted new string
     */
    public static String replaceAllSpecialCharsUNIXSafe(String a_text)
    {
        return replaceAll(a_text, StringUtils.SPECIAL_CHAR_UNIX_SAFE,
                REPLACE_CHAR);
    }

    /**
     * Replaces special characters.
     * @param a_text
     * @return the converted new string
     */
    public static String removeAllSpecialCharsUNIXSafe(String a_text)
    {
        return replaceAll(a_text, StringUtils.SPECIAL_CHAR_UNIX_SAFE, "");
    }
    
    public static String stripAllWhiteSpace(String a_text)
    {
        String stripped = null;

        if (a_text != null)
        {
            stripped = a_text.replaceAll("\\s", "");
        }

        return stripped;
    }

    /**
     * @param a_text to be encoded
     * @return the encoded string
     */
    public static String urlEncode(String a_text)
    {
        String encoded;

        try
        {
            encoded = URLEncoder.encode(a_text, "UTF8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Invalid encoding scheme", e);
        }

        return encoded;
    }

    /**
     * @param a_encodedText to be decoded
     * @return the decoded text
     */
    public static String urlDecode(String a_encodedText)
    {
        String decoded = null;

        if (a_encodedText != null)
        {
            try
            {
                decoded = URLDecoder.decode(a_encodedText, "UTF8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException("Invalid encoding scheme", e);
            }
        }

        return decoded;
    }

    /**
     * @param a_text
     * @param a_subString
     * @return true if the substring exists in the text and false otherwise.
     */
    public static boolean contains(String a_text, String a_subString)
    {
        boolean contains = false;

        if (null != a_text && a_subString != null)
        {
            contains = a_text.indexOf(a_subString) != -1;
        }

        return contains;
    }

    public static void println(String[] a_parts)
    {
        System.out.print(">");
        for (int index = 0; index < a_parts.length; index++)
        {
            System.out.print(a_parts[index]);
            if (index + 1 < a_parts.length)
            {
                System.out.print("|");
            }
        }
        System.out.println("<");
    }

    /**
     * Insert a string at a specified index within another string
     * @param a_destination
     * @param a_insert
     * @param a_index
     */
    public static String insert(String a_destination, String a_insert,
        int a_index)
    {
        String firstToken = "";
        String secondToken = "";

        if (!isEmpty(a_destination))
        {
            firstToken = a_destination.substring(0, a_index);
            secondToken = a_destination.substring(a_index);
        }

        StringBuffer finalValue = new StringBuffer();
        finalValue.append(firstToken);
        if (!isEmpty(a_insert))
        {
            finalValue.append(a_insert);
        }
        finalValue.append(secondToken);

        return finalValue.toString();
    }

    /**
     * Create a list of strings given a comma delimited string
     * @param a_value
     * @return list of values
     */
    public static List toList(String a_value)
    {
        List valueList = null;
        if (null != a_value)
        {
            StringTokenizer tokenizer = new StringTokenizer(a_value, ",");

            valueList = new ArrayList();
            while (tokenizer.hasMoreTokens())
            {
                valueList.add(tokenizer.nextToken());
            }
        }

        return valueList;
    }
    
    public static byte[] compressString(String inputString)
    {
        byte[] input = inputString.getBytes();
        
        // Compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        
        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();
        
        // Create an expandable byte array to hold the compressed data.
        // It is not necessary that the compressed data will be smaller than
        // the uncompressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        
        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) 
        {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Get the compressed data
        byte[] compressedData = bos.toByteArray();
        return compressedData;
    }
    
    public static String decompressString(byte[] compressedData)
    {
//      Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData);
        
        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
        
        // Decompress the data
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
            }
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Get the decompressed data
        byte[] decompressedData = bos.toByteArray();
        return new String(decompressedData);
    }
    
}
