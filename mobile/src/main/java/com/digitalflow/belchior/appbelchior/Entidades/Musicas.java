package com.digitalflow.belchior.appbelchior.Entidades;

/**
 * Created by MarllonS on 25/02/2018.
 */

public class Musicas {

    private String id;
    private boolean locked;

    public Musicas() {

    }

    public Musicas(String id, String name, boolean locked){
        this.id = id;
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
