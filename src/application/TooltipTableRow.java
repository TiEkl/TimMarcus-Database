package application;
import java.lang.reflect.Field;
import java.util.function.Function;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TooltipTableRow<T> extends TableRow<T> {
	private String missingCover = "http://www.jameshmayfield.com/wp-content/uploads/2015/03/defbookcover-min.jpg";
	private Function<T, String> toolTipStringFunction;

	public TooltipTableRow(Function<T, String> toolTipStringFunction) {
		this.toolTipStringFunction = toolTipStringFunction;
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if(item == null) {
			setTooltip(null);
		} else {
			Tooltip tooltip = new Tooltip();
			if(toolTipStringFunction.apply(item) != null) {
			Image image = new Image(toolTipStringFunction.apply(item), 500, 250, true, true, true);
			tooltip.setGraphic(new ImageView(image));
			}
			else {
				Image image = new Image(missingCover, 500, 250, true, true, true);
				tooltip.setGraphic(new ImageView(image));
			}
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