package server;

import logic.Product;
import logic.StockWriter;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StockWriterRemoteWrapper
        extends UnicastRemoteObject
        implements StockWriter {
    @Serial
    private static final long serialVersionUID = 1;

    private final StockWriter origin;

    public StockWriterRemoteWrapper(StockWriter origin) throws RemoteException {
        this.origin = origin;
    }

    @Override
    public synchronized boolean buyStock(String productNumber, int amount) {
        return origin.buyStock(productNumber, amount);
    }

    @Override
    public synchronized void addStock(String productNumber, int amount) {
        origin.addStock(productNumber, amount);
    }

    @Override
    public synchronized void modifyStock(Product detail) {
        origin.modifyStock(detail);
    }
}
