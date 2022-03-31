package data;

import java.time.LocalDateTime;

/**
 * general class of application entities
 */
public class Entity {
    private int id;
    private String name;
    private LocalDateTime creationTime;

    public Entity(int id, String name, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}