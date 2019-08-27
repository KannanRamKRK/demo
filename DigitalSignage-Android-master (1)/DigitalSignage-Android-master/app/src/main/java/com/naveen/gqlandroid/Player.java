package com.naveen.gqlandroid;

class Player {
    private static final Player ourInstance = new Player();

    static Player getInstance() {
        return ourInstance;
    }

    private Player() {
    }
}
