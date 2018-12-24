package gui_elements.buttons;

import authoring.edit_object.ComponentAddInteractionsScreen;
import interactions.InteractionManager;

public class InteractionOkButton extends MainButton {

	private static final String FILENAME = "interaction_ok_button.properties";
	private InteractionManager interaction_manager;
	private ComponentAddInteractionsScreen component_add_interactions_screen;
	private static final boolean EXPLICIT_SET_ACTION = false;

	public InteractionOkButton(InteractionManager interaction_manager, ComponentAddInteractionsScreen component_add_interactions_screen) {
		super(FILENAME, EXPLICIT_SET_ACTION);
		this.interaction_manager = interaction_manager;
		this.component_add_interactions_screen = component_add_interactions_screen;
		setAction();
	}

	@Override
	protected void setAction() {
		component_add_interactions_screen.getStage().setOnCloseRequest(e -> {
			consume();
		});
		
		getButton().setOnAction(value -> {
			consume();
		});
	}
	
	private void consume() {
		interaction_manager.removeLastAddedInteraction();
		component_add_interactions_screen.getStage().close();
	}
}