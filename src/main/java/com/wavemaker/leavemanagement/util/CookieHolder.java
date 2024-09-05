package com.wavemaker.leavemanagement.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieHolder {
    public static final Map<String, List<String>> cookieMap = new ConcurrentHashMap<>();

    public static void addKey(String unqId, List<String> employeeDetails) {
        cookieMap.put(unqId, employeeDetails);
    }

    public static List<String> getEmployeeDetails(String unqId) {
        return cookieMap.get(unqId);
    }

    public static void removeCookie(String unqId) {
        cookieMap.remove(unqId);
    }
}
