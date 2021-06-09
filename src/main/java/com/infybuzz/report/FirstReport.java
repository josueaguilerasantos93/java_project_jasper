package com.infybuzz.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.fill.JRTemplatePrintFrame;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class FirstReport {

    public static final String FILE_DATE_FORMAT = "YYYY_MM_dd__HH_mm_ss_SSS";
    public static final String PRITTY_DATE_FORMAT = "MMM dd, yyyy";
    public static final String FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat fileDateFormatter = new SimpleDateFormat(FILE_DATE_FORMAT, new Locale("en", "US"));
    public static final SimpleDateFormat prittyDateFormatter = new SimpleDateFormat(PRITTY_DATE_FORMAT, new Locale("en", "US"));
    public static final SimpleDateFormat fullDateFormatter = new SimpleDateFormat(FULL_DATE_FORMAT, new Locale("en", "US"));
    public static final SimpleDateFormat fullDateTimeFormatter = new SimpleDateFormat(FULL_DATETIME_FORMAT, new Locale("en", "US"));
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String TEMPLATES_LOCATION = "D:\\DOCUMENTACION & TUTORIALES\\Curso Udemy_Build Reports with JasperReports Java and Spring Boot" +
            "\\course_projects\\java_project_jasper\\src\\main\\resources\\templates\\";

    public static final String REPORTS_LOCATION = "D:\\DOCUMENTACION & TUTORIALES\\Curso Udemy_Build Reports with JasperReports Java and Spring Boot\\course_projects\\Exported Reports\\";

    public static final String pdfExtension = "pdf";
    public static final String jrxmlExtension = "jrxml";
    public static final String firstReportName = "FirstReport";
    public static final String studentReportName = "Student";

	public static void main(String[] args) {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("* Welcome to JasperSoft Project Tutorial *");
        System.out.println("******************************************");
        System.out.println();

        // Create FirstReport
        generateFirstReport();
        System.out.println();
        System.out.println();

        // Create Student Report
        generateStudentReport();

	}

    private static void generateFirstReport() {
	    String reportName = firstReportName;
        try {
            System.out.println(String.format("* Generating %s document:", reportName));

            // build parameters
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("studentName", "Josue Aguilera Santos");
            parameters.put("studentAge", 27);
            parameters.put("studentBirthDate", fullDateFormatter.parse("1993-09-26"));
            System.out.println("Parameters: " + MAPPER.writeValueAsString(parameters));

            // build fields
            Student student1 = new Student(1, "Josue", "Aguilera Santos", "Patrocinio", "La Habana");
            Student student2 = new Student(2, "Maria", "Gimenez", "Jose Marti", "La Habana");
            Student student3 = new Student(3, "Juan", "Gonzalez", "Alameda", "La Habana");
            Student student4 = new Student(4, "Marcos", "De la Rosa", "Camilo Cienfuegos", "La Habana");
            List<Student> studentList = new ArrayList<Student>();
            studentList.add(student1);
            studentList.add(student2);
            studentList.add(student3);
            studentList.add(student4);
            System.out.println("Fields: " + MAPPER.writeValueAsString(studentList));

            // Creating datasource
            System.out.print("Creating DataSource... ");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentList);
            System.out.println("DONE");

            generateReport(reportName, parameters, dataSource);

        } catch(Exception e) {
            System.out.println("Exception while creating report: " + reportName);
            System.out.println(e.getClass().getSimpleName() + ": "+ e.getLocalizedMessage());
        }
    }

    private static void generateStudentReport() {
        String reportName = studentReportName;
        try {
            System.out.println(String.format("* Generating %s document:", reportName));

            // build parameters
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("studentName", "Josue Aguilera Santos");

            // build fields
            List<Subject> subjectList = new ArrayList<Subject>();
            subjectList.add(new Subject("Java", 80));
            subjectList.add(new Subject("MySQL", 70));
            subjectList.add(new Subject("PHP", 50));
            subjectList.add(new Subject("MongoDB", 40));
            subjectList.add(new Subject("C++", 60));
            JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(subjectList);

            // chartDataSource
            JRBeanCollectionDataSource chartDataSource = new JRBeanCollectionDataSource(subjectList);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableData", tableDataSource);
            map.put("chartDataSource", chartDataSource);

            List<Map<String, ?>> mapList = new ArrayList<Map<String, ?>>();
            mapList.add(map);

            // Creating datasource
            System.out.print("Creating DataSource... ");
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(mapList);
            System.out.println("DONE");

            generateReport(reportName, parameters, dataSource);
        } catch(Exception e) {
            System.out.println("Exception while creating report: " + reportName);
            System.out.println(e.getClass().getSimpleName() + ": "+ e.getLocalizedMessage());
        }
    }

	private static void generateReport(String reportName, Map<String, Object> parameters, JRRewindableDataSource dataSource) throws Exception{
        System.out.print("Compiling Report... ");
        JasperReport jasperReport = JasperCompileManager.compileReport(TEMPLATES_LOCATION + reportName + "." + jrxmlExtension);
        System.out.println("DONE");
        runtimeModifications(reportName, jasperReport);
        System.out.print("Filling Report... ");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        System.out.println("DONE");
        String fileName = getFileName(reportName, pdfExtension);
        System.out.print("Exporting Report... ");
        JasperExportManager.exportReportToPdfFile(jasperPrint,REPORTS_LOCATION + fileName);
        System.out.println("DONE");
        System.out.println("Report successfully exported to: " + REPORTS_LOCATION + fileName);
    }

    private static void runtimeModifications(String reportName, JasperReport jasperReport){
	    if (reportName.equals(firstReportName)){
            // Modifications at runtime
            // Can modify any property like: location, alignment, size, backcolor,
            JRTextField studentNameTextField = (JRTextField) jasperReport.getPageHeader().getElementByKey("studentName");
            studentNameTextField.setForecolor(Color.red);

            JRTextField studentAgeTextField = (JRTextField) jasperReport.getPageHeader().getElementByKey("studentAge");
            studentAgeTextField.setForecolor(Color.yellow);

            JRTextField studentBirthDateTextField = (JRTextField) jasperReport.getPageHeader().getElementByKey("studentBirthDate");
            studentBirthDateTextField.setForecolor(Color.green);
        }
    }


	private static String getFormattedCurrentDatetime(){
	    return fileDateFormatter.format(new Date());
    }

    private static String getFileName(String fileName, String fileExtension){
//        return fileName + "_" + getFormattedCurrentDatetime() + "." + fileExtension;
        return fileName + "." + fileExtension;
    }

}
