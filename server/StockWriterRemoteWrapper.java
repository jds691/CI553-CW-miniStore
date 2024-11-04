package server;

import logic.Product;
import logic.StockWriter;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class StockWriterRemoteWrapper
        extends UnicastRemoteObject
        implements StockWriter {
    @Serial
    private static final long serialVersionUID = 1;

    private final StockWriter origin;

    public StockWriterRemoteWrapper(StockWriter origin) throws RemoteException {
        this.origin = origin;
    }

    @Override
    public synchronized boolean buyStock(Product product, int amount) {
        return origin.buyStock(product, amount);
    }

    @Override
    public synchronized void addStock(Product product, int amount) {
        origin.addStock(product, amount);
    }

    @Override
    public synchronized void modifyStock(Product detail) {
        origin.modifyStock(detail);
    }
}
