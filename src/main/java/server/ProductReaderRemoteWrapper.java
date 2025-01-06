package server;

import logic.Product;
import logic.ProductReader;
import remote.Repository;

import javax.swing.*;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class ProductReaderRemoteWrapper
        extends UnicastRemoteObject
        implements ProductReader {

    @Serial
    private static final long serialVersionUID = 1;

    private final ProductReader origin;

    public ProductReaderRemoteWrapper(ProductReader origin) throws RemoteException {
        this.origin = origin;
    }

    @Override
    public synchronized boolean doesProductExist(String productNumber) throws RemoteException {
        return origin.doesProductExist(productNumber);
    }

    @Override
    public synchronized Product getProductDetails(String pNum) throws RemoteException {
        return origin.getProductDetails(pNum);
    }

    @Override
    public synchronized ImageIcon getProductImage(String pNum) throws RemoteException {
        return origin.getProductImage(pNum);
    }

    @Override
    public Repository<Product> getRepository() throws RemoteException {
        return origin.getRepository();
    }
}
