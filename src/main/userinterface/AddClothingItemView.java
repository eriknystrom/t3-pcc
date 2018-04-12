// specify the package
package userinterface;

// system imports

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import model.ArticleType;
import model.Color;

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

// project imports

/**
 * The class containing the Add Clothing Item View  for the Professional Clothes
 * Closet application
 */
//==============================================================
public class AddClothingItemView extends View {

    // For showing error message
    protected MessageView statusLog;
    // GUI components
    protected ComboBox<String> genderCombo;
    protected TextField sizeText;
    protected ComboBox<ArticleType> articleTypeCombo;
    protected ComboBox<Color> primaryColorCombo;
    protected ComboBox<Color> secondaryColorCombo;
    protected TextField brandText;
    protected TextField notesText;
    protected TextField donorLastNameText;
    protected TextField donorFirstNameText;
    protected TextField donorPhoneText;
    protected TextField donorEmailText;
    private Button submitButton;
    private Button cancelButton;

    // constructor for this class -- takes a model object
    public AddClothingItemView(IModel clothingItem) {
        super(clothingItem, "AddClothingItemView");
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

    protected String getActionText() {
        return "** Adding a new Clothing Item **";
    }

    // Create the title container
    private Node createTitle() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(1, 1, 1, 30));

        Text clientText = new Text(" Office of Career Services ");
        clientText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        clientText.setWrappingWidth(350);
        clientText.setTextAlignment(TextAlignment.CENTER);
        clientText.setFill(javafx.scene.paint.Color.DARKGREEN);
        container.getChildren().add(clientText);

        Text collegeText = new Text(" THE COLLEGE AT BROCKPORT ");
        collegeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        collegeText.setWrappingWidth(350);
        collegeText.setTextAlignment(TextAlignment.CENTER);
        collegeText.setFill(javafx.scene.paint.Color.DARKGREEN);
        container.getChildren().add(collegeText);

        Text titleText = new Text(" Professional Clothes Closet Management System ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(350);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(javafx.scene.paint.Color.DARKGREEN);
        container.getChildren().add(titleText);

        Text blankText = new Text("  ");
        blankText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText.setWrappingWidth(350);
        blankText.setTextAlignment(TextAlignment.CENTER);
        blankText.setFill(javafx.scene.paint.Color.WHITE);
        container.getChildren().add(blankText);

        Text actionText = new Text("     " + getActionText() + "       ");
        actionText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        actionText.setWrappingWidth(350);
        actionText.setTextAlignment(TextAlignment.CENTER);
        actionText.setFill(javafx.scene.paint.Color.BLACK);
        container.getChildren().add(actionText);

        return container;
    }

