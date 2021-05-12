package com.evist0.engine.components;

import com.evist0.engine.networking.ClientRpc;
import com.evist0.engine.networking.HostRpc;

public class RemoteTransformComponent extends Component {
    @HostRpc
    @ClientRpc
    public void setPositionRpc(float x, float y) {
        TransformComponent transform = getTransform();
        if (!Float.isNaN(x)) {
            transform.setX(x);
        }
        if (!Float.isNaN(y)) {
            transform.setY(y);
        }
    }
}
