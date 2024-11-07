package remote;

import java.sql.Connection;

public abstract class Repository<Entity> {
    protected final Connection connection;

    public Repository(Connection connection) {
        this.connection = connection;
    }

    public abstract Entity create();
    public abstract Entity read(String id);
    public abstract boolean update(Entity entity);
    public abstract void delete(Entity entity);
}