package com.example.bicycle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves every server-rendered (Thymeleaf) page in the application.
 * All pages share the dark dashboard layout (fragments/layout.html) and
 * pull their live data client-side from the REST API under /api/**.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model m) {
        m.addAttribute("title", "Micromobility Fleet Network");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m) {
        m.addAttribute("title", "Dashboard");
        m.addAttribute("active", "dashboard");
        return "dashboard";
    }

    @GetMapping("/bikes")
    public String bikes(Model m) {
        m.addAttribute("title", "Bikes");
        m.addAttribute("active", "bikes");
        return "bikes";
    }

    @GetMapping("/stations")
    public String stations(Model m) {
        m.addAttribute("title", "Stations");
        m.addAttribute("active", "stations");
        return "stations";
    }

    @GetMapping("/rides")
    public String rides(Model m) {
        m.addAttribute("title", "Rides");
        m.addAttribute("active", "rides");
        return "rides";
    }

    @GetMapping("/ride-details")
    public String rideDetails(Model m) {
        m.addAttribute("title", "Ride Details");
        m.addAttribute("active", "rides");
        return "ride-details";
    }

    @GetMapping("/contacts")
    public String contacts(Model m) {
        m.addAttribute("title", "Contacts");
        m.addAttribute("active", "contacts");
        return "contacts";
    }

    @GetMapping("/products")
    public String products(Model m) {
        m.addAttribute("title", "Products");
        m.addAttribute("active", "products");
        return "products";
    }

    @GetMapping("/purchase-orders")
    public String purchaseOrders(Model m) {
        m.addAttribute("title", "Purchase Orders");
        m.addAttribute("active", "purchase-orders");
        return "purchase-orders";
    }

    @GetMapping("/vendor-bills")
    public String vendorBills(Model m) {
        m.addAttribute("title", "Vendor Bills");
        m.addAttribute("active", "vendor-bills");
        return "vendor-bills";
    }

    @GetMapping("/sales-orders")
    public String salesOrders(Model m) {
        m.addAttribute("title", "Sales Orders");
        m.addAttribute("active", "sales-orders");
        return "sales-orders";
    }

    @GetMapping("/invoices")
    public String invoices(Model m) {
        m.addAttribute("title", "Invoices");
        m.addAttribute("active", "invoices");
        return "invoices";
    }

    @GetMapping("/payments")
    public String payments(Model m) {
        m.addAttribute("title", "Payments");
        m.addAttribute("active", "payments");
        return "payments";
    }

    @GetMapping("/chart-of-accounts")
    public String chartOfAccounts(Model m) {
        m.addAttribute("title", "Chart of Accounts");
        m.addAttribute("active", "accounting");
        return "chart-of-accounts";
    }

    @GetMapping("/journals")
    public String journals(Model m) {
        m.addAttribute("title", "Journals");
        m.addAttribute("active", "accounting");
        return "journals";
    }

    @GetMapping("/ledger")
    public String ledger(Model m) {
        m.addAttribute("title", "Ledger");
        m.addAttribute("active", "accounting");
        return "ledger";
    }

    @GetMapping("/budgets")
    public String budgets(Model m) {
        m.addAttribute("title", "Budgets");
        m.addAttribute("active", "budgets");
        return "budgets";
    }

    @GetMapping("/budget-report")
    public String budgetReport(Model m) {
        m.addAttribute("title", "Budget Report");
        m.addAttribute("active", "budgets");
        return "budget-report";
    }

    @GetMapping("/profit-loss")
    public String profitLoss(Model m) {
        m.addAttribute("title", "Profit & Loss");
        m.addAttribute("active", "reports");
        return "profit-loss";
    }

    @GetMapping("/balance-sheet")
    public String balanceSheet(Model m) {
        m.addAttribute("title", "Balance Sheet");
        m.addAttribute("active", "reports");
        return "balance-sheet";
    }
}
