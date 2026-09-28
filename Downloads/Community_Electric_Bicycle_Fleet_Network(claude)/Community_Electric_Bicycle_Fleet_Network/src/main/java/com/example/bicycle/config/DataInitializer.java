package com.example.bicycle.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.bicycle.model.Account;
import com.example.bicycle.model.AnalyticAccount;
import com.example.bicycle.model.Budget;
import com.example.bicycle.model.CorporateSponsor;
import com.example.bicycle.model.CustomerInvoice;
import com.example.bicycle.model.DockStation;
import com.example.bicycle.model.EBike;
import com.example.bicycle.model.Journal;
import com.example.bicycle.model.JournalEntry;
import com.example.bicycle.model.LedgerEntry;
import com.example.bicycle.model.Payment;
import com.example.bicycle.model.Product;
import com.example.bicycle.model.PurchaseOrder;
import com.example.bicycle.model.Ride;
import com.example.bicycle.model.Rider;
import com.example.bicycle.model.SalesOrder;
import com.example.bicycle.model.User;
import com.example.bicycle.model.Vendor;
import com.example.bicycle.model.VendorBill;
import com.example.bicycle.repository.AccountRepository;
import com.example.bicycle.repository.AnalyticAccountRepository;
import com.example.bicycle.repository.BudgetRepository;
import com.example.bicycle.repository.CorporateSponsorRepository;
import com.example.bicycle.repository.CustomerInvoiceRepository;
import com.example.bicycle.repository.DockStationRepository;
import com.example.bicycle.repository.EBikeRepository;
import com.example.bicycle.repository.JournalEntryRepository;
import com.example.bicycle.repository.JournalRepository;
import com.example.bicycle.repository.LedgerEntryRepository;
import com.example.bicycle.repository.PaymentRepository;
import com.example.bicycle.repository.ProductRepository;
import com.example.bicycle.repository.PurchaseOrderRepository;
import com.example.bicycle.repository.RideRepository;
import com.example.bicycle.repository.RiderRepository;
import com.example.bicycle.repository.SalesOrderRepository;
import com.example.bicycle.repository.UserRepository;
import com.example.bicycle.repository.VendorBillRepository;
import com.example.bicycle.repository.VendorRepository;

