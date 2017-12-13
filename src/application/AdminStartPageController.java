package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class AdminStartPageController {

    @FXML
    private Tab manageCostumer, manageBooks;

    @FXML
    private TextField addTitle, addStreet, addAuthor, costumerIdUpdate, addCustomerName, addCity,
    addPhoneNr, addISBN, addPublisher, addQuantity, addCardID, addGenre, addPages;


    @FXML
    private Button logOut;

 

    @FXML
    private TabPane adminManageTab;

  

    @FXML
    void logOut(ActionEvent event) {

    }

}
