package tucil1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class GUI extends Application {
    
    private TextArea inputArea;
    private GridPane boardGrid;
    private Label statusLabel;
    private Label iterationsLabel;
    private Label timeLabel;
    private Button LoadButton;
    private Button solveButton;
    private Button loadFileButton;
    private Button clearButton;
    private Spinner<Integer> sizeSpinner;
    private CheckBox liveUpdateCheckBox;
    private Spinner<Integer> updateIntervalSpinner;
    private int BoardSize = 9;
    private Timeline updateTimeline;
    private long starttime;
    
    private final Color[] colorlist = {
        Color.web("#FFB6C1"), 
        Color.web("#87CEEB"), 
        Color.web("#98FB98"), 
        Color.web("#FFD700"), 
        Color.web("#DDA0DD"), 
        Color.web("#F0E68C"), 
        Color.web("#FFA07A"), 
        Color.web("#E6E6FA"), 
        Color.web("#FFE4B5"),
        Color.web("#F08080"),
        Color.web("#AFEEEE"),
        Color.web("#DB7093"),
        Color.web("#FFDAB9"), 
        Color.web("#E0BBE4"),
        Color.web("#FFDEAD"), 
        Color.web("#D8BFD8"),
        Color.web("#FFB347"), 
        Color.web("#B0E0E6"),
        Color.web("#F5DEB3"),
        Color.web("#FFC0CB"),
        Color.web("#FFFACD"), 
        Color.web("#C8A2C8"),
        Color.web("#B19CD9"),
        Color.web("#FFB6D9"),
        Color.web("#ADD8E6"),
        Color.web("#FAFAD2")  
    };
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LinkedIn Queen Solver");        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        VBox topSection = createTopSection();
        root.setTop(topSection);
        VBox leftSection = createLeftSection();
        root.setLeft(leftSection);        
        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);        
        VBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();        
        initializeBoard(BoardSize);
    }
    
    private VBox createTopSection() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        
        Text title = new Text("LinkedIn Queen Solver");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 28));
        title.setFill(Color.web("#2C3E50"));
        

        vbox.getChildren().addAll(title);
        return vbox;
    }
    
    private VBox createLeftSection() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(10));
        vbox.setPrefWidth(300);
        
        HBox sizeBox = new HBox(10);
        sizeBox.setAlignment(Pos.CENTER_LEFT);
        Label sizeLabel = new Label("Board Size:");
        sizeLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
        sizeSpinner = new Spinner<>(3, 99, 9);
        sizeSpinner.setEditable(true);
        sizeSpinner.setPrefWidth(80);
        sizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            BoardSize = newVal;
            initializeBoard(BoardSize);
            inputArea.clear();
        });
        sizeBox.getChildren().addAll(sizeLabel, sizeSpinner);
        
        Label liveUpdateLabel = new Label("Live Update Settings:");
        liveUpdateLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
        
        liveUpdateCheckBox = new CheckBox("Show Live Updates");
        liveUpdateCheckBox.setSelected(true);
        liveUpdateCheckBox.setFont(Font.font("Times New Roman", 11));
        
        HBox intervalBox = new HBox(10);
        intervalBox.setAlignment(Pos.CENTER_LEFT);
        Label intervalLabel = new Label("Update Interval (ms):");
        intervalLabel.setFont(Font.font("Times New Roman", 11));
        updateIntervalSpinner = new Spinner<>(100, 10000, 500, 100);
        updateIntervalSpinner.setEditable(true);
        updateIntervalSpinner.setPrefWidth(100);
        updateIntervalSpinner.disableProperty().bind(liveUpdateCheckBox.selectedProperty().not());
        intervalBox.getChildren().addAll(intervalLabel, updateIntervalSpinner);
        
        Label inputLabel = new Label("Bentuk Board:");
        inputLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
        
        inputArea = new TextArea();
        inputArea.setPromptText("Enter board configuration (e.g., AAABBCCCD\r\n" + //
                        "ABBBBCECD\r\n" + //
                        "ABBBDCECD\r\n" + //
                        "AAABDCCCD\r\n" + //
                        "BBBBDDDDD\r\n" + //
                        "FGGGDDHDD\r\n" + //
                        "FGIGDDHDD\r\n" + //
                        "FGIGDDHDD\r\n" + //
                        "FGGGDDHHH\r\n" + //
                        ")\n");
        inputArea.setPrefHeight(200);
        inputArea.setFont(Font.font("Times New Roman", 12));
        
        LoadButton = new Button("Load");
        LoadButton.setMaxWidth(Double.MAX_VALUE);
        LoadButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        LoadButton.setOnAction(e -> loadConfig());

        loadFileButton = new Button("Load from File");
        loadFileButton.setMaxWidth(Double.MAX_VALUE);
        loadFileButton.setOnAction(e -> loadFromFile());
        
        solveButton = new Button("Solve");
        solveButton.setMaxWidth(Double.MAX_VALUE);
        solveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        solveButton.setOnAction(e -> solve());
        solveButton.setDisable(true);

        clearButton = new Button("Clear");
        clearButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setOnAction(e -> clearBoard());
        
        vbox.getChildren().addAll(
            sizeBox,
            new Separator(),
            liveUpdateLabel,
            liveUpdateCheckBox,
            intervalBox,
            new Separator(),
            inputLabel,
            inputArea,
            LoadButton,
            loadFileButton,
            solveButton,
            clearButton
        );
        
        return vbox;
    }
    
    private VBox createCenterSection() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        boardGrid.setStyle("-fx-background-color: #000000; -fx-padding: 5;");
        
        vbox.getChildren().addAll(boardGrid);
        return vbox;
    }
    
    private VBox createBottomSection() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        
        statusLabel = new Label("");
        statusLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 13));
        statusLabel.setTextFill(Color.web("#2C3E50"));
        
        iterationsLabel = new Label("Iterations: -");
        iterationsLabel.setFont(Font.font("Times New Roman", 12));
        
        timeLabel = new Label("Time: -");
        timeLabel.setFont(Font.font("Times New Roman", 12));
        
        vbox.getChildren().addAll(statusLabel, iterationsLabel, timeLabel);
        return vbox;
    }
    
    private void initializeBoard(int size) {
        boardGrid.getChildren().clear();
        
        double cellSize = Math.min(60, 600.0 / size);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                StackPane cell = new StackPane();
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.LIGHTGRAY);
                rect.setStroke(Color.DARKGRAY);
                rect.setStrokeWidth(1);
                
                cell.getChildren().add(rect);
                boardGrid.add(cell, j, i);
            }
        }
    }
    
    private void visualizeBoard(char[][][] solution) {
        if (solution == null) return;
        
        boardGrid.getChildren().clear();
        int size = solution.length;
        double cellSize = Math.min(60, 600.0 / size);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                StackPane cell = new StackPane();
                Rectangle rect = new Rectangle(cellSize, cellSize);
                
                char colorChar = solution[i][j][0];
                Color cellColor = getcolor(colorChar);
                rect.setFill(cellColor);
                rect.setStroke(Color.DARKGRAY);
                rect.setStrokeWidth(1);
                
                cell.getChildren().add(rect);
                
                if (solution[i][j][1] == 'Y') {
                    Text queen = new Text("♛");
                    queen.setFont(Font.font("Times New Roman", FontWeight.BOLD, cellSize * 0.6));
                    queen.setFill(Color.web("#000000"));
                    queen.setStroke(Color.GOLD);
                    queen.setStrokeWidth(1);
                    cell.getChildren().add(queen);
                }
                
                boardGrid.add(cell, j, i);
            }
        }
    }
    
    private Color getcolor(char c) {
        int index = Character.toUpperCase(c) - 'A';
        if (index >= 0 && index < colorlist.length) {
            return colorlist[index];
        }
        return Color.LIGHTGRAY;
    }
    
    private void visualizeInitialBoard(String[] config, int size) {
        boardGrid.getChildren().clear();
        double cellSize = Math.min(60, 600.0 / size);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                StackPane cell = new StackPane();
                Rectangle rect = new Rectangle(cellSize, cellSize);
                
                char colorChar = config[i].trim().charAt(j);
                Color cellColor = getcolor(colorChar);
                rect.setFill(cellColor);
                rect.setStroke(Color.DARKGRAY);
                rect.setStrokeWidth(1);
                
                cell.getChildren().add(rect);
                
                Text colorLabel = new Text(String.valueOf(colorChar));
                colorLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, cellSize * 0.3));
                colorLabel.setFill(Color.web("#2C3E50"));
                cell.getChildren().add(colorLabel);
                
                boardGrid.add(cell, j, i);
            }
        }
    }
    
    private void loadConfig() {
        String input = inputArea.getText().trim();
        if (input.isEmpty()) {
            showAlert("Input Required", "belum ada config board-nya njir");
            return;
        }
        
        String[] lines = input.split("\n");
        if (lines.length != BoardSize) {
            showAlert("Invalid Input", "jumlah barisnya salah");
            return;
        }
        
        for (String line : lines) {
            if (line.trim().length() != BoardSize) {
                showAlert("Invalid Input", "jumlah kolomnnya ada yang ga bener");
                return;
            }
        }
        
        visualizeInitialBoard(lines, BoardSize);
        statusLabel.setText("Ready to solve");
        statusLabel.setTextFill(Color.web("#3498db"));
        solveButton.setDisable(false);
    }
    
    private void solve() {
        String input = inputArea.getText().trim();
        if (input.isEmpty()) {
            showAlert("Input Required", "belum ada config board-nya njir");
            return;
        }
        String[] lines = input.split("\n");
        if (lines.length != BoardSize) {
            showAlert("Invalid Input", "jumlah barisnya salah");
            return;
        }
        for (String line : lines) {
            if (line.trim().length() != BoardSize) {
                showAlert("Invalid Input", "jumlah kolomnnya ada yang ga bener");
                return;
            }
        }
        solveButton.setDisable(true);
        statusLabel.setText("Solving...");
        statusLabel.setTextFill(Color.web("#F39C12"));

        Bruteforce.iterasi = 0;
        Bruteforce.currentmap = null;
        starttime = System.nanoTime();
        
        if (liveUpdateCheckBox.isSelected()) {
            int intervalMs = updateIntervalSpinner.getValue();
            updateTimeline = new Timeline(new KeyFrame(Duration.millis((double) intervalMs), e -> {
                long currentIterations = Bruteforce.iterasi;
                double elapsedSeconds = (System.nanoTime() - starttime) / 1_000_000_000.0;            
                iterationsLabel.setText(String.format("Iterations: %,d", currentIterations));
                timeLabel.setText(String.format("Time: %.2f seconds", elapsedSeconds));            
                if (Bruteforce.currentmap != null) {
                    visualizeBoard(Bruteforce.copy(Bruteforce.currentmap));
                }
            }));
            updateTimeline.setCycleCount(Timeline.INDEFINITE);
            updateTimeline.play();
        }
        
        new Thread(() -> {
            char[][][] result = Bruteforce.solve(lines,BoardSize);
            long endtime = System.nanoTime();
            double timeSeconds = (endtime - starttime) / 1_000_000_000.0;
            
            Platform.runLater(() -> {
                if (updateTimeline != null) {
                    updateTimeline.stop();
                }
                
                if (result!=null) {
                    visualizeBoard((char[][][])result);
                    statusLabel.setText("Solution Found!");
                    statusLabel.setTextFill(Color.web("#27AE60"));
                } else {
                    statusLabel.setText("No Solution Found");
                    statusLabel.setTextFill(Color.web("#E74C3C"));
                }
                
                iterationsLabel.setText(String.format("Iterations: %,d", Bruteforce.iterasi));
                timeLabel.setText(String.format("Time: %.6f seconds", timeSeconds));
                solveButton.setDisable(false);
            });
        }).start();
    }
    
    private void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Board Configuration");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        File file = fileChooser.showOpenDialog(boardGrid.getScene().getWindow());
        if (file != null) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                if (!lines.isEmpty()) {
                    lines.removeIf(line -> line.trim().isEmpty());
                    if (lines.isEmpty()) {
                        showAlert("Error", "File is empty");
                        return;
                    }   
                    int size = lines.get(0).trim().length();
                    for (String line : lines) {
                        if (line.trim().length() != size) {
                            showAlert("Invalid File", "All rows must have the same length");
                            return;
                        }
                    }                    
                    sizeSpinner.getValueFactory().setValue(size);
                    StringBuilder sb = new StringBuilder();
                    for (String line : lines) {
                        sb.append(line.trim()).append("\n");
                    }
                    inputArea.setText(sb.toString().trim());
                    visualizeInitialBoard(lines.toArray(new String[0]), size);
                    solveButton.setDisable(false);

                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load file: " + e.getMessage());
            }
        }
    }
    
    private void clearBoard() {
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
        
        inputArea.clear();
        initializeBoard(BoardSize);
        statusLabel.setText("Ready to solve");
        statusLabel.setTextFill(Color.web("#2C3E50"));
        iterationsLabel.setText("Iterations: -");
        timeLabel.setText("Time: -");
        solveButton.setDisable(true);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
