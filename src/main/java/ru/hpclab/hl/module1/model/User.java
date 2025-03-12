package ru.hpclab.hl.module1.model;


import org.springframework.lang.NonNull;

import java.util.UUID;

public class User {

    @NonNull
    private UUID identifier;
    @NonNull
    private String fio;

    public User(@NonNull UUID identifier, @NonNull String fio) {
        this.identifier = identifier;
        this.fio = fio;
    }

    public User() {
    }

    @NonNull
    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NonNull UUID identifier) {
        this.identifier = identifier;
    }

    @NonNull
    public String getFio() {
        return fio;
    }

    public void setFio(@NonNull String fio) {
        this.fio = fio;
    }

    @Override
    public String toString() {
        return "User{" +
                "identifier=" + identifier +
                ", fio='" + fio + '\'' +
                '}';
    }
}
