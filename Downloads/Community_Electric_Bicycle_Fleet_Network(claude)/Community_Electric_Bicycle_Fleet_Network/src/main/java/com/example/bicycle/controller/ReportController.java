package com.example.bicycle.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bicycle.repository.AccountRepository;
import com.example.bicycle.repository.BudgetRepository;
import com.example.bicycle.repository.CustomerInvoiceRepository;
import com.example.bicycle.repository.DockStationRepository;
import com.example.bicycle.repository.EBikeRepository;
import com.example.bicycle.repository.PaymentRepository;
import com.example.bicycle.repository.RideRepository;
import com.example.bicycle.repository.VendorBillRepository;

/**
 * Aggregation / analytics REST API.
 * GET /api/reports/dashboard    -> KPI summary for the dashboard cards
 * GET /api/reports/profit-loss  -> revenue, expenses, net profit
 * GET /api/reports/balance-sheet-> assets/liabilities/equity style account totals
 * GET /api/reports/budget       -> allocated vs actual spend
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final EBikeRepository bikes;
    private final DockStationRepository stations;
    private final RideRepository rides;
    private final PaymentRepository payments;
    private final CustomerInvoiceRepository invoices;
    private final VendorBillRepository vendorBills;
    private final BudgetRepository budgets;
    private final AccountRepository accounts;

    public ReportController(EBikeRepository bikes, DockStationRepository stations, RideRepository rides,
                             PaymentRepository payments, CustomerInvoiceRepository invoices,
                             VendorBillRepository vendorBills, BudgetRepository budgets, AccountRepository accounts) {
        this.bikes = bikes;
        this.stations = stations;
        this.rides = rides;
        this.payments = payments;
        this.invoices = invoices;
        this.vendorBills = vendorBills;
        this.budgets = budgets;
        this.accounts = accounts;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        long totalBikes = bikes.count();
        long activeRides = rides.findAll().stream().filter(r -> "ACTIVE".equalsIgnoreCase(r.getStatus())).count();
        long totalStations = stations.count();
        double totalRevenue = payments.findAll().stream().mapToDouble(p -> p.getAmount()).sum();
        long healthy = bikes.findAll().stream().filter(b -> b.getBatteryPercent() >= 30).count();
        long low = bikes.findAll().stream().filter(b -> b.getBatteryPercent() < 20).count();
        long charging = bikes.findAll().stream().filter(b -> "CHARGING".equalsIgnoreCase(b.getStatus())).count();

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalBikes", totalBikes);
        m.put("activeRides", activeRides);
        m.put("totalStations", totalStations);
        m.put("totalRevenue", totalRevenue);
        m.put("batteryHealthy", healthy);
        m.put("batteryLow", low);
        m.put("batteryCharging", charging);
        return m;
    }

    @GetMapping("/profit-loss")
    public Map<String, Object> profitLoss() {
        double revenue = invoices.findAll().stream().mapToDouble(i -> i.getAmount()).sum()
                + payments.findAll().stream().filter(p -> "INCOME".equalsIgnoreCase(p.getPaymentType())).mapToDouble(p -> p.getAmount()).sum();
        double expenses = vendorBills.findAll().stream().mapToDouble(v -> v.getAmount()).sum();
        double netProfit = revenue - expenses;
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalRevenue", revenue);
        m.put("totalExpenses", expenses);
        m.put("netProfit", netProfit);
        m.put("hasData", revenue > 0 || expenses > 0 || netProfit != 0);
        return m;
    }

    @GetMapping("/balance-sheet")
    public Map<String, Object> balanceSheet() {
        double assets = accounts.findAll().stream().filter(a -> "ASSET".equalsIgnoreCase(a.getCategory())).mapToDouble(a -> a.getBalance()).sum();
        double liabilities = accounts.findAll().stream().filter(a -> "LIABILITY".equalsIgnoreCase(a.getCategory())).mapToDouble(a -> a.getBalance()).sum();
        double equity = accounts.findAll().stream().filter(a -> "EQUITY".equalsIgnoreCase(a.getCategory())).mapToDouble(a -> a.getBalance()).sum();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalAssets", assets);
        m.put("totalLiabilities", liabilities);
        m.put("totalEquity", equity);
        m.put("accounts", accounts.findAll());
        return m;
    }

    @GetMapping("/budget")
    public Map<String, Object> budget() {
        double allocated = budgets.findAll().stream().mapToDouble(b -> b.getAllocatedAmount()).sum();
        double actual = budgets.findAll().stream().mapToDouble(b -> b.getActualAmount()).sum();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("allocated", allocated);
        m.put("actual", actual);
        m.put("remaining", allocated - actual);
        return m;
    }
}
