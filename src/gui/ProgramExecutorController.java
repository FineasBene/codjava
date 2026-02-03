package gui;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramExecutorController {
    private Controller controller;

    @FXML
    private TextField numberOfProgramStatesTextField;

    @FXML
    private TableView<Map.Entry<Integer, Value>> heapTableView;
    @FXML
    private TableColumn<Map.Entry<Integer, Value>, Integer> addressColumn;
    @FXML
    private TableColumn<Map.Entry<Integer, Value>, String> valueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> programStateIdentifiersListView;

    @FXML
    private TableView<Map.Entry<String, Value>> symbolTableView;
    @FXML
    private TableColumn<Map.Entry<String, Value>, String> variableNameColumn;
    @FXML
    private TableColumn<Map.Entry<String, Value>, String> variableValueColumn;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private Button runOneStepButton;

    public void setController(Controller controller) {
        this.controller = controller;
        populate();
    }

    @FXML
    public void initialize() {
        programStateIdentifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        // Cand dam click pe un ID de program, actualizam tabelele specifice (SymTable, ExeStack)
        programStateIdentifiersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            populateSymTableAndExeStack();
        });
    }

    @FXML
    public void runOneStep() {
        if (controller == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No program selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            if(controller.getRepository().getPrgList().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing left to execute", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            controller.executeOneStep();
            populate();

        } catch (InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void populate() {
        populateHeap();
        populateProgramStateIdentifiers();
        populateFileTable();
        populateOutput();
        populateSymTableAndExeStack();

        numberOfProgramStatesTextField.setText(String.valueOf(controller.getRepository().getPrgList().size()));
    }

    private void populateHeap() {
        if (controller.getRepository().getPrgList().isEmpty()) return;
        // Heap-ul e partajat, il luam de la primul
        Map<Integer, Value> heap = controller.getRepository().getPrgList().get(0).getHeap().getContent();
        heapTableView.setItems(FXCollections.observableArrayList(heap.entrySet()));
    }

    private void populateProgramStateIdentifiers() {
        List<ProgramState> programStates = controller.getRepository().getPrgList();
        List<Integer> ids = programStates.stream().map(ProgramState::getId).collect(Collectors.toList());
        programStateIdentifiersListView.setItems(FXCollections.observableArrayList(ids));
        programStateIdentifiersListView.refresh();
    }

    private void populateFileTable() {
         if (controller.getRepository().getPrgList().isEmpty()) return;
         // FileTable e partajat
         Map<model.value.StringValue, java.io.BufferedReader> fileTable = controller.getRepository().getPrgList().get(0).getFileTable().getContent();
         List<String> files = new ArrayList<>();
         for(model.value.StringValue key : fileTable.keySet()){
             files.add(key.toString());
         }
         fileTableListView.setItems(FXCollections.observableArrayList(files));
    }

    private void populateOutput() {
        if (controller.getRepository().getPrgList().isEmpty()) return;
        // Out e partajat
        List<Value> out = controller.getRepository().getPrgList().get(0).out().getList();
        List<String> outStr = out.stream().map(Value::toString).collect(Collectors.toList());
        outputListView.setItems(FXCollections.observableArrayList(outStr));
    }

    private void populateSymTableAndExeStack() {
        Integer selectedId = programStateIdentifiersListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) {
            // Daca nu e nimic selectat, selectam primul default (daca exista)
            if (!programStateIdentifiersListView.getItems().isEmpty()) {
                programStateIdentifiersListView.getSelectionModel().select(0);
                selectedId = programStateIdentifiersListView.getSelectionModel().getSelectedItem();
            } else {
                symbolTableView.getItems().clear();
                executionStackListView.getItems().clear();
                return;
            }
        }

        ProgramState selectedPrg = null;
        for (ProgramState prg : controller.getRepository().getPrgList()) {
            if (prg.getId() == selectedId) {
                selectedPrg = prg;
                break;
            }
        }

        if (selectedPrg != null) {
            // Populate SymTable
            Map<String, Value> symTable = selectedPrg.symbolTable().getContent();
            symbolTableView.setItems(FXCollections.observableArrayList(symTable.entrySet()));

            // Populate ExeStack
            List<Statement> stack = new ArrayList<>();
            for(Statement s : selectedPrg.executionStack().getStack()){
                stack.add(s);
            }
            // Afisam de sus in jos (reverse) sau cum vine din stack
            // De obicei stack-ul e afisat cu varful sus.
            List<String> stackStr = new ArrayList<>();
            // Iteram invers pentru vizualizare tip stiva
            for(int i=stack.size()-1; i>=0; i--){
                stackStr.add(stack.get(i).toString());
            }
            executionStackListView.setItems(FXCollections.observableArrayList(stackStr));
        }
    }
}
