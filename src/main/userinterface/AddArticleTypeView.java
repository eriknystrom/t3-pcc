// specify the package
package userinterface;

// system imports
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Properties;

// project imports
import impresario.IModel;

import javax.swing.*;

/** The class containing the Add Article Type View  for the Professional Clothes
 *  Closet application
 */
//==============================================================
public class AddArticleTypeView extends View
{

	// GUI components
	protected TextField barcodePrefix;
	protected TextField description;
	protected TextField alphaCode;

	protected PccButton submitButton;
	protected PccButton cancelButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public AddArticleTypeView(IModel at)
	{
		super(at, "AddArticleTypeView");

		// create a container for showing the contents
		VBox container = getParentContainer();

		// Add a title for this panel
		container.getChildren().add(createTitle());

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("TransactionError", this);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** Adding a new Article Type **";
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		Text prompt = new Text("ARTICLE TYPE INFORMATION");
        prompt.setWrappingWidth(WRAPPING_WIDTH);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
		prompt.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		vbox.getChildren().add(prompt);


		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

		Text barcodePrefixLabel = new Text(" Barcode Prefix : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		barcodePrefixLabel.setFont(myFont);
		barcodePrefixLabel.setWrappingWidth(150);
		barcodePrefixLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodePrefixLabel, 0, 1);

		barcodePrefix = new TextField();
        barcodePrefix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]{0,5}")) {
                barcodePrefix.setText(oldValue);
            }
        });
		grid.add(barcodePrefix, 1, 1);

		Text descripLabel = new Text(" Description : ");
		descripLabel.setFont(myFont);
		descripLabel.setWrappingWidth(150);
		descripLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(descripLabel, 0, 2);

		description = new TextField();
		description.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("[a-zA-Z0-9]{0,30}")) {
		        description.setText(oldValue);
            }
		});
		grid.add(description, 1, 2);

		Text alphaCodeLabel = new Text(" Alpha Code : ");
		alphaCodeLabel.setFont(myFont);
		alphaCodeLabel.setWrappingWidth(150);
		alphaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(alphaCodeLabel, 0, 3);

		alphaCode = new TextField();
		alphaCode.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("[a-zA-Z]{0,5}")) {
                        alphaCode.setText(oldValue);
		    }
		});
		grid.add(alphaCode, 1, 3);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		submitButton = new PccButton("Submit");
		submitButton.setOnAction(this::processAction);
		doneCont.getChildren().add(submitButton);

		cancelButton = new PccButton("Return");
		cancelButton.setOnAction(e -> {
		   clearErrorMessage();
		   myModel.stateChangeRequest("CancelAddAT", null);
	  });
		doneCont.getChildren().add(cancelButton);

		vbox.getChildren().add(grid);
		vbox.getChildren().add(doneCont);

		return vbox;
	}

	private void processAction(ActionEvent e) {
		clearErrorMessage();
		Properties props = new Properties();
		String bcPrfx = barcodePrefix.getText();
		if (bcPrfx.length() > 0)
		{
			props.setProperty("BarcodePrefix", bcPrfx);
			String descrip = description.getText();
			if (descrip.length() > 0)
			{
				props.setProperty("Description", descrip);
				String alfaC = alphaCode.getText();
				if (alfaC.length() > 0)
				{
					props.setProperty("AlphaCode", alfaC);
					myModel.stateChangeRequest("ArticleTypeData", props);
				}
				else
				{
					displayErrorMessage("ERROR: Please enter a valid alpha code!");
				}
			}
			else
			{
				displayErrorMessage("ERROR: Please enter a valid description!");
			}

		}
		else
		{
			displayErrorMessage("ERROR: Please enter a barcode prefix!");

		}
	}

	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{

	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();

		if (key.equals("TransactionError") )
		{
			String val = (String)value;
			if (val.startsWith("ERR") )
			{
				displayErrorMessage(val);
			}
			else
			{
				displayMessage(val);
			}

		}
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}

//---------------------------------------------------------------
//	Revision History:
//