/**
 * Seeds the database with realistic sample/dummy data on first run so the
 * dashboard and every module has data to show immediately (and Postman
 * requests have real IDs to work against). Runs only when the bikes table
 * is empty, so it never duplicates data on subsequent restarts.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final EBikeRepository bikeRepo;
    private final DockStationRepository stationRepo;
    private final RiderRepository riderRepo;
    private final VendorRepository vendorRepo;
    private final CorporateSponsorRepository sponsorRepo;
    private final ProductRepository productRepo;
    private final RideRepository rideRepo;
    private final PurchaseOrderRepository poRepo;
    private final VendorBillRepository vendorBillRepo;
    private final SalesOrderRepository salesOrderRepo;
    private final CustomerInvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;
    private final AccountRepository accountRepo;
    private final JournalRepository journalRepo;
    private final JournalEntryRepository journalEntryRepo;
    private final LedgerEntryRepository ledgerRepo;
    private final AnalyticAccountRepository analyticRepo;
    private final BudgetRepository budgetRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(EBikeRepository bikeRepo, DockStationRepository stationRepo, RiderRepository riderRepo,
                            VendorRepository vendorRepo, CorporateSponsorRepository sponsorRepo, ProductRepository productRepo,
                            RideRepository rideRepo, PurchaseOrderRepository poRepo, VendorBillRepository vendorBillRepo,
                            SalesOrderRepository salesOrderRepo, CustomerInvoiceRepository invoiceRepo, PaymentRepository paymentRepo,
                            AccountRepository accountRepo, JournalRepository journalRepo, JournalEntryRepository journalEntryRepo,
                            LedgerEntryRepository ledgerRepo, AnalyticAccountRepository analyticRepo, BudgetRepository budgetRepo,
                            UserRepository userRepo, PasswordEncoder encoder) {
        this.bikeRepo = bikeRepo;
        this.stationRepo = stationRepo;
        this.riderRepo = riderRepo;
        this.vendorRepo = vendorRepo;
        this.sponsorRepo = sponsorRepo;
        this.productRepo = productRepo;
        this.rideRepo = rideRepo;
        this.poRepo = poRepo;
        this.vendorBillRepo = vendorBillRepo;
        this.salesOrderRepo = salesOrderRepo;
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
        this.accountRepo = accountRepo;
        this.journalRepo = journalRepo;
        this.journalEntryRepo = journalEntryRepo;
        this.ledgerRepo = ledgerRepo;
        this.analyticRepo = analyticRepo;
        this.budgetRepo = budgetRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (bikeRepo.count() > 0) {
            return; // already seeded
        }

        // ---- Users (login accounts) ----
        // Demo admin credential intentionally removed; create admin accounts only through the app or a secure setup step.
        userRepo.save(user("Rohit Sharma", "rohit@ebike.com", "rider123", "RIDER"));
        userRepo.save(user("Priya Nair", "priya@ebike.com", "rider123", "RIDER"));

        // ---- Stations ----
        DockStation s1 = station("Station-1", "MG Road", 15, 6, 4);
        DockStation s2 = station("Station-2", "City Mall", 12, 3, 3);
        DockStation s3 = station("Station-3", "Railway Station", 20, 9, 5);
        DockStation s4 = station("Station-4", "Tech Park", 10, 2, 2);
        stationRepo.saveAll(java.util.List.of(s1, s2, s3, s4));

        // ---- Bikes ----
        EBike b1 = bike("EB-001", "E-Bike X1", 85, "ACTIVE", 9.9252, 78.1198);
        EBike b2 = bike("EB-002", "E-Bike X1", 62, "ACTIVE", 9.9300, 78.1250);
        EBike b3 = bike("EB-003", "E-Bike X2", 18, "LOW_BATTERY", 9.9180, 78.1100);
        EBike b4 = bike("EB-004", "E-Bike X2", 90, "ACTIVE", 9.9350, 78.1300);
        EBike b5 = bike("EB-005", "E-Bike X3", 75, "AVAILABLE", 9.9200, 78.1150);
        bikeRepo.saveAll(java.util.List.of(b1, b2, b3, b4, b5));

        // ---- Riders ----
        Rider r1 = rider("Rohit Sharma", "rohit@ebike.com", "9876543210");
        Rider r2 = rider("Priya Nair", "priya@ebike.com", "9876543211");
        Rider r3 = rider("Amit Kumar", "amit@ebike.com", "9876543212");
        Rider r4 = rider("Sneha R", "sneha@ebike.com", "9876543213");
        riderRepo.saveAll(java.util.List.of(r1, r2, r3, r4));

        // ---- Rides ----
        rideRepo.save(ride(r1.getId(), b1.getId(), 12, 24, "COMPLETED"));
        rideRepo.save(ride(r2.getId(), b3.getId(), 8, 16, "COMPLETED"));
        rideRepo.save(ride(r3.getId(), b4.getId(), 15, 30, "ONGOING"));
        rideRepo.save(ride(r4.getId(), b2.getId(), 10, 20, "COMPLETED"));

        // ---- Vendors & Sponsors ----
        Vendor v1 = vendor("BikeParts Co.", "sales@bikeparts.com", "9000000001", "Industrial Area, Phase 1");
        Vendor v2 = vendor("GreenTech Repairs", "info@greentech.com", "9000000002", "Sector 5");
        Vendor v3 = vendor("Urban Wheels", "contact@urbanwheels.com", "9000000003", "Downtown");
        vendorRepo.saveAll(java.util.List.of(v1, v2, v3));

        CorporateSponsor sp1 = sponsor("Green City Corp", "Anita Desai", "anita@greencity.com");
        CorporateSponsor sp2 = sponsor("EcoMove Ventures", "Karan Mehta", "karan@ecomove.com");
        sponsorRepo.saveAll(java.util.List.of(sp1, sp2));

        // ---- Products ----
        Product p1 = product("Li-ion Battery Pack", "SPARE_PART", 4500, "unit");
        Product p2 = product("Bike Tyre", "SPARE_PART", 800, "unit");
        Product p3 = product("Dock Charger Unit", "EQUIPMENT", 12000, "unit");
        productRepo.saveAll(java.util.List.of(p1, p2, p3));

        // ---- Purchase Orders ----
        PurchaseOrder po1 = purchaseOrder(v1.getId(), "Li-ion Battery Pack", 10, 15000, "PENDING", LocalDate.of(2025, 6, 20));
        PurchaseOrder po2 = purchaseOrder(v2.getId(), "Dock Charger Unit", 2, 8000, "APPROVED", LocalDate.of(2025, 5, 18));
        PurchaseOrder po3 = purchaseOrder(v3.getId(), "Bike Tyre", 15, 12000, "DELIVERED", LocalDate.of(2025, 5, 15));
        poRepo.saveAll(java.util.List.of(po1, po2, po3));

        // ---- Vendor Bills ----
        vendorBillRepo.save(vendorBill(po1.getId(), v1.getId(), 15000, "UNPAID", LocalDate.of(2025, 6, 21)));
        vendorBillRepo.save(vendorBill(po3.getId(), v3.getId(), 12000, "PAID", LocalDate.of(2025, 5, 16)));

        // ---- Sales Orders & Invoices ----
        SalesOrder so1 = salesOrder(sp1.getId(), "Fleet Sponsorship Package", 1, 50000, "CONFIRMED", LocalDate.of(2025, 6, 1));
        salesOrderRepo.save(so1);
        invoiceRepo.save(invoice(so1.getId(), sp1.getId(), 50000, "PAID", LocalDate.of(2025, 6, 2)));
        invoiceRepo.save(invoice(null, sp2.getId(), 24000, "UNPAID", LocalDate.of(2025, 6, 10)));

        // ---- Payments ----
        paymentRepo.save(payment("INCOME", "RIDE", 1L, 24, "UPI", LocalDate.of(2025, 6, 20)));
        paymentRepo.save(payment("INCOME", "INVOICE", 1L, 50000, "BANK_TRANSFER", LocalDate.of(2025, 6, 2)));
        paymentRepo.save(payment("EXPENSE", "VENDOR_BILL", 1L, 15000, "BANK_TRANSFER", LocalDate.of(2025, 6, 21)));

        // ---- Accounting: Accounts, Journals, Ledger ----
        Account a1 = account("1000", "Cash & Bank", "ASSET", 330000);
        Account a2 = account("2000", "Accounts Payable", "LIABILITY", 27000);
        Account a3 = account("3000", "Owner's Equity", "EQUITY", 303000);
        Account a4 = account("4000", "Ride Revenue", "REVENUE", 248000);
        Account a5 = account("5000", "Maintenance Expense", "EXPENSE", 152000);
        accountRepo.saveAll(java.util.List.of(a1, a2, a3, a4, a5));

        Journal j1 = journal("General Journal", "GENERAL");
        Journal j2 = journal("Sales Journal", "SALES");
        journalRepo.saveAll(java.util.List.of(j1, j2));

        journalEntryRepo.save(journalEntry(j1.getId(), "Battery purchase", 0, 15000, LocalDate.of(2025, 6, 20)));
        journalEntryRepo.save(journalEntry(j2.getId(), "Sponsorship invoice", 50000, 0, LocalDate.of(2025, 6, 2)));

        ledgerRepo.save(ledgerEntry(a1.getId(), "Opening balance", 330000, 0, LocalDate.of(2025, 6, 1)));
        ledgerRepo.save(ledgerEntry(a5.getId(), "Vendor bill payment", 15000, 0, LocalDate.of(2025, 6, 21)));

        // ---- Budgets ----
        AnalyticAccount an1 = analytic("Micromobility Sector", "Fleet-wide operating budget");
        analyticRepo.save(an1);
        budgetRepo.save(budget(an1.getId(), 500000, 330000, "Aug 2025"));
    }

    private User user(String name, String email, String rawPassword, String role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(role);
        return u;
    }

    private DockStation station(String name, String location, int total, int available, int charging) {
        DockStation s = new DockStation();
        s.setName(name);
        s.setLocation(location);
        s.setTotalSlots(total);
        s.setAvailableSlots(available);
        s.setChargingSlots(charging);
        return s;
    }

    private EBike bike(String code, String model, int battery, String status, double lat, double lng) {
        EBike b = new EBike();
        b.setBikeCode(code + " (" + model + ")");
        b.setBatteryPercent(battery);
        b.setStatus(status);
        b.setLatitude(lat);
        b.setLongitude(lng);
        return b;
    }

    private Rider rider(String name, String email, String phone) {
        Rider r = new Rider();
        r.setName(name);
        r.setEmail(email);
        r.setPhone(phone);
        return r;
    }

    private Ride ride(Long riderId, Long bikeId, long durationMinutes, double fare, String status) {
        Ride r = new Ride();
        r.setRiderId(riderId);
        r.setBikeId(bikeId);
        r.setStartTime(LocalDateTime.now().minusMinutes(durationMinutes + 30));
        if (!"ONGOING".equals(status)) {
            r.setEndTime(LocalDateTime.now().minusMinutes(30));
        }
        r.setDurationMinutes(durationMinutes);
        r.setFare(fare);
        r.setStatus(status);
        return r;
    }

    private Vendor vendor(String name, String email, String phone, String address) {
        Vendor v = new Vendor();
        v.setName(name);
        v.setEmail(email);
        v.setPhone(phone);
        v.setAddress(address);
        return v;
    }

    private CorporateSponsor sponsor(String company, String contact, String email) {
        CorporateSponsor s = new CorporateSponsor();
        s.setCompanyName(company);
        s.setContactName(contact);
        s.setEmail(email);
        return s;
    }

    private Product product(String name, String type, double price, String unit) {
        Product p = new Product();
        p.setName(name);
        p.setType(type);
        p.setUnitPrice(price);
        p.setUnit(unit);
        return p;
    }

    private PurchaseOrder purchaseOrder(Long vendorId, String productName, int qty, double amount, String status, LocalDate date) {
        PurchaseOrder po = new PurchaseOrder();
        po.setVendorId(vendorId);
        po.setProductName(productName);
        po.setQuantity(qty);
        po.setTotalAmount(amount);
        po.setStatus(status);
        po.setOrderDate(date);
        return po;
    }

    private VendorBill vendorBill(Long poId, Long vendorId, double amount, String status, LocalDate date) {
        VendorBill vb = new VendorBill();
        vb.setPurchaseOrderId(poId);
        vb.setVendorId(vendorId);
        vb.setAmount(amount);
        vb.setStatus(status);
        vb.setBillDate(date);
        return vb;
    }

    private SalesOrder salesOrder(Long sponsorId, String productName, int qty, double amount, String status, LocalDate date) {
        SalesOrder so = new SalesOrder();
        so.setSponsorId(sponsorId);
        so.setProductName(productName);
        so.setQuantity(qty);
        so.setTotalAmount(amount);
        so.setStatus(status);
        so.setOrderDate(date);
        return so;
    }

    private CustomerInvoice invoice(Long salesOrderId, Long sponsorId, double amount, String status, LocalDate date) {
        CustomerInvoice inv = new CustomerInvoice();
        inv.setSalesOrderId(salesOrderId);
        inv.setSponsorId(sponsorId);
        inv.setAmount(amount);
        inv.setStatus(status);
        inv.setInvoiceDate(date);
        return inv;
    }

    private Payment payment(String type, String refType, Long refId, double amount, String method, LocalDate date) {
        Payment p = new Payment();
        p.setPaymentType(type);
        p.setReferenceType(refType);
        p.setReferenceId(refId);
        p.setAmount(amount);
        p.setMethod(method);
        p.setPaymentDate(date);
        return p;
    }

    private Account account(String code, String name, String category, double balance) {
        Account a = new Account();
        a.setCode(code);
        a.setName(name);
        a.setCategory(category);
        a.setBalance(balance);
        return a;
    }

    private Journal journal(String name, String type) {
        Journal j = new Journal();
        j.setName(name);
        j.setType(type);
        return j;
    }

    private JournalEntry journalEntry(Long journalId, String desc, double debit, double credit, LocalDate date) {
        JournalEntry je = new JournalEntry();
        je.setJournalId(journalId);
        je.setDescription(desc);
        je.setDebit(debit);
        je.setCredit(credit);
        je.setEntryDate(date);
        return je;
    }

    private LedgerEntry ledgerEntry(Long accountId, String desc, double debit, double credit, LocalDate date) {
        LedgerEntry le = new LedgerEntry();
        le.setAccountId(accountId);
        le.setDescription(desc);
        le.setDebit(debit);
        le.setCredit(credit);
        le.setEntryDate(date);
        return le;
    }

    private AnalyticAccount analytic(String name, String description) {
        AnalyticAccount a = new AnalyticAccount();
        a.setName(name);
        a.setDescription(description);
        return a;
    }

    private Budget budget(Long analyticAccountId, double allocated, double actual, String period) {
        Budget b = new Budget();
        b.setAnalyticAccountId(analyticAccountId);
        b.setAllocatedAmount(allocated);
        b.setActualAmount(actual);
        b.setPeriod(period);
        return b;
    }
}
