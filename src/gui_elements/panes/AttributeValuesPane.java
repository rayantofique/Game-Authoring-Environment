package gui_elements.panes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import game_object.ObjectAttributes;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class AttributeValuesPane extends MainPane {

	private FlowPane flow_pane;
	private String full_directory_name = DIRECTORY_STRING + "attribute_values_pane.properties";
    private static final String CATCH_IO_EXCEPTION_STRING = "Attribute values pane file input does not exist!";
	private static final String FINALLY_IO_EXCEPTION_STRING = "Attribute values pane file input cannot close!";
	private static final String PANE_STYLE = "-fx-background-color: #000000";
	private int x, y, width, height;
	private static final int TEXTFIELD_SIZE = 10;
	private static final double DEFAULT_ATTRIBUTE_VALUE = 0.0;
	private static final Pos TEXTFIELD_POSITION = Pos.CENTER;	
	private ObjectAttributes objAttributesInstance;	
	
	public AttributeValuesPane(ObjectAttributes objAttributesInstance) {
		this.objAttributesInstance = objAttributesInstance;
		initialize();
	}
	
	public void initialize() {
		getProperties();
		createPane();
	}
	
	@Override
	protected void getProperties() {
		properties = new Properties();
		input = null;
		try {
	  		input = new FileInputStream(full_directory_name);
	  		properties.load(input);

	  		x = Integer.parseInt(properties.getProperty(X_LOC_STRING));
	  		y = Integer.parseInt(properties.getProperty(Y_LOC_STRING));
	  		width = Integer.parseInt(properties.getProperty(WIDTH));
	  		height = Integer.parseInt(properties.getProperty(HEIGHT));
		} catch (IOException ex) {
			logError(ex, Level.SEVERE, CATCH_IO_EXCEPTION_STRING);
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				logError(e, Level.SEVERE, FINALLY_IO_EXCEPTION_STRING);
    			}
    		}
    	}
    }
	
	private void logError(Exception e, Level level, String error) {
		Logger logger = Logger.getAnonymousLogger();
		Exception ex = new Exception(e);
		logger.log(level, error, ex);
	}

	@Override
	protected void createPane() {
		flow_pane = new FlowPane();
		flow_pane.setLayoutX(x);
		flow_pane.setLayoutY(y);
		flow_pane.setPrefSize(width, height);
		flow_pane.setStyle(PANE_STYLE);

		for(double attribute_value : objAttributesInstance.getAttributeValues()) {
			createTextField(attribute_value);
		}
		createTextField(DEFAULT_ATTRIBUTE_VALUE);
		
	}
	
	private void createTextField(double attribute_value) {
		TextField attribute_name_field = new TextField(Double.toString(attribute_value));
		attribute_name_field.setAlignment(TEXTFIELD_POSITION);
		attribute_name_field.setPrefSize(width, height / TEXTFIELD_SIZE);
		flow_pane.getChildren().add(attribute_name_field);
	}

	@Override
	protected void setX(int x) {
		flow_pane.setLayoutX(x);
	}

	@Override
	protected void setY(int y) {
		flow_pane.setLayoutY(y);
	}

	@Override
	protected int getX() {
		return (int) flow_pane.getLayoutX();
	}

	@Override
	protected int getY() {
		return (int) flow_pane.getLayoutY();
	}
	
	@Override
	public Pane getPane() {
		return flow_pane;
	}
}