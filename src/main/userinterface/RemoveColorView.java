// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing the Add Article Type View  for the Professional Clothes
 *  Closet application
 */
//==============================================================
public class RemoveColorView extends View
{

    protected PccButton submitButton;
    protected PccButton cancelButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object

    public RemoveColorView(IModel at)
    {
        super(at, "RemoveColorView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

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
        return "** Remove Color Type **";
    }

    // Create the main form content

    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        Text prompt1 = new Text("Are you sure you wish to remove color?");
        prompt1.setWrappingWidth(400);
        prompt1.setTextAlignment(TextAlignment.CENTER);
        prompt1.setFill(Color.BLACK);
        prompt1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vbox.getChildren().add(prompt1);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new PccButton("Yes");
        submitButton.setOnAction(e -> myModel.stateChangeRequest("RemoveColor", null));
        doneCont.getChildren().add(submitButton);

        cancelButton = new PccButton("No");
        cancelButton.setOnAction(e -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelRemoveCT", null);
        });
        doneCont.getChildren().add(cancelButton);

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
