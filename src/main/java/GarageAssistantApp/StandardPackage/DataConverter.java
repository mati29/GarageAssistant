package main.java.GarageAssistantApp.StandardPackage;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * Created by Mati on 2017-04-02.
 */
public class DataConverter {
    private static String billTemplatePath = "templates/StandardBill.jasper";

    public class ClientData {
        public static final String clientLastName = "client_lastname";
        public static final String clientFirstName = "client_firstname";
        public static final String clientEmail = "client_email";
        public static final String clientNumber = "client_number";
    }

    public class CarData {
        public static final String carNumberPlate = "car_numberPlate";
        public static final String carModel = "car_model";
        public static final String carBrand = "car_brand";
        public static final String carVintage = "car_vintage";
    }

    public class CommissionData {
        public static final String commissionTotal = "commission_total";
        public static final String commissionDate = "commission_Date";
    }

    public class TableData {
        public static final String partDataSource = "part_data_source";
        public static final String repairDataSource= "repair_data_source";
    }

    public static byte[] getReportAsBytes(Map<String, Object> parameters) {
        byte[] pdfReport = null;
        JasperPrint print;
        JasperReport report = loadTemplateFromResources(billTemplatePath);

        try {
            print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            pdfReport = JasperExportManager.exportReportToPdf(print);

        } catch (JRException e) {
            e.getStackTrace();
        }

        return pdfReport;
    }

    private static JasperReport loadTemplateFromResources(String resourcePath) {
        Resource resource = new ClassPathResource(resourcePath);

        JasperReport report = null;
        try {
            report = (JasperReport) JRLoader.loadObject(resource.getInputStream());
        } catch (Exception e) {
            e.getStackTrace();
        }

        return report;
    }


}
