package com.evist0.engine.components;

import com.evist0.engine.InputManager;

import java.awt.*;

public class PauseComponent extends Component {
    private final int pauseKeyCode;

    private boolean playerReady = false;
    private boolean enemyReady = false;

    public PauseComponent(int pauseKeyCode) {
        this.pauseKeyCode = pauseKeyCode;
    }

    public boolean isPaused() {
        return !playerReady || !enemyReady;
    }

    public void setPlayerReady(boolean ready) {
        playerReady = ready;
    }

    public void setEnemyReady(boolean ready) {
        enemyReady = ready;
    }

    @Override
    protected void doUpdate(float deltaTime) {
        InputManager input = getEntity().getApplication().getInputManager();

        if (input.getKeyPressed(pauseKeyCode) && !playerReady) {
            setPlayerReady(true);
            getEntity().getApplication().getNetworkManager().rpc(0, RemotePauseComponent.class.getName(), "setEnemyReady", 1);
        }
    }
}
