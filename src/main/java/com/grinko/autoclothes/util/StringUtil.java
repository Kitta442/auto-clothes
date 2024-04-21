package com.grinko.autoclothes.util;

import lombok.extern.slf4j.Slf4j;

import static com.grinko.autoclothes.util.LangUtil.nvl;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public final class StringUtil {

    private StringUtil() {
    }

    public static String obj2str(final Object i) {
        return nvl(i, String::valueOf);
    }

    /**
     * Converts string to integer
     *
     * @param s - string value
     * @return integer value
     */
    public static Integer str2int(final String s) {

        final String str = defaultString(s).trim();
        if (!isBlank(str) && isSignedNumeric(str)) {
            return Integer.valueOf(str);
        } else if (!isBlank(str) && !isSignedNumeric(str)) {
            throw new NumberFormatException("String '" + str + "' contains not numeric chars");
        }
        return null;
    }

    /**
     * Converts string to integer
     *
     * @param str - string value
     * @return integer value
     */
    public static Long str2long(final String str) {

        if (!isBlank(str) && isSignedNumeric(str)) {
            return Long.valueOf(str);
        } else if (!isBlank(str) && !isSignedNumeric(str)) {
            throw new NumberFormatException("String '" + str + "' contains not numeric chars");
        }
        return null;
    }

    static boolean isSignedNumeric(final CharSequence cs) {

        if (isBlank(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            final char ch = cs.charAt(i);
            if (!(Character.isDigit(ch) || '-' == ch || '+' == ch)) {
                return false;
            }
        }
        return true;
    }

}
