package com.evist0.engine.networking.components;

import com.evist0.engine.components.Component;

public class NetworkComponent extends Component {
    private final int id;

    public NetworkComponent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
