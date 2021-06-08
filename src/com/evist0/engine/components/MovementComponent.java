package com.evist0.engine.components;

import com.evist0.engine.InputManager;

public class MovementComponent extends Component {
    private final int upKeyCode;
    private final int downKeyCode;
    private final int speed;

    private final PauseComponent pauseComponent;

    public MovementComponent(int upKeyCode, int downKeyCode, int speed, PauseComponent pauseComponent) {
        this.upKeyCode = upKeyCode;
        this.downKeyCode = downKeyCode;
        this.speed = speed;
        this.pauseComponent = pauseComponent;
    }

    @Override
    protected void doUpdate(float deltaTime) {
        if (pauseComponent.isPaused()) {
            return;
        }

        TransformComponent transform = getEntity().getComponent(TransformComponent.class);
        InputManager input = getEntity().getApplication().getInputManager();

        if (input.getKeyPressed(upKeyCode)) {
            transform.setY(transform.getY() - speed * deltaTime);
        }

        if (input.getKeyPressed(downKeyCode)) {
            transform.setY(transform.getY() + speed * deltaTime);
        }

        getEntity().getApplication().getNetworkManager().rpc(2, RemoteTransformComponent.class.getName(), "setPositionRpc", Float.NaN, transform.getY());
    }
}
