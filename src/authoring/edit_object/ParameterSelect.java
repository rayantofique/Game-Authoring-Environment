package authoring.edit_object;

import java.util.Iterator;
import java.util.List;

import authoring.support.Extractor;
import authoring.view.AuthoringView;
import game_object.PropertyNotFoundException;
import gui_elements.factories.ButtonFactory;
import gui_elements.factories.TextFieldFactory;
import interactions.CustomComponentParameterFormat;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class ParameterSelect implements AuthoringView {

	protected Stage stage;
	protected Scene scene;
	protected VBox root;

	public ParameterSelect() {
		initializeScene();
		initializeStage();
		initializeButtons();
		customize();
	}
	
	protected void initializeScene() {
		root = new VBox();
		scene = new Scene (root, 1.2*PANEL_WIDTH, PANEL_HEIGHT / 2, DEFAULT_BACKGROUND);
		root.setPadding(LINE_INSETS);
	}
	
	protected void initializeStage() {
		stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Edit Custom Conditions");
		stage.setResizable(false);
		stage.show();		
	}
	
	protected void initializeButtons() {
		HBox box = new HBox();
		box.getChildren().addAll(
				ButtonFactory.makeButton("Add Line", e -> addLine()),
				ButtonFactory.makeButton("Save", e -> saveAll()));
		root.getChildren().add(box);
	}
	
	protected abstract void customize();
	protected abstract void addLine();
	protected abstract void loadSaved();
	@SuppressWarnings("rawtypes")
	protected abstract void save(HBox line, ComboBox comboBox);
	
	protected void clearConditionParameters(HBox line) {
		for (Iterator<Node> iterator = line.getChildren().iterator(); iterator.hasNext();) {
			Node node = iterator.next();
			if (node instanceof TextField) {
				iterator.remove();
			}
		}
		
	}
	
	protected void saveAll() {
		clear();
		for (Node node: root.getChildren()) {
			if (node instanceof HBox) {
				extractInfo((HBox) node);
			}
		}
		stage.close();
	}
	
	protected abstract void clear();
	@SuppressWarnings("rawtypes")
	protected void extractInfo(HBox box) {
		Node nodeOne = box.getChildren().get(0);
		if (nodeOne instanceof ComboBox && ((ComboBox) nodeOne).getValue() != null) {
			save(box, (ComboBox) nodeOne);
		}
	}
	
	protected void loadParameters(HBox line, CustomComponentParameterFormat format) {
		List<String> parameters = format.getParameterList();
		for (String p: parameters) {
			String value;
			try {
				value = format.getParameterValue(p);
			} catch (PropertyNotFoundException e) {
				value = "";
			}
			line.getChildren().add(TextFieldFactory.makeTextField(value, p));
		}
	}
	
	protected void setParameters(HBox box, CustomComponentParameterFormat format) {
		List<String> parameters = format.getParameterList();
		for (int i = 0; i<parameters.size(); i++) {
			format.setFieldValue(parameters.get(i), Extractor.extractTextField(box.getChildren().get(i+1)));
		}
	}
	
	protected void setParameterFields(HBox line, CustomComponentParameterFormat format) {
		List<String> parameters = format.getParameterList();
		for (int i = 0; i<parameters.size(); i++) {
			line.getChildren().add(TextFieldFactory.makeTextFieldPrompt(parameters.get(i)));
		}
	}	
}
