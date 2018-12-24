package gui_elements.buttons;

import authoring.edit_object.ComponentAddConditionsScreen;
import conditions.ConditionManager;
public class CreateConditionsButton extends MainButton {

	private static final String FILENAME = "create_conditions_button.properties";
	private static final boolean EXPLICIT_SET_ACTION = true;
	private ConditionManager conditionManager;
	public CreateConditionsButton(ConditionManager cm) {
		super(FILENAME, EXPLICIT_SET_ACTION);
		conditionManager = cm;
		setAction();
	}

	@Override
	protected void setAction() {
		this.setOnAction(e -> new ComponentAddConditionsScreen(conditionManager));
	}
}
