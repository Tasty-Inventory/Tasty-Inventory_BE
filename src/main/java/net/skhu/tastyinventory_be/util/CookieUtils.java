//package net.skhu.tastyinventory_be.util;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.util.Optional;
//
//public class CookieUtils {
//    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null && cookies.length > 0) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    return Optional.of(cookie);
//                }
//            }
//        }
//        return Optional.empty();
//    }
//
//    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
//        addCookie(response, name, value, true, true, maxAge);
//    }
//
//    public static void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly, boolean secure, int maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setPath("/");
//        cookie.setHttpOnly(httpOnly);
//        cookie.setSecure(secure);
//        cookie.setMaxAge(maxAge);
//        cookie.setAttribute("SameSite", "None");
//        response.addCookie(cookie);
//    }
//
//    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length > 0) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    cookie.setValue("");
//                    cookie.setPath("/");
//                    cookie.setMaxAge(0);
//                    response.addCookie(cookie);
//                }
//            }
//        }
//    }
//}
