package net.akhyar.plurkita.model;

import java.util.List;
import java.util.Map;

/**
 * @author akhyar
 */
public class Timeline {

    List<Plurk> plurks;
    Map<String, User> plurkUsers;

    public Map<String, User> getPlurkUsers() {
        return plurkUsers;
    }

    public List<Plurk> getPlurks() {
        return plurks;
    }

}
