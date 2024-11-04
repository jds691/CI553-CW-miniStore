package remote;

import java.sql.Connection;

public abstract class Repository<Entity, Identifier> {
    protected final Connection connection;

    public Repository(Connection connection) {
        this.connection = connection;
    }

    public abstract Entity create();
    public abstract Entity read(Identifier id);
    public abstract boolean update(Entity entity);
    public abstract void delete(Entity entity);
}