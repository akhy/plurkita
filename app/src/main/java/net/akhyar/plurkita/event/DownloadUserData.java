package net.akhyar.plurkita.event;

/**
 * @author akhyar
 */
public class DownloadUserData {
    private long[] ids;

    public DownloadUserData(long... ids) {
        this.ids = ids;
    }

    public long[] getIds() {
        return ids;
    }
}
