package com.evist0.engine.components;


public class BallComponent extends Component {
    private final float resetPositionX;
    private final float resetPositionY;

    private final float velocity;
    private float velocityX;
    private float velocityY;

    private final RectangleComponent leftRectangle;
    private final RectangleComponent rightRectangle;

    private final ScoreComponent scoreComponent;

    private final PauseComponent pauseComponent;

    public BallComponent(float resetPositionX,
                         float resetPositionY,
                         float velocity,
                         RectangleComponent leftRectangle,
                         RectangleComponent rightRectangle,
                         ScoreComponent scoreComponent,
                         PauseComponent pauseComponent) {
        this.resetPositionX = resetPositionX;
        this.resetPositionY = resetPositionY;
        this.velocity = velocity;
        this.velocityX = velocity;
        this.velocityY = 0.f;
        this.leftRectangle = leftRectangle;
        this.rightRectangle = rightRectangle;
        this.scoreComponent = scoreComponent;
        this.pauseComponent = pauseComponent;
    }

    private void resetPosition() {
        TransformComponent transform = getEntity().getComponent(TransformComponent.class);

        transform.setX(resetPositionX);
        transform.setY(resetPositionY);
    }

    private RectangleComponent findRectangleUsingVelocity() {
        if (velocityX > 0)
            return rightRectangle;

        return leftRectangle;
    }

    @Override
    protected void doUpdate(float deltaTime) {
        if (pauseComponent.isPaused()) {
            return;
        }

        TransformComponent transform = getEntity().getComponent(TransformComponent.class);
        CircleComponent circle = getEntity().getComponent(CircleComponent.class);

        transform.setX(transform.getX() + velocityX * deltaTime);
        transform.setY(transform.getY() + velocityY * deltaTime);

        getEntity().getApplication().getNetworkManager().rpc(3, RemoteTransformComponent.class.getName(), "setPositionRpc", 1280 - transform.getX(), transform.getY());

        int x = (int) transform.getX();
        int y = (int) transform.getY();
        int radius = circle.getRadius();

        boolean goal = false;

        if (!isRectangleCollision(circle) && x < 0) {
            int score = scoreComponent.getEnemyScore() + 1;

            scoreComponent.setEnemyScore(score);
            getEntity().getApplication().getNetworkManager().rpc(0, RemoteScoreComponent.class.getName(), "setPlayerScore", score);
            resetPosition();

            goal = true;
        }

        if (!isRectangleCollision(circle) && x > 1280) {
            int score = scoreComponent.getPlayerScore() + 1;

            scoreComponent.setPlayerScore(score);
            getEntity().getApplication().getNetworkManager().rpc(0, RemoteScoreComponent.class.getName(), "setEnemyScore", score);
            resetPosition();

            goal = true;
        }

        if (goal) {
            getEntity().getApplication().getNetworkManager().rpc(0, RemotePauseComponent.class.getName(), "setEnemyReady", 0);
            getEntity().getApplication().getNetworkManager().rpc(0, RemotePauseComponent.class.getName(), "setPlayerReady", 0);
            pauseComponent.setEnemyReady(false);
            pauseComponent.setPlayerReady(false);
        }

        if (y - radius < 0 || y + radius > 720) {
            velocityY = -velocityY;
        }
    }

    private static float lerp(float x, float y, float t) {
        return x + y * t;
    }

    private static float clamp(float v, float min, float max) {
        return v < min ? min : Math.min(v, max);
    }

    private boolean isRectangleCollision(CircleComponent circle) {
        RectangleComponent rectangle = findRectangleUsingVelocity();

        var distX = Math.abs(circle.getTransform().getX() - rectangle.getTransform().getX() - rectangle.getWidth() * 0.5f);
        var distY = circle.getTransform().getY() - rectangle.getTransform().getY() - rectangle.getHeight() * 0.5f;
        float angleSign = Math.signum(distY);
        distY = Math.abs(distY);

        if (distX > (rectangle.getWidth() * 0.5f + circle.getRadius())) {
            return false;
        }
        if (distY > (rectangle.getHeight() * 0.5f + circle.getRadius())) {
            return false;
        }

        if (distY <= (rectangle.getHeight() * 0.5f)) {
            float sign = Math.signum(velocityX);

            velocityX = velocity;
            velocityY = 0;

            float angle = angleSign * lerp((float) Math.toRadians(0), (float) Math.toRadians(30), distY / rectangle.getHeight() * 2.f);

            float rvx = (float) ((velocityX * Math.cos(angle)) - (velocityY * Math.sin(angle)));
            float rvy = (float) ((velocityX * Math.sin(angle)) + (velocityY * Math.cos(angle)));

            velocityX = -sign * rvx;
            velocityY = rvy;

            return true;
        }

        return false;
    }
}
