package com.digitalflow.belchior.appbelchior.Entidades;

/**
 * Created by MarllonS on 03/03/2018.
 */

public class Musicas {
    private String name;
    private Object isUnlocked;

    public Musicas(String name, Object isUnlocked) {
        this.name = name;
        this.isUnlocked = isUnlocked;
    }

    public Musicas(String name) {
        this.name = name;
    }

    public String getNome() {
        return name;
    }

    public void setNome(String name) {
        this.name = name;
    }

    public Object getLocked() {
        return isUnlocked;
    }

    public void setLocked(Object unlocked) {
        isUnlocked = unlocked;
    }

}
