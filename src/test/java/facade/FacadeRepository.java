package facade;

import remote.Repository;

import java.util.ArrayList;

public abstract class FacadeRepository<Entity> extends Repository<Entity> {
    protected ArrayList<Entity> entities = new ArrayList<>();

    public void add(Entity entity) {
        entities.add(entity);
    }

    public void remove(Entity entity) {
        entities.remove(entity);
    }

    public void clear() {
        entities.clear();
    }

    public boolean contains(Entity entity) {
        return entities.contains(entity);
    }
}
