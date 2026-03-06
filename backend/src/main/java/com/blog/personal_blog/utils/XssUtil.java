package com.blog.personal_blog.utils;

import org.springframework.web.util.HtmlUtils;

public class XssUtil {

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return HtmlUtils.htmlEscape(input);
    }

    public static String sanitizeRichText(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replaceAll("<script[^>]*>.*?</script>", "")
                .replaceAll("on\\w+\\s*=", "x-on-")
                .replaceAll("javascript\\s*:", "x-javascript:");
    }
}
