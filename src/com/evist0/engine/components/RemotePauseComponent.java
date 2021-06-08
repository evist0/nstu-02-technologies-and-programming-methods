package com.evist0.engine.components;

import com.evist0.engine.networking.ClientRpc;
import com.evist0.engine.networking.HostRpc;

public class RemotePauseComponent extends Component {
    @HostRpc
    @ClientRpc
    public void setEnemyReady(int ready) {
        PauseComponent pauseComponent = getEntity().getComponent(PauseComponent.class);
        pauseComponent.setEnemyReady(ready == 1);
    }

    @ClientRpc
    public void setPlayerReady(int ready) {
        PauseComponent pauseComponent = getEntity().getComponent(PauseComponent.class);
        pauseComponent.setPlayerReady(ready == 1);
    }
}
