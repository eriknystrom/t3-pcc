package userinterface;

// system imports
import Utilities.UiConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;
//import model.Item;
import model.ClothingItem;
import model.InventoryItemCollection;

//==============================================================================
public class CheckoutHelperView extends View
{
    protected TableView<InventoryTableModel> InventoryTable;
    protected PccButton addAnotherBarcodeButton;
    protected PccButton checkoutButton;
    protected PccButton cancelButton;

    protected TextField netId;
    protected TextField fName;
    protected TextField lName;
//    protected Button submitButton;

    protected MessageView statusLog;


    //--------------------------------------------------------------------------
    public CheckoutHelperView(IModel inv)
    {
        super(inv, "InventoryItemCollectionView");

        // create a container for showing the contents
        VBox container = getParentContainer();

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        // Error message area
        String errorMessage = "";
        container.getChildren().add(createStatusLog(""));
        displayErrorMessage(errorMessage);

        getChildren().add(container);

        populateFields();
    }


    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {
        ObservableList<InventoryTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            InventoryItemCollection inventoryItemCollection =
                    (InventoryItemCollection)myModel.getState("InventoryList");

            Vector entryList = (Vector)inventoryItemCollection.getState("InventoryItems");

            if (entryList.size() > 0)
            {
                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() )
                {
                    ClothingItem nextCI = (ClothingItem)entries.nextElement();
                    Vector<String> view = nextCI.getEntryListView();

                    // add this list entry to the list
                    InventoryTableModel nextTableRowData = new InventoryTableModel(view);
                    tableData.add(nextTableRowData);

                }
            }
            else
            {
//                displayMessage("No matching entries found!");
            }

