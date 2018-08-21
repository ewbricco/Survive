package eastin.Survive.utils;

import java.io.Serializable;

/**
 * Created by ebricco on 8/21/18.
 */
public enum Corner implements Serializable {
    BOTTOMLEFT, TOPLEFT, TOPRIGHT, BOTTOMRIGHT;

    public Corner getOpposite() {
        if (this.name().equals("BOTTOMLEFT")) {
            return TOPRIGHT;
        } else if (this.name().equals("TOPLEFT")) {
            return BOTTOMRIGHT;
        } else if (this.name().equals("TOPRIGHT")) {
            return BOTTOMLEFT;
        } else if (this.name().equals("BOTTOMRIGHT")) {
            return TOPLEFT;
        }

        return null;
    }
}
