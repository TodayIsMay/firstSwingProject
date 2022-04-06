package data;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public String toString() {
        return  "id=" + id + ", name='" + name + "' , creationTime=" + creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id && Objects.equals(name, entity.name) && Objects.equals(creationTime, entity.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creationTime);
    }
}