    // Create the main form content
    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        Text prompt = new Text("CLOTHING ITEM INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(javafx.scene.paint.Color.BLACK);
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vbox.getChildren().add(prompt);


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

        // Gender UI items ==================================================
        Text genderLabel = new Text(" Gender : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        genderLabel.setFont(myFont);
        genderLabel.setWrappingWidth(150);
        genderLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(genderLabel, 0, 1);

        genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Mens", "Womens");

        grid.add(genderCombo, 1, 1);
        // =================================================================
        // Size UI Items ===================================================
        Text sizeLabel = new Text(" Size : ");
        sizeLabel.setFont(myFont);
        sizeLabel.setWrappingWidth(150);
        sizeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sizeLabel, 0, 2);

        sizeText = new TextField();
        sizeText.setOnAction(this::processAction);
        grid.add(sizeText, 1, 2);

        // =================================================================
        // Article Type UI Items ===========================================
        Text articleTypeLabel = new Text(" Article Type : ");
        articleTypeLabel.setFont(myFont);
        articleTypeLabel.setWrappingWidth(150);
        articleTypeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(articleTypeLabel, 0, 3);

        articleTypeCombo = new ComboBox<>();
        articleTypeCombo.setConverter(new StringConverter<ArticleType>() {
            @Override
            public String toString(ArticleType object) {
                return (String) object.getState("Description");
            }

            @Override
            public ArticleType fromString(String string) {
                return articleTypeCombo.getItems().stream().filter(at ->
                        at.getState("Description").equals(string)).findFirst().orElse(null);
            }
        });
        articleTypeCombo.valueProperty().addListener((obs, oldval, newval) -> {
            /*if (newval != null) {
                System.out.println("Selected article type: " + newval.getState("Description")
                        + " ID: " + newval.getState("ID"));
            }*/
        });

        grid.add(articleTypeCombo, 1, 3);
        // =================================================================
        // Primary Color UI Items ==========================================
        Text primaryColorLabel = new Text(" Primary Color : ");
        primaryColorLabel.setFont(myFont);
        primaryColorLabel.setWrappingWidth(150);
        primaryColorLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(primaryColorLabel, 0, 4);

        primaryColorCombo = new ComboBox<>();
        primaryColorCombo.setConverter(new StringConverter<Color>() {
            @Override
            public String toString(Color object) {
                return (String) object.getState("Description");
            }

            @Override
            public Color fromString(String string) {
                return primaryColorCombo.getItems().stream().filter(ct ->
                        ct.getState("Description").equals(string)).findFirst().orElse(null);
            }
        });
        grid.add(primaryColorCombo, 1, 4);
        // =================================================================
        // Secondary Color UI Items ========================================
        Text secondaryColorLabel = new Text(" Secondary Color : ");
        secondaryColorLabel.setFont(myFont);
        secondaryColorLabel.setWrappingWidth(150);
        secondaryColorLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(secondaryColorLabel, 0, 5);

        secondaryColorCombo = new ComboBox<>();
        secondaryColorCombo.setConverter(new StringConverter<Color>() {
            @Override
            public String toString(Color object) {
                return (String) object.getState("Description");
            }

            @Override
            public Color fromString(String string) {
                return secondaryColorCombo.getItems().stream().filter(ct ->
                        ct.getState("Description").equals(string)).findFirst().orElse(null);
            }
        });
        grid.add(secondaryColorCombo, 1, 5);
        // =================================================================
        // Brand UI Items ==================================================
        Text brandLabel = new Text(" Brand : ");
        brandLabel.setFont(myFont);
        brandLabel.setWrappingWidth(150);
        brandLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(brandLabel, 0, 6);

        brandText = new TextField();
        brandText.setOnAction(this::processAction);
        grid.add(brandText, 1, 6);

        // =================================================================
        // Notes UI Items ==================================================
        Text notesLabel = new Text(" Notes : ");
        notesLabel.setFont(myFont);
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 7);

        notesText = new TextField();
        notesText.setOnAction(this::processAction);
        grid.add(notesText, 1, 7);

        // =================================================================
        // Donor UI Items ==================================================
        Text donorFirstNameLabel = new Text(" Donor First Name : ");
        Text donorLastNameLabel = new Text(" Donor Last Name : ");
        Text donorPhoneLabel = new Text(" Donor Phone Number : ");
        Text donorEmailLabel = new Text(" Donor E-Mail : ");

        donorFirstNameLabel.setFont(myFont);
        donorLastNameLabel.setFont(myFont);
        donorPhoneLabel.setFont(myFont);
        donorEmailLabel.setFont(myFont);

        donorFirstNameLabel.setWrappingWidth(150);
        donorLastNameLabel.setWrappingWidth(150);
        donorPhoneLabel.setWrappingWidth(150);
        donorEmailLabel.setWrappingWidth(150);

        donorFirstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        donorLastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        donorPhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        donorEmailLabel.setTextAlignment(TextAlignment.RIGHT);

        grid.add(donorFirstNameLabel, 0, 9);
        grid.add(donorLastNameLabel, 0, 10);
        grid.add(donorPhoneLabel, 0, 11);
        grid.add(donorEmailLabel, 0, 12);

        donorFirstNameText = new TextField();
        donorFirstNameText.setOnAction(this::processAction);
        donorLastNameText = new TextField();
        donorLastNameText.setOnAction(this::processAction);
        donorPhoneText = new TextField();
        donorPhoneText.setOnAction(this::processAction);
        donorEmailText = new TextField();
        donorEmailText.setOnAction(this::processAction);

        grid.add(donorFirstNameText, 1, 9);
        grid.add(donorLastNameText, 1, 10);
        grid.add(donorPhoneText, 1, 11);
        grid.add(donorEmailText, 1, 12);
        // =================================================================
        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new PccButton("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnMouseEntered(me ->
        {
        	submitButton.setScaleX(1.1);
        	submitButton.setScaleY(1.1);
        });

        submitButton.setOnMouseExited(me ->
        {
        	submitButton.setScaleX(1);
        	submitButton.setScaleY(1);
        });

        submitButton.setOnMousePressed(me ->
    {
    	submitButton.setScaleX(0.9);
    	submitButton.setScaleY(0.9);
    });
        submitButton.setOnMouseReleased(me ->
    {
    	submitButton.setScaleX(1.1);
    	submitButton.setScaleY(1.1);
    });
        submitButton.setOnAction(this::processAction);
        doneCont.getChildren().add(submitButton);

