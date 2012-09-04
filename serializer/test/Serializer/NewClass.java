package Serializer;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable
public class NewClass {

    @GaeaMember(sortId=100)
    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

