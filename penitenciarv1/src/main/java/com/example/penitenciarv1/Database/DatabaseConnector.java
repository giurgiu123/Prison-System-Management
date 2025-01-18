package com.example.penitenciarv1.Database;

import com.example.penitenciarv1.Entities.*;
import com.example.penitenciarv1.Listeners.DynamicScallingAppIntPrisonerFutureTasks;
import com.example.penitenciarv1.Services.WrapperClassArrayListAndInt;
import eu.hansolo.toolbox.time.DateTimes;
import javafx.beans.property.SimpleStringProperty;

import javax.print.DocFlavor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private String user = "root";
    private String url = "jdbc:mysql://127.0.0.1:3306/penitenciar";
    private String password = "Baze_De_Date-2224";
    public Connection conn = null;

    public DatabaseConnector() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

        } catch (Exception e) {
            System.out.println("eroare");
            throw new RuntimeException(e);
        }
    }

    public void callRandomProcedure() {
        try {
            System.out.println("Calling random procedure");
            CallableStatement callableStatement = conn.prepareCall("SELECT * FROM penitenciar.detinut");
            callableStatement.execute();
            if (callableStatement.getResultSet() == null) {
                System.out.println("No results found");
            }
            while (callableStatement.getResultSet().next()) {
                System.out.println(callableStatement.getResultSet().getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User checkAndReturnUser(String username, String password) {

        try {
            String theQuery = "SELECT * FROM penitenciar.utilizator " +
                    "WHERE username ='" + username + "' AND password = '" + password + "'";
            CallableStatement callableStatement = conn.prepareCall(theQuery);
            callableStatement.execute();
            System.out.println(callableStatement);
            if (callableStatement.getResultSet() == null) {
                System.out.println("No results found");
                return null;
            }

            User newUser = new User();
            if (callableStatement.getResultSet().next()) {
                newUser.setId(callableStatement.getResultSet().getInt(1));
                System.out.println(newUser.getId());
                newUser.setAccessRights(callableStatement.getResultSet().getInt(4));
                newUser.printUser();
                return newUser;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Query error");
            return null;
        }

    }

    public String getRemainingSentence(int idPrizioner) {
        try{
            String perioadaRamasa = "";
            CallableStatement cs = conn.prepareCall("{call penitenciar.remaining_time_based_on_id_inmate( ?, ?)}");
            // we calculated the total time
            cs.setInt(1, idPrizioner);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.execute();
            if(cs.getString(2) != null)
            {
                perioadaRamasa = cs.getString(2);
                return perioadaRamasa;
            }
            System.out.println(perioadaRamasa);

            return "Necunoscut";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Guardian> getGuardianColleaguesSameBlock(int idGuardian) {
        ArrayList<Guardian> guardian = new ArrayList<>();
        try{
            CallableStatement cs = conn.prepareCall("{call penitenciar.GetColegiiGardianului(?)}");
            cs.setInt(1, idGuardian);
            boolean hasResults = cs.execute();
            if(hasResults) {
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {
                    Guardian newGuardian = new Guardian();
                    newGuardian.setId(new SimpleStringProperty(rs.getString(1)));
                    newGuardian.setUsername(new SimpleStringProperty(rs.getString(2)));
                    newGuardian.setFloor(new SimpleStringProperty(rs.getString(3)));
                    newGuardian.setDetentionBlock(new SimpleStringProperty(rs.getString(4)));
                    guardian.add(newGuardian);
                }
                rs.close();
            }else
                System.out.printf("No results found");

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return guardian;
    }

    public ArrayList<Inmates> getInmatesOnShift(int idGardian) {
        ArrayList<Inmates> inmates = new ArrayList<>();
        try{
            CallableStatement cs = conn.prepareCall("{call penitenciar.GetInmatesOnShift(?)}");
            cs.setInt(1, idGardian);
            boolean hasResults = cs.execute();
            if(hasResults) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    Inmates newInmate = new Inmates();
                    newInmate.setid(new SimpleStringProperty(rs.getString(1)));
                    newInmate.setName(new SimpleStringProperty(rs.getString(2)));
                    newInmate.setIdCelula(new SimpleStringProperty(rs.getString(3)));
                    newInmate.setProfession(new SimpleStringProperty(rs.getString(4)));
                    String remainedSentence = getRemainingSentence(Integer.parseInt(newInmate.getid().getValue()));
                    newInmate.setSentenceRemained(new SimpleStringProperty(remainedSentence));
                    inmates.add(newInmate);
                }
                rs.close();
            }else{
                System.out.println("No results found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return inmates;
    }

    public ArrayList<String> getEmptyCells(int idGardian) {
        ArrayList<String> emptyCells = new ArrayList<>();
        try{
            CallableStatement callableStatement = conn.prepareCall("{call penitenciar.GetCellsOnShift(?)}");
            System.out.println(idGardian);
            callableStatement.setInt(1, idGardian);
            callableStatement.execute();
            ResultSet rs = callableStatement.getResultSet();
            if(rs == null){
                System.out.println("No results found");
            }else {
                while (rs.next()) {
                    emptyCells.add(rs.getString(1));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return emptyCells;
    }

    public void updateCellSize(int idCell, int modifyValue){
        try {
            String updateInitialCell = "UPDATE penitenciar.celula SET locuri_ramase = locuri_ramase + ? WHERE id_celula = ?";
            CallableStatement csUpdateCell = conn.prepareCall(updateInitialCell);
            csUpdateCell.setInt(1, modifyValue);
            csUpdateCell.setInt(2, idCell);
            csUpdateCell.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateInmateCell(int idInmate, int newCell) {
        try {
            //get the fk_id_cell from detinut table
            String getFK_ID_Cell = "SELECT detinut.fk_id_celula FROM penitenciar.detinut WHERE detinut.id_detinut = ?";
            CallableStatement csFKCell = conn.prepareCall(getFK_ID_Cell);
            csFKCell.setInt(1, idInmate);
            csFKCell.executeQuery();
            ResultSet rsFKCell = csFKCell.getResultSet();
            int fk_id;
            if(rsFKCell.next())
                fk_id = rsFKCell.getInt(1);
            else {
                fk_id = -1;
                System.out.println("No results found");
            }

            //get id of the initial cell the inmate was in
            String getInitialCell = "SELECT celula.id_celula FROM penitenciar.celula WHERE celula.id_celula = ?";
            CallableStatement csInitialCell = conn.prepareCall(getInitialCell);
            csInitialCell.setInt(1, fk_id);
            csInitialCell.executeQuery();
            ResultSet rsInitialCell = csInitialCell.getResultSet();
            int initialCell;
            if(rsInitialCell.next())
                initialCell = rsInitialCell.getInt(1);
            else {
                initialCell = -1;
                System.out.println("No results found");
            }

            //update the size of each cell
            updateCellSize(initialCell, -1);
            updateCellSize(newCell, 1);

            String theQuery = "UPDATE penitenciar.detinut SET fk_id_celula = ? WHERE id_detinut = ?";
            CallableStatement callableStatement = conn.prepareCall(theQuery);
            callableStatement.setInt(1, newCell);
            callableStatement.setInt(2, idInmate);
            callableStatement.execute();
        }catch (Exception e){
            System.out.println("Update error");
            e.printStackTrace();
        }
    }

    public ArrayList<Guardian> getGuardianColleaguesWholePrison(int idGuardian) {
        ArrayList<Guardian> guardian = new ArrayList<>();
        try{
            CallableStatement cs = conn.prepareCall("{call penitenciar.GetTotiColegiiGardianului(?)}");
            cs.setInt(1, idGuardian);
            boolean hasResult = cs.execute();
            if(hasResult) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    Guardian newGuardian = new Guardian();
                    newGuardian.setId(new SimpleStringProperty(cs.getResultSet().getString(1)));
                    newGuardian.setUsername(new SimpleStringProperty(cs.getResultSet().getString(2)));
                    newGuardian.setFloor(new SimpleStringProperty(cs.getResultSet().getString(3)));
                    newGuardian.setDetentionBlock(new SimpleStringProperty(cs.getResultSet().getString(4)));
                    guardian.add(newGuardian);
                }
            }else
                System.out.printf("No results found");


        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return guardian;
    }

    public int getGuardianId(int idGuardian) {
        try{
            System.out.println("calling a query that gives the guardian id based on the user id");
            CallableStatement cs = conn.prepareCall("SELECT id FROM penitenciar.gardian WHERE fk_id_utilizator = ?");
            cs.setInt(1, idGuardian);
            cs.executeQuery();
            if(cs.getResultSet().next()) {
                return cs.getResultSet().getInt(1);
            }else{
                System.out.println("No results found in getGuardian");
                return -1;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public List<DynamicScallingAppIntPrisonerFutureTasks.Task> getFutureTasks(String detinutUsername) {
        List<DynamicScallingAppIntPrisonerFutureTasks.Task> tasks = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("{CALL GetTaskuriViitoare(?)}");
            cs.setString(1, detinutUsername); // Setăm parametru de intrare
            ResultSet rs = cs.executeQuery(); // Executăm și obținem rezultatul

            while (rs.next()) {
                String idTask = rs.getString("ID_Task");
                String description = rs.getString("Descriere");
                String difficulty = rs.getString("Dificultate");
                String startTime = rs.getString("Inceput");
                String endTime = rs.getString("Sfarsit");

                tasks.add(new DynamicScallingAppIntPrisonerFutureTasks.Task(idTask, description, difficulty, startTime, endTime));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing GetTaskuriViitoare procedure", e);
        }
        return tasks;
    }

    public int[] getCarceraIdAndOcupationStatus(int idInmate) {
        int [] carceraInfo = new int[2];
        try{
            System.out.println("ASDADADASD" + idInmate);
            CallableStatement cs = conn.prepareCall("{CALL penitenciar.GetCarceraPrizonier(?)}");
            cs.setInt(1, idInmate);
            ResultSet rs = cs.executeQuery();
            if(rs.next()) {
                carceraInfo[0] = rs.getInt(2);
                carceraInfo[1] = rs.getInt(3);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return carceraInfo;
    }

    public void updateCarceraStatus(int id_carcera){
        String updateStatusQuery = "UPDATE carcera SET is_free = ? WHERE id_carcera = ?";
        try {
            CallableStatement cs = conn.prepareCall(updateStatusQuery);
            cs.setInt(1, 0);
            cs.setInt(2, id_carcera);
            cs.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void newRegistrationToSolitude(String idInmate, int idCarcera, String endTime) {
        String newInsertQuery = "INSERT INTO `inregistrare_carcera`(`fk_id_carcera`, `fk_id_detinut`, `end_time`) VALUES(?,?,?);";
        try{
            CallableStatement cs = conn.prepareCall(newInsertQuery);
            cs.setInt(1, idCarcera);
            cs.setString(2, idInmate);
            cs.setString(3, endTime);
            cs.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String getLastIncarceration(int carceraId) {
        String lastIncarceration = null;
        String query = "SELECT end_time FROM inregistrare_carcera WHERE fk_id_carcera = ? ORDER BY end_time DESC;";
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1, carceraId);
            ResultSet rs = cs.executeQuery();
            if(rs.next()) {
                lastIncarceration = rs.getString(1);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return lastIncarceration;
    }

    public ArrayList<Inmates> getAllInmates(){

        ArrayList<Inmates> inmates = new ArrayList<Inmates>();
        try{
            CallableStatement cs = conn.prepareCall("call GetAllInmates()");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                String name = rs.getString("nume");
                String id =  rs.getString("id");
                String sentence = getRemainingSentence(Integer.parseInt(id));
                String profession = rs.getString("profesie");
                String celula = rs.getString("celula");
                inmates.add(new Inmates(name, id, sentence, profession, celula));
            }
            System.out.println("injuratura");
            return inmates;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            return inmates;
        }

    }
    public ArrayList<Sentence> getAllSentencesOfOneInmate(int idInmate) {
        ArrayList<Sentence> sentences = new ArrayList<>();
        try{
            ///  caut sentintele fiecariu detinut

            CallableStatement cs = conn.prepareCall("SELECT * FROM penitenciar.sentinta where fk_id_detinut = "+ idInmate +" ;");
            
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                    String categorie = rs.getString("categorie");
                    String motivSpecific = rs.getString("motiv_specific");
                    String start_time = rs.getString("start_time");
                    String end_time = rs.getString("end_time");
                    sentences.add(new Sentence(categorie, motivSpecific, start_time, end_time));
            }


            return sentences;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            return sentences;
        }

    }

    public ArrayList<Visit> getVisits(int idVisitor){
        try{
            CallableStatement cs = conn.prepareCall("{CALL GetProgramariByVizitator(?)}");
            cs.setInt(1, idVisitor);
            ResultSet rs = cs.executeQuery();
            ArrayList<Visit> visits = new ArrayList<>();
            while (rs.next()) {

                String startTime = (rs.getString("startTime"));
                String endTime = (rs.getString("endTime"));
                String nume = (rs.getString("nume"));
                System.out.println(nume);
                Visit visit = new Visit(startTime, endTime, nume);
                visits.add(visit);
            }
            return visits;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public int getIdVizitatorPentruUtilizator(int id) {
        try{
            CallableStatement cs = conn.prepareCall("select * from vizitator where vizitator.fk_id_utilizator = ? limit 1");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
            System.out.println("This user is not a visitor");
            return -1;
        }
        catch (Exception e){
            System.out.println("Vizitatorul cu acest id de utilizator nu exista");
            throw new RuntimeException(e);
        }


    }

    public ArrayList<Inmates> getVisitedInmates(int idVisitor) {
        try{
            CallableStatement cs = conn.prepareCall("{CALL get_data_detinut_from_vizitor(?)}");
            cs.setInt(1, idVisitor);
            ResultSet rs = cs.executeQuery();
            ArrayList<Inmates> inmates = new ArrayList<>();
            while (rs.next()) {
                String idInmate = rs.getString("fk_id_prizioner");
                String numeInmate = rs.getString("nume");
                String remainingSentence = rs.getString("sentence_remained");
                inmates.add(new Inmates(numeInmate, idInmate, remainingSentence));

            }
            return inmates;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String fetchRemainingSentence(String username) {
        if (conn == null) {
            return "Database connection is not established.";
        }

        String callQuery = "{CALL GetRemainingSentence(?)}";
        try (CallableStatement callableStatement = conn.prepareCall(callQuery)) {
            callableStatement.setString(1, username);

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                if (resultSet.next()) {
                    int years = resultSet.getInt("Ani");
                    int months = resultSet.getInt("Luni");
                    int days = resultSet.getInt("Zile");
                    return String.format("Remaining Time: %d years, %d months, %d days", years, months, days);
                } else {
                    return "No data available for the given username.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching sentence data: " + e.getMessage();
        }
    }

    public String getInmateUsername(int idInmate) {
        String inmateUsername = null;
        String query = "{CALL  GetUsernameDetinut(?)}";
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1, idInmate);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                inmateUsername = rs.getString("username");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return inmateUsername;
    }

    public WrapperClassArrayListAndInt getProgramariPtDetinut(String inmateUsername) {
        ArrayList<Visit> visits = new ArrayList<>();
        int [] idVisit = new int[10];
        int contIdVisits = 0;
        try{
            String query = "{CALL GetVisitorScheduleByUsernamePerf(?)}";
            CallableStatement cs = conn.prepareCall(query);
            cs.setString(1, inmateUsername);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                visits.add(new Visit(rs.getString(2), rs.getString(3), rs.getString(1)));
                idVisit[contIdVisits] = rs.getInt(4);
                contIdVisits++;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return new WrapperClassArrayListAndInt(visits, idVisit);
    }

    public void deleteVisit(int idVisit) {
        String query = "DELETE FROM programari WHERE id_programare = ?";
        try{
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1, idVisit);
            cs.execute();
        }catch (Exception e){
            System.out.println("Nu s-a putut sterge");
            throw new RuntimeException(e);
        }
    }

    public void deleteInscriereTask(int idTask, int idDetinut) {
        String query = "DELETE FROM inscriere_task WHERE fk_id_detinut = ? AND fk_id_task = ?";
        try{
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1, idDetinut);
            cs.setInt(2, idTask);
            cs.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteTask(int idTask, int idDetinut) {
        String query = "DELETE FROM task_inchisoare WHERE id_task = ?";
        try{
            deleteInscriereTask(idTask, idDetinut);
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1, idTask);
            cs.execute();
        }catch (Exception e){
            System.out.println("Nu s-a putut sterge");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Task> getTasksDetinut(String inmateUsername) {
        ArrayList<Task> tasks = new ArrayList<>();
        String query = "{CALL GetTaskuriViitoare(?)}";
        try{
            CallableStatement cs = conn.prepareCall(query);
            cs.setString(1, inmateUsername);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                String taskId = rs.getString(1);
                String description = rs.getString(2);
                String dificulty = rs.getString(3);
                String startDate = rs.getString(4);
                String endDate = rs.getString(5);
                tasks.add(new Task(taskId, description, dificulty, startDate, endDate));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return tasks;
    }


    public Connection getConnection() {
        return this.conn;
    }

    public ArrayList<Visit> getAllVisits() {
        try{
            ArrayList<Visit> visits = new ArrayList<>();
            CallableStatement cs = conn.prepareCall("call penitenciar.get_programare_details();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Visit visit = new Visit();
                visit.setIdVisit(rs.getString("id_programare"));
                visit.setVisitType(rs.getString("tip_programari").equals("1") ? "Visit" : "Laundry");
                visit.setStartTime(rs.getString("start_time"));
                visit.setEndTime(rs.getString("end_time"));
                visit.setInmateName(rs.getString("detinut_name"));
                visit.setVisitorName(rs.getString("vizitator_name"));
                //
                visits.add(visit);
            }
            return visits;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addInscriereTask(int idLastIns, int inmateId){
        String query = "INSERT INTO `inscriere_task`(`fk_id_detinut`, `fk_id_task`) VALUES (?, ?);";
        try{
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1, inmateId);
            cs.setInt(2, idLastIns);
            cs.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void addTask(String dificulty, String startTime, String endTime, String description, int inmateId){
        String query = "INSERT INTO `task_inchisoare` (`difficulty`, `start_time`, `end_time`, `description`) VALUES\n" +
                "(?,?,?,?);";
        try{
            CallableStatement cs = conn.prepareCall(query);
            cs.setString(1, dificulty);
            cs.setString(2, startTime);
            cs.setString(3, endTime);
            cs.setString(4, description);
            cs.execute();
            String lastIdQuery = "SELECT last_insert_id();";
            try{
                CallableStatement cs1 = conn.prepareCall(lastIdQuery);
                ResultSet rs1 = cs1.executeQuery();
                if(rs1.next()){
                    int idLastIns = rs1.getInt(1);
                    addInscriereTask(idLastIns, inmateId);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
