package de.turidus.buttpluggui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FXMLHelper {

    private static final Logger logger = LoggerFactory.getLogger(FXMLHelper.class.getSimpleName());

    public static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch(IOException e) {
            logger.error("FXML could not be loaded: " + fxml + ".fxml");
            throw new RuntimeException(e);
        }
    }

    public static FXMLLoader getFXMLLoader(String fxml) {
        return  new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(fxml + ".fxml"));
    }

}
