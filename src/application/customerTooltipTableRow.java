package application;
import java.lang.reflect.Field;
import java.util.function.Function;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class customerTooltipTableRow<T> extends TableRow<T> {
	private Function<T, String> toolTipStringFunction;

	public customerTooltipTableRow(Function<T, String> toolTipStringFunction) {
		this.toolTipStringFunction = toolTipStringFunction;
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if(item == null) {
			setTooltip(null);
		} else {
			Tooltip tooltip = new Tooltip();
			tooltip.setStyle("-fx-font-size: 15");
			tooltip.setText(toolTipStringFunction.apply(item));
			hackTooltipStartTiming(tooltip);
			setTooltip(tooltip);
		}
	}
	public static void hackTooltipStartTiming(Tooltip tooltip) {
	    try {
	        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
	        fieldBehavior.setAccessible(true);
	        Object objBehavior = fieldBehavior.get(tooltip);

	        Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
	        fieldTimer.setAccessible(true);
	        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

	        objTimer.getKeyFrames().clear();
	        objTimer.getKeyFrames().add(new KeyFrame(new Duration(100)));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}