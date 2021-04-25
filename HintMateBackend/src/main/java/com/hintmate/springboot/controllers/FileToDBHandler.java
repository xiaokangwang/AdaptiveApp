package com.hintmate.springboot.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;

import com.hintmate.springboot.model.UserKnowledgeModel;

public class FileToDBHandler {

	static String KNOWLEDGE_DB = "jdbc:sqlite:src/main/resources/user-knowledge.db";
	
	
	public static void loadKnownWords() throws SQLException{
//	    DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
	    Connection dbConnection = DriverManager.getConnection(KNOWLEDGE_DB);
	    try {
	        String query = "CREATE TABLE IF NOT EXISTS Known_Words (word ,counter integer);";
	        String sql = "INSERT INTO Known_Words(word,counter) VALUES(?,?)";
	        
	        int batchCounter = 0;
	        Statement s  = dbConnection.createStatement();
	        s.execute(query);
	        PreparedStatement pstmt = dbConnection.prepareStatement(sql);
	        File currentDirFile = new File(".");
			String helper = currentDirFile.getAbsolutePath();
	        String filePath = helper+"/pyFileDir/known_words.txt";
	        FileInputStream fis = new FileInputStream(filePath);
	        InputStreamReader isr = new InputStreamReader(fis);
	        BufferedReader bReader = new BufferedReader(isr);
	        ArrayList<UserKnowledgeModel> listResult = new ArrayList<>();
	        String line = null;
	        String strWord = null;
	        while(true) {
	            line = bReader.readLine();
	            System.out.println(line);
	            if(line == null) {
	                break;
	            } else {
//	                strWord = line.split(",");
//	                listResult.add(new UserKnowledgeModel(line, 0));
	                
	                pstmt.setString(1, line);
	                pstmt.setDouble(2, 0);
	                pstmt.addBatch();
	                batchCounter++;
	                if(batchCounter == 1000)
	                {
	                	pstmt.executeBatch();
	                	batchCounter = 0;
	                }
	                
	            }
	        }
	    } catch (SQLException | IOException ex) {
	        ex.printStackTrace();
	    }
	}
	public static void loadUnKnownWords() throws SQLException{
//	    DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
	    Connection dbConnection = DriverManager.getConnection(KNOWLEDGE_DB);
	    try {
	        String query = "CREATE TABLE IF NOT EXISTS Unknown_Words (word ,counter integer);";
	        String sql = "INSERT INTO Unknown_Words(word,counter) VALUES(?,?)";
	        Statement s  = dbConnection.createStatement();
	        s.execute(query);
	        int batchCounter = 0;
	        PreparedStatement pstmt = dbConnection.prepareStatement(sql);
	        File currentDirFile = new File(".");
			String helper = currentDirFile.getAbsolutePath();
	        String filePath = helper+"/pyFileDir/unknown_words.txt";
	        FileInputStream fis = new FileInputStream(filePath);
	        InputStreamReader isr = new InputStreamReader(fis);
	        BufferedReader bReader = new BufferedReader(isr);
	        ArrayList<UserKnowledgeModel> listResult = new ArrayList<>();
	        String line = null;
	        String[] strWord = null;
	        while(true) {
	            line = bReader.readLine();
	            if(line == null) {
	                break;
	            } else {
//	            	listResult.add(new UserKnowledgeModel(line, 0));
	                
	                pstmt.setString(1, line);
	                pstmt.setDouble(2, 0);
	                pstmt.addBatch();
	                batchCounter++;
	                if(batchCounter == 1000)
	                {
	                	pstmt.executeBatch();
	                	batchCounter = 0;
	                }
	            }
	        }
	    } catch (SQLException | IOException ex) {
	        ex.printStackTrace();
	    }
	}
	public static void createDatabase() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
	    try (Connection conn = DriverManager.getConnection(KNOWLEDGE_DB)) {
	        if (conn != null) {
	            DatabaseMetaData meta = conn.getMetaData();
	            meta.getDriverName();
	            System.out.println("DB created");
	        }
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	public static void loadTextToDB() throws ClassNotFoundException {
//		ClassPathResource res = new ClassPathResource("adaptive_applications.py");
//		File f = null;
//		try {
//			f = res.getFile();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		System.out.println(f.getAbsolutePath());
		createDatabase();
		File currentDirFile = new File(".");
		String helper = currentDirFile.getAbsolutePath();
		System.out.println(helper);
		ProcessBuilder pb = new ProcessBuilder("python",helper+"\\pyFileDir\\adaptive_clean_code.py",helper);
		try {
			String s = "";
			Process p = pb.start();
			BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));
			 while ((s = stdInput.readLine()) != null) {
	                System.out.println(s);
	            }
			System.out.println("Text converted to words");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			loadKnownWords();
			loadUnKnownWords();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			createDatabase();
			loadKnownWords();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
