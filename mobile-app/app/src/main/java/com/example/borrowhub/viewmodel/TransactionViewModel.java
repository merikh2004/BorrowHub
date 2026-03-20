package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionViewModel extends AndroidViewModel {

    // Simulated student database (student number → {name, course})
    private static final Map<String, String[]> STUDENT_DATABASE = new HashMap<>();

    // Item types available for borrowing
    public static final String[] ITEM_TYPES = {"Equipment", "Laptop"};

    // Items available per type
    public static final Map<String, String[]> ITEMS_BY_TYPE = new HashMap<>();

    static {
        STUDENT_DATABASE.put("2024-12345", new String[]{"Sarah Chen", "BS Computer Science"});
        STUDENT_DATABASE.put("2024-23456", new String[]{"Emily Rodriguez", "BS Information Technology"});
        STUDENT_DATABASE.put("2024-34567", new String[]{"Mark Santos", "BS Computer Engineering"});

        ITEMS_BY_TYPE.put("Equipment", new String[]{
                "Projector - Epson EB-X41",
                "Camera - Canon EOS R6",
                "Conference Microphone",
                "Extension Cable 10m",
                "Wireless Presenter",
                "Tripod Stand",
                "HDMI Cable 5m"
        });
        ITEMS_BY_TYPE.put("Laptop", new String[]{
                "Laptop - Dell XPS 15",
                "Laptop - MacBook Pro 16",
                "Laptop - Lenovo ThinkPad"
        });
    }

    // Auto-fill fields (populated from student lookup)
    private final MutableLiveData<String> studentName = new MutableLiveData<>("");
    private final MutableLiveData<String> course = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> studentFound = new MutableLiveData<>(false);

    // Dynamic item rows
    private final MutableLiveData<List<ItemRow>> itemRows = new MutableLiveData<>(new ArrayList<>());

    // Submission state
    private final MutableLiveData<Boolean> submitted = new MutableLiveData<>(false);
    private final MutableLiveData<String> submitError = new MutableLiveData<>(null);

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        List<ItemRow> initial = new ArrayList<>();
        initial.add(new ItemRow());
        itemRows.setValue(initial);
    }

    // --- Getters ---

    public LiveData<String> getStudentName() {
        return studentName;
    }

    public LiveData<String> getCourse() {
        return course;
    }

    public LiveData<Boolean> isStudentFound() {
        return studentFound;
    }

    public LiveData<List<ItemRow>> getItemRows() {
        return itemRows;
    }

    public LiveData<Boolean> isSubmitted() {
        return submitted;
    }

    public LiveData<String> getSubmitError() {
        return submitError;
    }

    // --- Student lookup ---

    public void lookupStudent(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            studentName.setValue("");
            course.setValue("");
            studentFound.setValue(false);
            return;
        }
        String[] info = STUDENT_DATABASE.get(studentNumber.trim());
        if (info != null) {
            studentName.setValue(info[0]);
            course.setValue(info[1]);
            studentFound.setValue(true);
        } else {
            studentName.setValue("");
            course.setValue("");
            studentFound.setValue(false);
        }
    }

    // --- Item row management ---

    public void addItemRow() {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        current.add(new ItemRow());
        itemRows.setValue(current);
    }

    public void removeItemRow(int index) {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        if (current.size() > 1 && index >= 0 && index < current.size()) {
            current.remove(index);
            itemRows.setValue(current);
        }
    }

    public void updateItemRowType(int index, String type) {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        if (index >= 0 && index < current.size()) {
            current.get(index).type = type == null ? "" : type;
            current.get(index).name = "";
            itemRows.setValue(current);
        }
    }

    public void updateItemRowName(int index, String name) {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        if (index >= 0 && index < current.size()) {
            current.get(index).name = name == null ? "" : name;
            itemRows.setValue(current);
        }
    }

    // --- Form submission ---

    public void submitBorrow(String studentNumber, String studentNameStr, String courseStr, String collateral) {
        if (isBlank(studentNumber) || isBlank(studentNameStr)
                || isBlank(courseStr) || isBlank(collateral)) {
            submitError.setValue("Please complete all borrower fields.");
            return;
        }
        List<ItemRow> rows = deepCopy(itemRows.getValue());
        for (ItemRow row : rows) {
            if (isBlank(row.name)) {
                submitError.setValue("Please complete all item selections.");
                return;
            }
        }
        submitError.setValue(null);
        submitted.setValue(true);
    }

    public void clearSubmitError() {
        submitError.setValue(null);
    }

    public void resetForm() {
        studentName.setValue("");
        course.setValue("");
        studentFound.setValue(false);
        List<ItemRow> initial = new ArrayList<>();
        initial.add(new ItemRow());
        itemRows.setValue(initial);
        submitted.setValue(false);
        submitError.setValue(null);
    }

    // --- Utility ---

    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US);
        return sdf.format(new Date());
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        return sdf.format(new Date());
    }

    private List<ItemRow> deepCopy(List<ItemRow> source) {
        List<ItemRow> copy = new ArrayList<>();
        if (source != null) {
            for (ItemRow row : source) {
                copy.add(new ItemRow(row.type, row.name));
            }
        }
        return copy;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // --- Item row model ---

    public static class ItemRow {
        public String type;
        public String name;

        public ItemRow() {
            this.type = "";
            this.name = "";
        }

        public ItemRow(String type, String name) {
            this.type = type == null ? "" : type;
            this.name = name == null ? "" : name;
        }
    }
}
