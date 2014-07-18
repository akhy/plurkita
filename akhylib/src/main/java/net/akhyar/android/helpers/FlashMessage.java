package net.akhyar.android.helpers;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author akhyar
 */
public class FlashMessage {

    public static final int INFO = 0;
    public static final int ALERT = 1;
    public static Queue<FlashMessage> list = new LinkedList<FlashMessage>();
    private String message;
    private int type;

    public FlashMessage(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public FlashMessage() {
    }

    public static void add(FlashMessage flashMessage) {
        list.add(flashMessage);
    }

    public static void add(String message, int type) {
        list.add(new FlashMessage(message, type));
    }

    public static void add(String message) {
        list.add(new FlashMessage(message, INFO));
    }

    public static FlashMessage get() {
        return list.poll();
    }

    public static void clear() {
        list.clear();
    }

    public static boolean isEmpty() {
        return list.isEmpty();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
