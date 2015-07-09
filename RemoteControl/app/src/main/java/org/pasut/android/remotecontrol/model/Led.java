package org.pasut.android.remotecontrol.model;

import com.google.common.base.Objects;

import static com.google.common.base.Objects.equal;

/**
 * Created by marcelo on 09/07/15.
 */
public class Led {
    private final int id;

    private String name;

    private boolean state;
    public Led(final int id) {
        this(id, false);
    }

    public Led(final int id, final boolean state) {
        this(id, state, null);
    }

    public Led(final int id, final boolean state, final String name) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Led)) return false;
        Led led = (Led)o;
        return equal(id, led.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
