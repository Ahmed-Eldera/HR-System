package com.orange.hr.generateData;


import com.orange.hr.enums.Gender;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


//@Component
public class InsertScriptGenerator implements CommandLineRunner {
    private static final String PATH = "src/main/resources/sql/";
    private static final int EMPLOYEES = 10_000;
    private static final Set<String> employeeNationalIds = new HashSet<>();
    private static final Long MANAGER_ID = 1L;
    private static final Integer MAX_LEAVE_DAYS = 40;
    private static final Integer MAX_BONUS_COUNT = 8;
    private final List<Long> architectEmployeeIds = new ArrayList<>();
    private final List<Long> seniorEmployeeIds = new ArrayList<>();
    private final List<Long> intermediateEmployeeIds = new ArrayList<>();
    private final List<EmployeeInfo> insertedEmployeeInfo = new ArrayList<>();
    LocalTime fixedTime = LocalTime.of(17, 0, 0);


    private Path createOrReplacePath(String fileName) throws IOException {
        Path path = Paths.get(PATH + fileName);
        Files.createDirectories(path.getParent());
        return path;
    }

    private void writeToFile(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardCharsets.UTF_8);
        System.out.println(path.getFileName() + " data written!");
    }

    private void generateInsertScript(String fileName, String tableName, String[] data) throws IOException {
        Path path = createOrReplacePath(fileName);
        List<String> lines = new ArrayList<>();

        for (String value : data) {
            String sql = String.format("INSERT INTO %s (name) VALUES ('%s');", tableName, value);
            lines.add(sql);
        }
        writeToFile(path, lines);
    }

    private int generateRandomNumberOfSpecificSize(int size) {
        // from 0 to size - 1
        return new Random().nextInt(size);
    }

    private Person generateFirstNameAndGender() {
        // get the firstName and gender
        int peopleSize = Data.PEOPLE.length;
        int random = generateRandomNumberOfSpecificSize(peopleSize);
        return Data.PEOPLE[random];
    }

    private String generateLastName() {
        int lastNameSize = Data.LAST_NAMES.length;
        int random = generateRandomNumberOfSpecificSize(lastNameSize);
        return Data.LAST_NAMES[random];
    }

    private LocalDate generateBirthDate() {
        // birthdate from 1960 to 2002
        long minDay = LocalDate.of(1960, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2002, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1); // Upper bound is exclusive
        return LocalDate.ofEpochDay(randomDay);
    }

    private LocalDate generateGraduationDateFromBirthDate(LocalDate birthDate) {
        // 1. Add 22 years
        int gradYear = birthDate.getYear() + 22;

        // 2. Set Month to May (Common Graduation Month)
        int gradMonth = 5;

        // 3. Pick a random day in the common window (May 10 - May 24)
        // threadLocalRandom uses (origin, bound) where bound is exclusive
        int gradDay = ThreadLocalRandom.current().nextInt(10, 25);

        return LocalDate.of(gradYear, gradMonth, gradDay);
    }

    private LocalDate generateJoinedDateFromGraduationDate(LocalDate graduationDate) {
        // suppose the employee hired after graduated
        // randomly pick joinedDate from graduationDate to currentDate
        LocalDate today = LocalDate.now();

        // Convert both to Epoch Days to create a numeric range
        long startEpoch = graduationDate.toEpochDay();
        long endEpoch = today.toEpochDay();

        // Pick a random day between them (origin inclusive, bound exclusive)
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);

        // return the result
        return LocalDate.ofEpochDay(randomEpochDay);
    }


    private void generateEmployeeInsertScript() throws IOException {
        Path path = createOrReplacePath("3.insert_employees.sql");
        List<String> lines = new ArrayList<>();

        for (int i = 0; i < EMPLOYEES; i++) {
            final long id = i + 1;
            Person person = generateFirstNameAndGender();
            String firstName = person.firstName();
            Gender gender = person.gender();

            LocalDate birthDate = (id == 1) ? LocalDate.of(1960, 1, 1) : generateBirthDate();
            LocalDate graduationDate = generateGraduationDateFromBirthDate(birthDate);
            LocalDate joinedDate = generateJoinedDateFromGraduationDate(graduationDate);
            LocalDateTime joinedDatee = LocalDateTime.of(generateJoinedDateFromGraduationDate(graduationDate), fixedTime);

            long pastExperienceYears = ChronoUnit.YEARS.between(graduationDate, joinedDate);

            long departmentId = generateRandomNumberOfSpecificSize(Data.DEPARTMENTS.length + 1);
            if (departmentId == 0) departmentId++;

            long teamId = generateRandomNumberOfSpecificSize(Data.TEAMS.length + 1);
            if (teamId == 0) teamId++;

            Long managerId = 1L;

            lines.add(String.format("INSERT INTO employees ( name, gender, date_of_birth," + " graduation_date, created_at, yoe,  department_id," + " team_id, manager_id) " + "VALUES ( '%s',  '%s',  '%s', '%s', '%s', %d,  %d, %d, %s);", firstName, gender, birthDate, graduationDate, joinedDatee, pastExperienceYears, departmentId, teamId, managerId));


            insertedEmployeeInfo.add(new EmployeeInfo(id, LocalDateTime.of(joinedDate, fixedTime)));
        }
        writeToFile(path, lines);
    }


    private void generateEmployeeSalary() throws IOException {
        Path path = createOrReplacePath("4.insert_employeeSalaries.sql");
        List<String> lines = new ArrayList<>();
        for (EmployeeInfo employeeInfo : insertedEmployeeInfo) {
            Double grossSalary = 50000d;

            // Set a fixed simple time (e.g., 5:00 PM)
            // Combine them into a LocalDateTime
            LocalDateTime creationDate = employeeInfo.joinedDate();
            double raisePercentage = 0.10d;
            long employeeId = employeeInfo.id();

            lines.add(String.format("INSERT INTO salaries (raise_percentage, gross_salary, employee_id, created_at) VALUES" + " (%f, %f, %d, '%s');",
                    raisePercentage, grossSalary, employeeId, creationDate));
        }
        writeToFile(path, lines);
    }


    private LocalDate[] generateDatesInSpecificYear(int year, int days) {
        Set<LocalDate> dates = new HashSet<>();
        int numberOfLeaves = generateRandomNumberOfSpecificSize(days + 1);

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        for (int i = 0; i < numberOfLeaves; i++) {
            int randomDay = ThreadLocalRandom.current().nextInt(start.getDayOfYear(), end.getDayOfYear() + 1);

            LocalDate randomDate = LocalDate.ofYearDay(year, randomDay);
            DayOfWeek day = randomDate.getDayOfWeek();

            // Skip Friday and Saturday
            if (day != DayOfWeek.FRIDAY && day != DayOfWeek.SATURDAY) {
                dates.add(randomDate); // Set guarantees uniqueness
            }
        }
        return dates.toArray(LocalDate[]::new);
    }

    private void generateLeavesInSpecificYear(int year) throws IOException {
        Path path = createOrReplacePath("7.insert_employee_leaves.sql");
        List<String> lines = new ArrayList<>();

        for (EmployeeInfo employeeInfo : insertedEmployeeInfo) {
            LocalDate[] dates = generateDatesInSpecificYear(year, MAX_LEAVE_DAYS);
            for (LocalDate date : dates) {
                LocalDateTime creationDate = LocalDateTime.of(date, fixedTime);
                lines.add(String.format("INSERT INTO leaves (leave_date, employee_id,created_at) VALUES ('%s', %d,'%s');", date.toString(), employeeInfo.id(), creationDate.toString()));
            }
        }
        writeToFile(path, lines);
    }


    private void generateBonusInSpecificYear(int year) throws IOException {
        Path path = createOrReplacePath("8.insert_employee_bonuses.sql");
        List<String> lines = new ArrayList<>();

        for (EmployeeInfo employeeInfo : insertedEmployeeInfo) {
            LocalDate[] dates = generateDatesInSpecificYear(year, MAX_BONUS_COUNT);
            for (LocalDate date : dates) {
                Double amount = 10000d;
                LocalDateTime creationDate = LocalDateTime.of(date, fixedTime);
                lines.add(String.format("INSERT INTO salary_adjustments (amount, created_at, employee_id) VALUES (%f, '%s', %d);", amount, creationDate.toString(), employeeInfo.id()));
            }
        }
        writeToFile(path, lines);
    }

    @Override
    public void run(String... args) throws Exception {
        generateInsertScript("1.insert_departments.sql", "departments", Data.DEPARTMENTS);
        generateInsertScript("2.insert_teams.sql", "teams", Data.TEAMS);
        // insert employees
        generateEmployeeInsertScript();
        // insert salaries for inserted employees
        generateEmployeeSalary();
        // insert employee leaves in 2025
        generateLeavesInSpecificYear(2025);
        // insert employee bonus in 2025
        generateBonusInSpecificYear(2025);
    }
}
