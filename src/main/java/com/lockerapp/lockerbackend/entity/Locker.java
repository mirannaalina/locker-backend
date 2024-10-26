package com.lockerapp.lockerbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private boolean isAvailable;

    public Locker() {
    }

    public Locker(String location, boolean isAvailable) {
        this.location = location;
        this.isAvailable = isAvailable;
    }

    // equals() și hashCode() care folosesc doar id-ul
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locker locker = (Locker) o;
        return id != null && id.equals(locker.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
