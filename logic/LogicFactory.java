package logic;

public interface LogicFactory {
    OrderProcessor getOrderProcessor();

    ProductReader getProductReader();

    StockWriter getStockWriter();
}
