package config.property;

import java.io.File;
import java.io.IOException;

public class TradeProperties extends Property {

    public TradeProperties() {
    }

    @Override
    public File getFile() {
        return new File("config/trade.properties");
    }
}
