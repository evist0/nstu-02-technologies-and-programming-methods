package com.evist0.engine.components;

import java.awt.*;

public class ScoreComponent extends Component {
    private int playerScore;
    private int enemyScore;

    public ScoreComponent() {
        this.playerScore = 0;
        this.enemyScore = 0;
    }

    @Override
    protected void doDraw(Graphics graphics) {
        TransformComponent transform = getEntity().getComponent(TransformComponent.class);
        String scoreString = "%d:%d".formatted(playerScore, enemyScore);

        graphics.setColor(Color.WHITE);
        graphics.drawString(scoreString, (int) transform.getX(), (int) transform.getY());
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setEnemyScore(int enemyScore) {
        this.enemyScore = enemyScore;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public int getEnemyScore() {
        return this.enemyScore;
    }
}
