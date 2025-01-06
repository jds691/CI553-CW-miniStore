package clients.adapters;

import logic.Product;
import logic.ProductReader;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Converts PRODUCTTABLE.name to PRODUCTTABLE.productNo
 */
public class ProductNameAdapter {
    private final HashMap<String, String> productMapping = new HashMap<>();

    /**
     * Initialises the list of products to convert between from the product repository.
     *
     * @param productReader ProductReader to get information from
     */
    public ProductNameAdapter(ProductReader productReader) {
        try {
            for (Product product : productReader.readAllProducts()) {
                productMapping.put(product.getName(), product.getProductNumber());
            }
        } catch (RemoteException ignored) {
            // Disable the adapter
        }
    }

    /**
     * Takes a product name and attempts to link it to a relevant product in the system.
     *
     * @param productName Name searched by the user
     * @return Product number of the closest match or null if there are no matches
     *
     * @implNote This method will always return the first available product, ordered by product number
     */
    public String getProductNumber(String productName) {
        for (String name : productMapping.keySet()) {
            if (name.toLowerCase().contains(productName.toLowerCase())) {
                return productMapping.get(name);
            }
        }

        return null;
    }
}
