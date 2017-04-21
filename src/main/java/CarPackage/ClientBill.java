package main.java.CarPackage;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Mati on 2017-03-28.
 */
public class ClientBill {

    public static byte[] makeBill(Bill bill) {
        Map<String, Object> parameters = getReportParameters(bill);
        byte[] pdfReport = DataConverter.getReportAsBytes(parameters);

        return pdfReport;
    }
    private static Map<String, Object> getReportParameters(Bill bill) {
        Map<String, Object> parameters = new HashMap<>();

        Commission commission = bill.getCommission();

        Client client = commission.getClient();

        parameters.put(DataConverter.ClientData.clientFirstName,client.getFirstName());
        parameters.put(DataConverter.ClientData.clientLastName,client.getLastName());
        parameters.put(DataConverter.ClientData.clientEmail,client.getEmail());
        parameters.put(DataConverter.ClientData.clientNumber,Integer.toString(client.getPhoneNumber()));

        Car car = commission.getCar();

        parameters.put(DataConverter.CarData.carNumberPlate,car.getNumberPlate());
        parameters.put(DataConverter.CarData.carModel,car.getModel());
        parameters.put(DataConverter.CarData.carBrand,car.getBrand());
        parameters.put(DataConverter.CarData.carVintage,car.getVintage());

        List<PartBillDTO> partTable = new ArrayList<>();
        List<RepairBillDTO> repairTable= new ArrayList<>();

        int repairPos=1;
        int partPos=1;
        double commissionTotal = 0d;

        for (Repair repair: commission.getRepairList()) {
            double partTotal = 0d;

            for(Part part: repair.getPartList()){
                Store store = part.getStore();
                PartBillDTO partElem = new PartBillDTO(partPos,store.getBrand() +" "+ store.getModel(),store.getPrice(),repairPos);
                partTotal+=partElem.getPartTotal();
                partTable.add(partElem);
                partPos++;
            }

            Employee employee = repair.getEmployee();
            RepairBillDTO repairElem = new RepairBillDTO(partTotal,repair.getHours(),employee.getSalary(),employee.getLastName(),repairPos);
            commissionTotal+=repairElem.getRepairCost();
            repairTable.add(repairElem);
            repairPos++;
        }

        parameters.put(DataConverter.CommissionData.commissionDate,commission.getTerm());
        parameters.put(DataConverter.CommissionData.commissionTotal,commissionTotal);

        JRBeanCollectionDataSource partDataSource = new JRBeanCollectionDataSource(partTable,false);
        JRBeanCollectionDataSource repairDataSource = new JRBeanCollectionDataSource(repairTable, false);

        parameters.put(DataConverter.TableData.partDataSource, partDataSource);
        parameters.put(DataConverter.TableData.repairDataSource, repairDataSource);

        return parameters;

    }
}