        cancelButton = new PccButton("Return");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnMouseEntered(me ->
   	        {
   	        	cancelButton.setScaleX(1.1);
   	        	cancelButton.setScaleY(1.1);
   	        });

   	        cancelButton.setOnMouseExited(me ->
   	        {
   	        	cancelButton.setScaleX(1);
   	        	cancelButton.setScaleY(1);
   	        });

   	        cancelButton.setOnMousePressed(me ->
   	    {
   	    	cancelButton.setScaleX(0.9);
   	    	cancelButton.setScaleY(0.9);
   	    });
   	        cancelButton.setOnMouseReleased(me ->
   	    {
   	    	cancelButton.setScaleX(1.1);
   	    	cancelButton.setScaleY(1.1);
   	    });
        cancelButton.setOnAction(e -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelAddClothingItem", null);
        });
        doneCont.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }


    // Create the status log field
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    public void populateFields() {
        clearErrorMessage();
        genderCombo.setValue((String) myModel.getState("Gender"));
        Vector ArticleList = (Vector) myModel.getState("Articles");
        Iterator articles = ArticleList.iterator();
        ObservableList<ArticleType> articleTypes = FXCollections.observableArrayList();
        while (articles.hasNext()) {
            articleTypes.add((ArticleType) articles.next());
        }
        articleTypeCombo.setItems(articleTypes);

        Vector ColorList = (Vector) myModel.getState("Colors");
        Iterator colors = ColorList.iterator();
        ObservableList<Color> colorItems = FXCollections.observableArrayList();
        while (colors.hasNext()) {
            colorItems.add((Color) colors.next());
        }
        primaryColorCombo.setItems(colorItems);
        secondaryColorCombo.setItems(colorItems);
    }

    private void processAction(ActionEvent e) {
        clearErrorMessage();
        Properties props = new Properties();
        String gender = genderCombo.getValue();
        String size = sizeText.getText();
        ArticleType articleType = articleTypeCombo.getValue(); //.getState("ID");
        Color color1 = primaryColorCombo.getValue(); //.getState("ID");
        Color color2 = secondaryColorCombo.getValue();
        String brand = brandText.getText();
        String notes = notesText.getText();
        String donorFirstName = donorFirstNameText.getText();
        String donorLastName = donorLastNameText.getText();
        String donorPhone = donorPhoneText.getText();
        String donorEmail = donorEmailText.getText();

        Pattern emailValidation = Pattern.compile("^([\\w \\._]+\\<[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a" +
                "-z0-9](?:[a-z0-9-]*[a-z0-9])?\\>|[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%" +
                "&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-" +
                "]*[a-z0-9])?)$");
// TODO: Improve UI validation
        if (gender.length() == 0) {
            displayErrorMessage("ERROR: Please select gender!");
            genderCombo.requestFocus();
        } else if (size.length() == 0) {
            displayErrorMessage("ERROR: Please enter a size!");
            sizeText.requestFocus();
        } else if (articleType == null) {
            displayErrorMessage("ERROR: Please select an article type!");
            articleTypeCombo.requestFocus();
        } else if (color1 == null) {
            displayErrorMessage("ERROR: Please select a primary color!");
            primaryColorCombo.requestFocus();
        } else if ((donorEmail.length() > 0) &&
                (!emailValidation.matcher(donorEmail.toLowerCase()).matches())) {
            displayErrorMessage("Email not in name@address.domain format!");
            donorEmailText.requestFocus();
        } else {
            props.setProperty("Gender", gender);
            props.setProperty("Size", size);
            props.setProperty("ArticleType", (String) articleType.getState("ID"));
            props.setProperty("Color1", (String) color1.getState("ID"));
            if (color2 != null) {
                props.setProperty("Color2", (String) color2.getState("ID"));
            } else {
                props.setProperty("Color2", "0");
            }
            props.setProperty("Brand", brand);
            props.setProperty("Notes", notes);
            props.setProperty("DonorFirstName", donorFirstName);
            props.setProperty("DonorLastName", donorLastName);
            props.setProperty("DonorPhone", donorPhone);
            props.setProperty("DonorEmail", donorEmail);
            myModel.stateChangeRequest("ClothingItemData", props);

        }
    }

    /**
     * Update method
     */
    public void updateState(String key, Object value) {
        clearErrorMessage();

        if (key.equals("TransactionError")) {
            String val = (String) value;
            if (val.startsWith("ERR")) {
                displayErrorMessage(val);
            } else {
                displayMessage(val);
            }

        }
    }

    /**
     * Display error message
     */
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

}