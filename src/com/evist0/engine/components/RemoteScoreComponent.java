package com.evist0.engine.components;

import com.evist0.engine.networking.ClientRpc;
import com.evist0.engine.networking.HostRpc;

public class RemoteScoreComponent extends Component {
    @HostRpc
    @ClientRpc
    public void setPlayerScore(int score) {
        ScoreComponent scoreComponent = getEntity().getComponent(ScoreComponent.class);
        scoreComponent.setPlayerScore(score);
    }

    @HostRpc
    @ClientRpc
    public void setEnemyScore(int score) {
        ScoreComponent scoreComponent = getEntity().getComponent(ScoreComponent.class);
        scoreComponent.setEnemyScore(score);
    }
}
