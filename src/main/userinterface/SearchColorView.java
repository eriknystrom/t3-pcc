// specify the package
package userinterface;

// system imports
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

/** The class containing the Add Article Type View  for the Professional Clothes
 *  Closet application
 */
//==============================================================
public class SearchColorView extends View
{

    // GUI components
    protected TextField description;
    protected TextField alphaCode;

    protected PccButton submitButton;
    protected PccButton cancelButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object

    public SearchColorView(IModel at)
    {
        super(at, "SearchColorView");

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


    @Override
    protected String getActionText()
    {
        return "** Search for Colors **";
    }

    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);
        Font myFont = Font.font(APP_FONT, FontWeight.BOLD, 12);

        PccText prompt1 = new PccText("Enter Color Barcode Prefix (if known)");
        prompt1.setWrappingWidth(WRAPPING_WIDTH);
        prompt1.setTextAlignment(TextAlignment.CENTER);
        prompt1.setFill(Color.web(APP_TEXT_COLOR));
        prompt1.setFont(Font.font(APP_FONT, FontWeight.BOLD, 18));
        vbox.getChildren().add(prompt1);

        GridPane grid0 = new GridPane();
        grid0.setAlignment(Pos.CENTER);
        grid0.setHgap(10);
        grid0.setVgap(10);
        grid0.setPadding(new Insets(0, 25, 10, 0));

        vbox.getChildren().add(grid0);

        PccText prompt2 = new PccText(" - Otherwise, enter other criteria below - ");
        prompt2.setWrappingWidth(WRAPPING_WIDTH);
        prompt2.setTextAlignment(TextAlignment.CENTER);
        prompt2.setFill(Color.web(APP_TEXT_COLOR));
        prompt2.setFont(Font.font(APP_FONT, FontWeight.BOLD, 18));
        vbox.getChildren().add(prompt2);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

        PccText descripLabel = new PccText(" Description : ");
        descripLabel.setFont(myFont);
        descripLabel.setWrappingWidth(150);
        descripLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(descripLabel, 0, 1);

        description = new TextField();
        description.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9 -]{0,30}")) {
                description.setText(oldValue);
            }
        });
        description.setOnAction(e -> {
            clearErrorMessage();
            Properties props = new Properties();

            String descrip = description.getText();
            props.setProperty("Description", descrip);
            String alfaC = alphaCode.getText();
            props.setProperty("AlphaCode", alfaC);
            myModel.stateChangeRequest("SearchColor", props);

        });
        grid.add(description, 1, 1);

        PccText alphaCodeLabel = new PccText(" Alpha Code : ");
        alphaCodeLabel.setFont(myFont);
        alphaCodeLabel.setWrappingWidth(150);
        alphaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(alphaCodeLabel, 0, 2);

        alphaCode = new TextField();
        alphaCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]{0,5}")) {
                alphaCode.setText(oldValue);
            }
            if(newValue.matches("[A-Za-z]{0,5}")) {
                alphaCode.setText(newValue.toUpperCase());
            }
        });
		alphaCode.setOnAction(e -> {
            clearErrorMessage();
            Properties props = new Properties();

            String descrip = description.getText();
            props.setProperty("Description", descrip);
            String alfaC = alphaCode.getText();
            props.setProperty("AlphaCode", alfaC);
            myModel.stateChangeRequest("SearchColor", props);

        });
        grid.add(alphaCode, 1, 2);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new PccButton("Submit");
        submitButton.setOnAction(e -> {
            clearErrorMessage();
            Properties props = new Properties();

            String descrip = description.getText();
            props.setProperty("Description", descrip);
            String alfaC = alphaCode.getText();
            props.setProperty("AlphaCode", alfaC);
            myModel.stateChangeRequest("SearchColor", props);

        });
        doneCont.getChildren().add(submitButton);

        cancelButton = new PccButton("Return");
        cancelButton.setOnAction(e -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelSearchColor", null);
        });
        doneCont.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }


    // Create the status log field

    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }


    public void populateFields()
    {

    }

    /**
     * Update method
     */

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

    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */

    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */

    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

}


//	Revision History:
//

