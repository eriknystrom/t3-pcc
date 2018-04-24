// specify the package
package model;

// system imports
import Utilities.Utilities;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.Calendar;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

import javax.rmi.CORBA.Util;


public class FulfilRequestTransaction extends Transaction
{

    private RequestCollection myRequestCollection;
    private ClothingRequest myClothingRequest;
    private ClothingItem myClothingItem;
    private ClothingItemCollection myClothingCollection;

    private ArticleTypeCollection myArticleTypeList;
    private ColorCollection myColorList;
    private String gender;

    private String transactionErrorMessage = "";


    //----------------------------------------------------------
    public FulfilRequestTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelRequest", "CancelTransaction");
        dependencies.setProperty("CancelClothingItemList", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("ProcessRequest", "TransactionError");
        dependencies.setProperty("ClothingRequestData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    //-----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        myRequestCollection = new RequestCollection();
        myRequestCollection.findAll();

        try
        {
            Scene newScene = createRequestCollectionView();
            swapToView(newScene);
        }
        catch (Exception ex)
        {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating ColorCollectionView", Event.ERROR);
        }
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        if ((key.equals("DoYourJob") == true) || (key.equals("CancelTransaction") == true))
        {
            doYourJob();
        }
        else
        if (key.equals("SearchRequest") == true)
        {
            processTransaction((Properties)value);
        }
        else
        if (key.equals("ProcessRequest") == true)
        {
            myClothingItem.stateChangeRequest("Status", "Received");
            myClothingRequest.stateChangeRequest("Status", "Fulfilled");
            Date date = new Date();
            String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            myClothingItem.stateChangeRequest("DateTaken", modifiedDate);
            myClothingRequest.stateChangeRequest("RequestFulfilledDate", modifiedDate);
            myClothingItem.stateChangeRequest("ReceveiverNetid", (String) myClothingRequest.getState("RequesterNetid"));
            myClothingItem.stateChangeRequest("ReceveiverFirstName", (String) myClothingRequest.getState("RequesterFirstName"));
            myClothingItem.stateChangeRequest("ReceveiverLastName", (String) myClothingRequest.getState("RequesterLastName"));
            myClothingItem.update();
            myClothingRequest.update();
            transactionErrorMessage = "Request has been fulfilled";
        }
        if(key.equals("ClothingItemSelected") == true)
        {
            try {
                myClothingItem = myClothingCollection.retrieve((String) value);

                Scene newScene = createFulfilView();

                swapToView(newScene);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (key.equals("RequestSelected") == true)
        {
            myClothingRequest = myRequestCollection.retrieve((String) value);
            myClothingCollection = new ClothingItemCollection();
            myClothingCollection.findDonatedCriteria((String) myClothingRequest.getState("RequestedArticleType"),
                                                     (String) myClothingRequest.getState("RequestedGender"),
                                                     (String) myClothingRequest.getState("RequestedSize"));
            try
            {

                Scene newScene = createInventoryCollectionView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating Inventory View", Event.ERROR);
            }
        }
        else
        if (key.equals("RequestData") == true)
        {
            myClothingRequest = myRequestCollection.retrieve((String) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("ClothingItemList") == true) {
            return myClothingCollection;
        }
        if (key.equals("RequestList") == true)
        {
            return myRequestCollection;
        }
        if (key.equals("ClothingItem") == true)
        {
            return myClothingItem;
        }
        if (key.equals("ClothingRequest") == true)
        {
            return myClothingRequest;
        }
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        return null;
    }

    //-------------------------------------------------------------
    protected Scene createView()
    {
        // placed it here since the collection is drawn from step 2 of the use case
        myRequestCollection = new RequestCollection();
        myRequestCollection.findAll();

        Scene currentScene = myViews.get("RequestCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("RequestCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("RequestCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //-------------------------------------------------------------
    protected Scene createFulfilView()
    {
        View newView = ViewFactory.createView("FulfillRequestView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //-------------------------------------------------------------
    protected Scene createRequestCollectionView()
    {
        View newView = ViewFactory.createView("RequestCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //-------------------------------------------------------------
    protected Scene createInventoryCollectionView()
    {
        View newView = ViewFactory.createView("ClothingItemCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //---------------------------------------------------------------
    protected Scene createFulfillRequestTractionView()
    {
        return null;
    }

    /*
    //----------------------------------------------------------------
    private void processFulfillRequestTransaction(Properties props)
    {
        //--
        String receiverNetid = props.getProperty("ReceiverNetid");
        String receiverFirstName = props.getProperty("ReceiverFirstName");
        String receiverLastName = props.getProperty("ReceiverLastName");

        myClothingItem.stateChangeRequest("ReceiverNetid", receiverNetid);
        myClothingItem.stateChangeRequest("ReceiverFirstName", receiverFirstName);
        myClothingItem.stateChangeRequest("ReceiverLastName", receiverLastName);
        myClothingItem.stateChangeRequest("Status", "Received");

        Calendar currDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = dateFormat.format(currDate.getTime());
        myClothingItem.stateChangeRequest("DateTaken", date);

        //---
        if(myClothingRequest != null) {
            myClothingRequest.stateChangeRequest("Status", "Received");
            myClothingRequest.update();
            transactionErrorMessage = (String)myClothingRequest.getState("UpdateStatusMessage");
        }
    }*/

}
