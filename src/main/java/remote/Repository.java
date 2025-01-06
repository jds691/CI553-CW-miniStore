package remote;

import java.io.Serializable;

public abstract class Repository<Entity> implements Serializable {
    public abstract Entity create();
    public abstract Entity read(String id);
    /**
     * Reads all entities stored in the repository and returns them.
     *
     * @return All entities.
     */
    public abstract Entity[] readAll();
    public abstract boolean update(Entity entity);
    public abstract void delete(Entity entity);
}