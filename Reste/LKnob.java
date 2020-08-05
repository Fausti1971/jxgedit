package gui;


import parm.XGParameter;
import parm.XGParameterConstants;

public class LKnob extends Control
{	private static final int FONTSIZE = 12;
	private static final double RADIUS = 30, W = RADIUS * 2 + 4, H = RADIUS * 2 + FONTSIZE + 4, CX = W / 2, CY = RADIUS + FONTSIZE + 5;
	private static final Color BACK = Color.LIGHTGRAY, FORE = Color.DEEPSKYBLUE;

/**************************************************************************************************************/

	public LKnob(XGParameter p)
	{	this.setMaxSize(W, H);
		this.setMinSize(W, H);
		this.setHeight(H);
		this.setWidth(W);
		this.setManaged(true);
		this.setFocusTraversable(true);
		this.setOnMouseClicked(this::handleMouseClick);
		this.setOnMouseEntered(this::handleMouseEnter);
		this.setOnMouseExited(this::handleMouseExited);
		this.setUserData(p);

		Region r = new Region();
		r.setShape(new Rectangle(W, H));
		this.getChildren().add(r);

		Text t = new Text("Parameter");
		t.setFont(Font.font(FONTSIZE));
		t.setLayoutX(CX - t.getLayoutBounds().getWidth() / 2);
		t.setY(FONTSIZE);
		t.setFill(FORE);
		this.getChildren().add(t);

		Arc back = new Arc(CX, CY, RADIUS, RADIUS, 225.0, -270.0);
		back.setFill(null);
		back.setType(ArcType.OPEN);
		back.setSmooth(true);
		back.setStroke(BACK);
		back.setStrokeWidth(4.0);
		back.setStrokeType(StrokeType.INSIDE);
		back.setStrokeLineCap(StrokeLineCap.ROUND);
		this.getChildren().add(back);

		Arc fore = new Arc(CX, CY, RADIUS, RADIUS, 225.0, -270.0 / 100 * p.value.get());
		fore.lengthProperty().bind(p.value);
		fore.setFill(null);
		fore.setType(ArcType.OPEN);
		fore.setSmooth(true);
		fore.setStroke(FORE);
		fore.setStrokeWidth(4.0);
		fore.setStrokeType(StrokeType.INSIDE);
		fore.setStrokeLineCap(StrokeLineCap.ROUND);
//		fore.setEffect(new Bloom(0.2));
		this.getChildren().add(fore);

		Text v = new Text();
		v.setFont(Font.font(10));
		v.textProperty().bind(p.valueText);
		v.setLayoutX(CX - v.getLayoutBounds().getWidth() / 2);
		v.setY(CY);
		v.setFill(FORE);
		this.getChildren().add(v);
	}

	private void handleMouseClick(MouseEvent e)
	{	switch(e.getButton())
		{	case PRIMARY:	setValue(1); break;
			case SECONDARY:	setValue(-1); break;
			case MIDDLE:
			case NONE:		return;
		}
	}

	private void setValue(int delta)
	{	
		
	}

	private void handleMouseEnter(MouseEvent e)
	{	this.setFocused(true);
		this.setEffect(new Bloom());
	}

	private void handleMouseExited(MouseEvent e)
	{	this.setFocused(false);
		this.setEffect(null);
	}
}