            InventoryTable.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
    }

    @Override
    protected String getActionText() {
        return "** Checkout Clothing Items **";
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        Text prompt = new Text("Active Cart");
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

        InventoryTable = new TableView<InventoryTableModel>();
        InventoryTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn barcodeColumn = new TableColumn("Barcode") ;
        barcodeColumn.setMinWidth(50);
        barcodeColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("barcode"));

        TableColumn genderColumn = new TableColumn("Gender") ;
        genderColumn.setMinWidth(150);
        genderColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("gender"));

        TableColumn sizeColumn = new TableColumn("Size") ;
        sizeColumn.setMinWidth(50);
        sizeColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("size"));

        TableColumn articleTypeColumn = new TableColumn("Article Type") ;
        articleTypeColumn.setMinWidth(50);
        articleTypeColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("articleType"));

        TableColumn color1Column = new TableColumn("Color1") ;
        color1Column.setMinWidth(50);
        color1Column.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("color1"));

        TableColumn color2Column = new TableColumn("Color2") ;
        color2Column.setMinWidth(50);
        color2Column.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("color2"));

        TableColumn brandColumn = new TableColumn("Brand") ;
        brandColumn.setMinWidth(50);
        brandColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("brand"));

        TableColumn notesColumn = new TableColumn("Notes") ;
        notesColumn.setMinWidth(50);
        notesColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("notes"));

        TableColumn statusColumn = new TableColumn("Status") ;
        statusColumn.setMinWidth(50);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("status"));

        TableColumn donorFirstNameColumn = new TableColumn("Donor First Name") ;
        donorFirstNameColumn.setMinWidth(50);
        donorFirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("donorFirstName"));

        TableColumn donorLastNameColumn = new TableColumn("Donor Last Name") ;
        donorLastNameColumn.setMinWidth(50);
        donorLastNameColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("donorLastName"));

        TableColumn donorPhoneColumn = new TableColumn("Donor Phone") ;
        donorPhoneColumn.setMinWidth(50);
        donorPhoneColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("donorPhone"));

        TableColumn donorEmailColumn = new TableColumn("Donor Email") ;
        donorEmailColumn.setMinWidth(50);
        donorEmailColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("donorEmail"));

        TableColumn receiverNetIdColumn = new TableColumn("Receiver Net ID") ;
        receiverNetIdColumn.setMinWidth(50);
        receiverNetIdColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("receiverNetId"));

        TableColumn receiverFirstNameColumn = new TableColumn("Receiver First Name") ;
        receiverFirstNameColumn.setMinWidth(50);
        receiverFirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("receiverFirstName"));

        TableColumn receiverLastNameColumn = new TableColumn("Receiver Last Name") ;
        receiverLastNameColumn.setMinWidth(50);
        receiverLastNameColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("receiverLastName"));

        TableColumn dateDonatedColumn = new TableColumn("Date Donated") ;
        dateDonatedColumn.setMinWidth(50);
        dateDonatedColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("dateDonated"));

        TableColumn dateTakenColumn = new TableColumn("Date Taken") ;
        dateTakenColumn.setMinWidth(50);
        dateTakenColumn.setCellValueFactory(
                new PropertyValueFactory<InventoryTableModel, String>("dateTaken"));


        InventoryTable.getColumns().addAll(barcodeColumn, genderColumn, sizeColumn,
                articleTypeColumn, color1Column, color2Column, brandColumn, notesColumn, statusColumn,
                donorLastNameColumn, donorFirstNameColumn, donorPhoneColumn, donorEmailColumn,
                receiverNetIdColumn, receiverLastNameColumn, receiverFirstNameColumn,
                dateDonatedColumn, dateTakenColumn);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(150, 150);
        scrollPane.setContent(InventoryTable);

        Text netIdLabel = new Text(" Net ID : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        netIdLabel.setFont(myFont);
        netIdLabel.setWrappingWidth(150);
        netIdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(netIdLabel, 0, 1);
        netId = new TextField();
        grid.add(netId, 1, 1);

        Text fNameLabel = new Text(" First Name : ");
        fNameLabel.setFont(myFont);
        fNameLabel.setWrappingWidth(150);
        fNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(fNameLabel, 0, 2);
        fName = new TextField();
        grid.add(fName, 1, 2);

        Text lNameLabel = new Text(" Last Name : ");
        lNameLabel.setFont(myFont);
        lNameLabel.setWrappingWidth(150);
        lNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lNameLabel, 0, 3);
        lName = new TextField();
        grid.add(lName, 1, 3);

        VBox doneCont = new VBox(10);
        doneCont.setAlignment(Pos.CENTER);
        addAnotherBarcodeButton = new PccButton("Add Another Barcode");
        addAnotherBarcodeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addAnotherBarcodeButton.setPrefSize(250, 20);
        addAnotherBarcodeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("MoreData", null);

            }
        });
        doneCont.getChildren().add(addAnotherBarcodeButton);

        checkoutButton = new PccButton("Checkout Items");
        checkoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        checkoutButton.setPrefSize(250, 20);
        checkoutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e)
            {
                clearErrorMessage();
                Properties props = new Properties();
                String netIdReceiver = netId.getText();
                //TODO should netid have a maximum?
                if (netIdReceiver.length() > 0)
                {
                    props.setProperty("ReceiverNetid", netIdReceiver);
                    String fNameReceiver = fName.getText();
                    if (fNameReceiver.length() > 0 && fNameReceiver.length() < UiConstants.RECEIVER_FIRST_NAME_MAX_LENGTH)
                    {
                        props.setProperty("ReceiverFirstName", fNameReceiver);
                        String lNameReceiver = lName.getText();
                        if (lNameReceiver.length() > 0 && lNameReceiver.length() < UiConstants.RECEIVER_LAST_NAME_MAX_LENGTH)
                        {
                            props.setProperty("ReceiverLastName", lNameReceiver);
                            myModel.stateChangeRequest("ReceiverData", props);
                        }
                        else
                        {
                            displayErrorMessage("Last name incorrect size!");
                        }
                    }
                    else
                    {
                        displayErrorMessage("First name incorrect size!");
                    }
                }
                else
                {
                    displayErrorMessage("NetId incorrect size!");
                }
            }
        });
        doneCont.getChildren().add(checkoutButton);


        doneCont.setAlignment(Pos.CENTER);
        cancelButton = new PccButton("Done");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setPrefSize(250, 20);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelCheckoutCI", null);
            }
        });
        doneCont.getChildren().add(cancelButton);

        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }

    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {

    }
    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
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

    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }


}


