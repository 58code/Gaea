package Serializer;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 * enmReadState
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable
public enum EnmReadState {

    Read(0),
    UnRead(1),
    All(2);
    private final int eNum;

    public int getENum() {
        return this.eNum;
    }

    private EnmReadState(int stateNum) {
        this.eNum = stateNum;
    }
}
