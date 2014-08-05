package net.akhyar.plurkita.event;

import net.akhyar.plurkita.model.Plurk;

/**
 * @author akhyar
 */
public class PlurkAdded {
    private Plurk plurk;

    public PlurkAdded(Plurk plurk) {
        this.plurk = plurk;
    }

    public Plurk getPlurk() {
        return plurk;
    }
}
