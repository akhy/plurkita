package net.akhyar.plurkita;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author akhyar
 */
public class PlurkUtils {
    final static DateTimeFormatter PLURK = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss ZZZZ");

    public static String getAvatarUrl(long userId, int hasProfileImage, int avatar, String size) {
        String ext = "big".equalsIgnoreCase(size) ? "jpg" : "gif";
        if (hasProfileImage == 1) {
            String avatarString = avatar >= 0 ? String.valueOf(avatar) : "";
            return String.format("http://avatars.plurk.com/%d-%s%s.%s",
                    userId, size, avatarString, ext);
        } else {
            return String.format("http://www.plurk.com/static/default_%s.%s", size, ext);
        }
    }
}
