package MobileOperator;

//Потребно е да се развие апликација за чување на информации за месечните сметки на корисниците на еден мобилен оператор. Мобилниот оператор нуди 2 типа на пакети на своите корисници: S и M пакет. Во пакетот S корисникот во рамки на еден месец има бесплати 100 минути разговор, 50 СМС пораки и 5 GB интернет, додека пак во пакетот M корисникот има бесплатни 150 минути, 60 СМС пораки и 10 GB интернет. Основната цена на пакетот S е 500 денари, додека пак основната цена на пакетот M е 750 денари.
//
//Во операторот работат претставници за продажба (sales reps) коишто се одговорни за клиентите кои тие ги вовеле во операторот и соодветно добиваат одредена провизија (commision) за секоја месечна сметка на своите клиенти.
//
//За таа цел дефинирајте класа MobileOperatorсо следните методи:
//
//Конструктор без аргументи MobileOperator()
//Метод void readSalesRepData (InputStream is) - метод за вчитување на месечните извештаии за клиентите на sales reps. Во секој ред се дадени информациите за еден sales rep во следниот формат: salesRepID [customerBill1] [customerBill2] … [customeBillrN]. Форматот на информациите за секој сметка е следен: customerID package_type count_of_minutes count_of_SMS count_of_data_in_GB, каде што package_type е еден карактер S или М што го означува типот на пакетот. Бројот на потрошени минути и пораки е цел број, додека бројот на потрошен интернет може да биде и децимален број. Сите информации во рамки на една линија се одделени со празно место.
//ID на еден sales rep содржи точно 3 знаци (сите се цифри), додека пак ID на клиент содржи точно 7 знаци (сите се цифри). Да се фрли исклучот од тип InvalidIdException доколку некој клиенt или sales rep имаат невалиден ID. Доколку станува збор за клиент, сметката на тој клиент да се игнорира. Доколку станува збор за sales rep да се игнорира целиот негов извештај. Да се испечати порака при фаќање на исклучокот. Форматот на пораките е во вториот и третиот тест пример.
//методот void printSalesReport (OutputStream os) - методот за печатење на извештаите за sales representatives. Извештајот за еден sales rep e во следниот формат ID number_of_bills min_bill average_bill max_bill total_commission. Под bill се подразбира износот на сметката на некој клиент.
//На сликата подолу е дадено колку чини секоја услуга над бесплатните услуги. Сметката на даден клиент се пресметува така што се собира основната цена на пакетот со дополнителните трошоци направени како резултат на надминување на бесплатните минути/пораки/GB интернет.
//Еден sales rep добива провизија 4% од сметката на корисник на пакет M и 7% од сметката на корисник на пакет S.
//Извештаите да бидат испечатени сортирани во опаѓачки редослед според провизијата на sales rep-от.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class InvalidIdException extends Exception {
    public InvalidIdException(String ID, String message) {
        System.out.println(ID + message);
    }
}

class Client {
    private final String customerID;
    private final String packageType;
    private final String minutes;
    private final String sms;
    private final String gb;

    public Client(String str) throws InvalidIdException {
        String[] split = str.split("\\s+");

        this.customerID = split[1];
        this.packageType = split[2];
        this.minutes = split[3];
        this.sms = split[4];
        this.gb = split[5];

        if (customerID.length() != 7) {
            throw new InvalidIdException(customerID, " is not a valid user ID");
        }
    }

    //0.07 po vkupna smetka ili 0.04 po vkupna smetka

    public int presmetajSmetka() {

        int minuti = Integer.parseInt(minutes);
        int SMS = Integer.parseInt(sms);
        int GB = Integer.parseInt(gb);

        if (Objects.equals(packageType, "S")) {
            int smetka = 500;
            if (minuti > 100) {
                smetka += (minuti - 100) * 5;
            }
            if (SMS > 50) {
                smetka += (SMS - 50) * 6;
            }
            if (GB > 5) {
                smetka += (GB - 5) * 25;
            }
            return smetka;
        }
        if (Objects.equals(packageType, "M")) {
            int smetka = 750;
            if (minuti > 150) {
                smetka += (minuti - 150) * 4;
            }
            if (SMS > 60) {
                smetka += (SMS - 60) * 4;
            }
            if (GB > 10) {
                smetka += (GB - 10) * 20;
            }
            return smetka;
        }

        return 0;
    }

    public String getPackageType() {
        return packageType;
    }

    public String getCustomerID() {
        return customerID;
    }
}

class MobileOperator {
    //private List<SalesRep> reps;
    public String salesRepID;
    private final List<Client> clients;

    public MobileOperator() {
        //this.reps = new ArrayList<SalesRep>();
        this.salesRepID = "";
        this.clients = new ArrayList<Client>();
    }

    public void readSalesRepData(InputStream is) throws InvalidIdException {
        Scanner scanner = new Scanner(is);

        while (scanner.hasNextLine()) {
            Client client = new Client(scanner.next("mmmmmmm M mmm MM mmmmm"));
            String[] split = scanner.nextLine().split("\\s+");
            salesRepID = split[0];
            if (salesRepID.length() != 3)
                throw new InvalidIdException(salesRepID, " is not a valid sales rep ID");
            clients.add(client);
        }
    }

    public double minimumBill() {
        double min = 999999;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).presmetajSmetka() < min)
                min = clients.get(i).presmetajSmetka();
        }
        return min;
    }

    public double maximumBill() {
        double max = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).presmetajSmetka() > max)
                max = clients.get(i).presmetajSmetka();
        }
        return max;
    }

    public double averageBill() {
        int sum = 0;
        for (int i = 0; i < clients.size(); i++) {
            sum += clients.get(i).presmetajSmetka();
        }
        return (double) sum / clients.size();
    }

    public double commission() {
        double commission = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (Objects.equals(clients.get(i).getPackageType(), "M"))
                commission += 0.04 * clients.get(i).presmetajSmetka();
            if (Objects.equals(clients.get(i).getPackageType(), "S"))
                commission += 0.07 * clients.get(i).presmetajSmetka();
        }
        return commission;
    }

    public void printSalesReport(PrintStream os) {
        os.printf("%s Count: %s Min: %f Average: %f Max: %s Commission: %s", salesRepID, clients.size(), minimumBill(), averageBill(), maximumBill(), commission());
    }
}

public class MobileOperatorTest {
    public static void main(String[] args) throws InvalidIdException {
        MobileOperator mobileOperator = new MobileOperator();
        System.out.println("---- READING OF THE SALES REPORTS ----");
        mobileOperator.readSalesRepData(System.in);
        System.out.println("---- PRINTING FINAL REPORTS FOR SALES REPRESENTATIVES ----");
        mobileOperator.printSalesReport(System.out);
    }
}