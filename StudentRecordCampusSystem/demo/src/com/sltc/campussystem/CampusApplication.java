package com.sltc.campussystem;

import com.sltc.campussystem.exceptions.DuplicateLocationException;
import com.sltc.campussystem.exceptions.InvalidConnectionException;
import com.sltc.campussystem.exceptions.LocationNotFoundException;
import com.sltc.campussystem.model.Action;
import com.sltc.campussystem.model.ServiceRequest;
import com.sltc.campussystem.model.StudentRecord;
import com.sltc.campussystem.structures.ActionStack;
import com.sltc.campussystem.structures.CampusGraph;
import com.sltc.campussystem.structures.ServiceQueue;
import com.sltc.campussystem.structures.StudentAVLTree;
import com.sltc.campussystem.structures.StudentHashTable;
import com.sltc.campussystem.structures.StudentLinkedList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CampusApplication extends Application {

    private final StudentLinkedList studentList = new StudentLinkedList();
    private final StudentAVLTree studentTree = new StudentAVLTree();
    private final StudentHashTable studentHashTable = new StudentHashTable();
    private final ActionStack actionStack = new ActionStack();
    private final ServiceQueue serviceQueue = new ServiceQueue();
    private final CampusGraph campusGraph = new CampusGraph();

    private final ObservableList<StudentRecord> studentRows = FXCollections.observableArrayList();
    private final ObservableList<ServiceRequest> requestRows = FXCollections.observableArrayList();
    private final ObservableList<Action> actionRows = FXCollections.observableArrayList();
    private final ObservableList<String> locationRows = FXCollections.observableArrayList();
    private int nextRequestId = 1;

    private TableView<StudentRecord> studentTable;
    private TableView<ServiceRequest> requestTable;
    private TableView<Action> actionTable;
    private ListView<String> networkView;
    private ComboBox<String> traversalStart;

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane(
                new Tab("Student Records", createStudentView()),
                new Tab("Requests & History", createServiceView()),
                new Tab("Campus Network", createCampusView()));
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Label title = new Label("Student Record Campus System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 18px;");
        BorderPane root = new BorderPane(tabs);
        root.setTop(title);
        root.setStyle("-fx-background-color: #f4f7fb;");

        stage.setTitle("Student Record Campus System");
        stage.setScene(new Scene(root, 1100, 720));
        stage.show();
    }

    private VBox createStudentView() {
        TextField id = new TextField();
        TextField name = new TextField();
        TextField programme = new TextField();
        TextField marks = new TextField();
        id.setPromptText("Student ID");
        name.setPromptText("Name");
        programme.setPromptText("Programme");
        marks.setPromptText("Marks (0-100)");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(16));
        form.addRow(0, new Label("Student ID"), id, new Label("Name"), name);
        form.addRow(1, new Label("Programme"), programme, new Label("Marks"), marks);

        Button add = new Button("Add");
        Button update = new Button("Update");
        Button delete = new Button("Delete");
        Button search = new Button("Search");
        Button clear = new Button("Clear");
        HBox actions = new HBox(8, add, update, delete, search, clear);
        actions.setPadding(new Insets(0, 16, 16, 16));

        studentTable = new TableView<>(studentRows);
        studentTable.getColumns().add(studentColumn("ID", "studentId", 150));
        studentTable.getColumns().add(studentColumn("Name", "name", 250));
        studentTable.getColumns().add(studentColumn("Programme", "programme", 280));
        TableColumn<StudentRecord, String> marksColumn = new TableColumn<>("Marks");
        marksColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f", cell.getValue().getMarks())));
        marksColumn.setPrefWidth(100);
        studentTable.getColumns().add(marksColumn);
        studentTable.setPlaceholder(new Label("No student records yet"));
        VBox.setVgrow(studentTable, Priority.ALWAYS);

        add.setOnAction(event -> addStudent(id, name, programme, marks));
        update.setOnAction(event -> updateStudent(id, name, programme, marks));
        delete.setOnAction(event -> deleteStudent(id));
        search.setOnAction(event -> searchStudent(id));
        clear.setOnAction(event -> clearFields(id, name, programme, marks));
        studentTable.setOnMouseClicked(event -> {
            StudentRecord selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                id.setText(selected.getStudentId());
                name.setText(selected.getName());
                programme.setText(selected.getProgramme());
                marks.setText(String.valueOf(selected.getMarks()));
            }
        });

        refreshStudents();
        return new VBox(form, actions, studentTable);
    }

    private VBox createServiceView() {
        TextField studentId = new TextField();
        TextField description = new TextField();
        studentId.setPromptText("Student ID");
        description.setPromptText("Request description");
        HBox form = new HBox(10, studentId, description);
        form.setPadding(new Insets(16));
        HBox.setHgrow(description, Priority.ALWAYS);

        Button add = new Button("Add Request");
        Button process = new Button("Process Next");
        HBox requestActions = new HBox(8, add, process);
        requestActions.setPadding(new Insets(0, 16, 12, 16));

        requestTable = new TableView<>(requestRows);
        TableColumn<ServiceRequest, String> requestIdColumn = new TableColumn<>("Request");
        requestIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRequestId())));
        requestIdColumn.setPrefWidth(90);
        requestTable.getColumns().add(requestIdColumn);
        requestTable.getColumns().add(requestColumn("Student ID", "studentId", 130));
        requestTable.getColumns().add(requestColumn("Description", "description", 400));
        TableColumn<ServiceRequest, String> requestTime = new TableColumn<>("Created");
        requestTime.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTimestamp().toString()));
        requestTime.setPrefWidth(220);
        requestTable.getColumns().add(requestTime);
        requestTable.setPlaceholder(new Label("No pending requests"));

        actionTable = new TableView<>(actionRows);
        TableColumn<Action, String> actionType = new TableColumn<>("Action");
        actionType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType().name()));
        actionType.setPrefWidth(100);
        actionTable.getColumns().add(actionType);
        TableColumn<Action, String> actionStudentId = new TableColumn<>("Student ID");
        actionStudentId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSnapshot().getStudentId()));
        actionStudentId.setPrefWidth(130);
        actionTable.getColumns().add(actionStudentId);
        TableColumn<Action, String> actionStudentName = new TableColumn<>("Student Name");
        actionStudentName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSnapshot().getName()));
        actionStudentName.setPrefWidth(220);
        actionTable.getColumns().add(actionStudentName);
        TableColumn<Action, String> actionTime = new TableColumn<>("Time");
        actionTime.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTimestamp().toString()));
        actionTime.setPrefWidth(220);
        actionTable.getColumns().add(actionTime);
        actionTable.setPlaceholder(new Label("No student actions yet"));
        VBox.setVgrow(requestTable, Priority.ALWAYS);
        VBox.setVgrow(actionTable, Priority.ALWAYS);

        add.setOnAction(event -> addRequest(studentId, description));
        process.setOnAction(event -> processRequest());
        refreshRequests();
        refreshActions();

        Label queueTitle = new Label("Pending Service Requests (FIFO)");
        queueTitle.setStyle("-fx-font-weight: bold;");
        Label historyTitle = new Label("Recent Student Actions (LIFO)");
        historyTitle.setStyle("-fx-font-weight: bold;");
        VBox queue = new VBox(queueTitle, requestTable);
        VBox history = new VBox(historyTitle, actionTable);
        VBox.setVgrow(queue, Priority.ALWAYS);
        VBox.setVgrow(history, Priority.ALWAYS);
        return new VBox(form, requestActions, queue, history);
    }

    private VBox createCampusView() {
        TextField location = new TextField();
        TextField from = new TextField();
        TextField to = new TextField();
        location.setPromptText("New location");
        from.setPromptText("From");
        to.setPromptText("To");
        Button addLocation = new Button("Add Location");
        Button removeLocation = new Button("Remove Location");
        Button addConnection = new Button("Add Connection");
        Button removeConnection = new Button("Remove Connection");
        HBox controls = new HBox(8, location, addLocation, removeLocation, from, to, addConnection, removeConnection);
        controls.setPadding(new Insets(16));

        traversalStart = new ComboBox<>(locationRows);
        traversalStart.setPromptText("Start location");
        ComboBox<String> mode = new ComboBox<>(FXCollections.observableArrayList("BFS", "DFS"));
        mode.getSelectionModel().selectFirst();
        Button traverse = new Button("Traverse");
        HBox traversal = new HBox(8, new Label("Traversal:"), traversalStart, mode, traverse);
        traversal.setPadding(new Insets(0, 16, 12, 16));

        networkView = new ListView<>();
        VBox.setVgrow(networkView, Priority.ALWAYS);
        addLocation.setOnAction(event -> addLocation(location));
        removeLocation.setOnAction(event -> removeLocation(location));
        addConnection.setOnAction(event -> changeConnection(from, to, true));
        removeConnection.setOnAction(event -> changeConnection(from, to, false));
        traverse.setOnAction(event -> traverseCampus(mode));

        refreshNetwork();
        return new VBox(controls, traversal, networkView);
    }

    private TableColumn<StudentRecord, String> studentColumn(String title, String property, int width) {
        TableColumn<StudentRecord, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private TableColumn<ServiceRequest, String> requestColumn(String title, String property, int width) {
        TableColumn<ServiceRequest, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private TableColumn<Action, String> actionColumn(String title, String property, int width) {
        TableColumn<Action, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private void addStudent(TextField id, TextField name, TextField programme, TextField marks) {
        try {
            requireText(id, "Student ID");
            requireText(name, "Name");
            requireText(programme, "Programme");
            double value = parseMarks(marks.getText());
            if (studentHashTable.get(id.getText().trim()) != null) {
                throw new IllegalArgumentException("That student ID already exists.");
            }
            StudentRecord record = new StudentRecord(id.getText().trim(), name.getText().trim(), programme.getText().trim(), value);
            studentList.add(record);
            studentTree.insert(record);
            studentHashTable.put(record.getStudentId(), record);
            actionStack.push(new Action(Action.Type.ADD, record.copy()));
            refreshStudents();
            refreshActions();
            clearFields(id, name, programme, marks);
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void updateStudent(TextField id, TextField name, TextField programme, TextField marks) {
        try {
            requireText(id, "Student ID");
            StudentRecord record = studentHashTable.get(id.getText().trim());
            if (record == null) {
                throw new IllegalArgumentException("Student ID was not found.");
            }
            Double newMarks = marks.getText().isBlank() ? null : parseMarks(marks.getText());
            studentList.update(id.getText().trim(), name.getText(), programme.getText(), newMarks);
            actionStack.push(new Action(Action.Type.UPDATE, record.copy()));
            refreshStudents();
            refreshActions();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteStudent(TextField id) {
        try {
            requireText(id, "Student ID");
            StudentRecord removed = studentList.delete(id.getText().trim());
            if (removed == null) {
                throw new IllegalArgumentException("Student ID was not found.");
            }
            studentTree.delete(removed.getStudentId());
            studentHashTable.remove(removed.getStudentId());
            actionStack.push(new Action(Action.Type.DELETE, removed.copy()));
            refreshStudents();
            refreshActions();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void searchStudent(TextField id) {
        StudentRecord record = studentHashTable.get(id.getText().trim());
        if (record == null) {
            showError("Student ID was not found.");
            return;
        }
        studentTable.getSelectionModel().select(record);
        studentTable.scrollTo(record);
    }

    private void addRequest(TextField studentId, TextField description) {
        try {
            requireText(studentId, "Student ID");
            requireText(description, "Description");
            serviceQueue.enqueue(new ServiceRequest(nextRequestId++, studentId.getText().trim(), description.getText().trim()));
            refreshRequests();
            studentId.clear();
            description.clear();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void processRequest() {
        ServiceRequest request = serviceQueue.dequeue();
        if (request == null) {
            showError("There are no pending service requests.");
        } else {
            refreshRequests();
            showInfo("Processed request #" + request.getRequestId() + ".");
        }
    }

    private void addLocation(TextField field) {
        try {
            requireText(field, "Location");
            campusGraph.addLocation(field.getText().trim());
            field.clear();
            refreshNetwork();
        } catch (DuplicateLocationException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void removeLocation(TextField field) {
        try {
            requireText(field, "Location");
            campusGraph.removeLocation(field.getText().trim());
            field.clear();
            refreshNetwork();
        } catch (LocationNotFoundException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void changeConnection(TextField from, TextField to, boolean add) {
        try {
            requireText(from, "From location");
            requireText(to, "To location");
            if (add) {
                campusGraph.addConnection(from.getText(), to.getText());
            } else {
                campusGraph.removeConnection(from.getText(), to.getText());
            }
            refreshNetwork();
        } catch (LocationNotFoundException | InvalidConnectionException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void traverseCampus(ComboBox<String> mode) {
        String start = traversalStart.getValue();
        if (start == null) {
            showError("Select a start location first.");
            return;
        }
        try {
            String result = mode.getValue().equals("BFS")
                    ? String.join(" -> ", campusGraph.bfs(start))
                    : String.join(" -> ", campusGraph.dfs(start));
            showInfo(mode.getValue() + " traversal: " + result);
        } catch (LocationNotFoundException exception) {
            showError(exception.getMessage());
        }
    }

    private void refreshStudents() {
        studentRows.setAll(studentList.toList());
    }

    private void refreshRequests() {
        requestRows.setAll(serviceQueue.toList());
    }

    private void refreshActions() {
        actionRows.setAll(actionStack.toList());
    }

    private void refreshNetwork() {
        if (networkView != null) {
            networkView.getItems().setAll(campusGraph.networkLines());
        }
        locationRows.setAll(campusGraph.locations());
        if (traversalStart != null && !locationRows.isEmpty() && traversalStart.getValue() == null) {
            traversalStart.getSelectionModel().selectFirst();
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void requireText(TextField field, String label) {
        if (field.getText() == null || field.getText().isBlank()) {
            throw new IllegalArgumentException(label + " is required.");
        }
    }

    private double parseMarks(String text) {
        try {
            double value = Double.parseDouble(text.trim());
            if (value < 0 || value > 100) {
                throw new IllegalArgumentException("Marks must be between 0 and 100.");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Marks must be a number.");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}