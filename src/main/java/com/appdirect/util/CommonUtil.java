package com.appdirect.util;

import java.util.Collection;

/**
 * Defines common utility methods
 */
public class CommonUtil {

    public static boolean isNull(Object object) {
        return (null == object) ? true : false;
    }

    public static boolean isEmpty(String object) {
        return (isNull(object) || object.length() == 0) ? true : false;
    }

    public static String coalesce(String one, String two) {
        return (isNull(one) || "null".equals(one)) ? two : one;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return (null == collection || collection.size() == 0) ? true : false;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

}
