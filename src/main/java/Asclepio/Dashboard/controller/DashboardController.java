package Asclepio.Dashboard.controller;

import Asclepio.Dashboard.DashboardService;
import Asclepio.Dashboard.controller.api.DashboardApi;
import Asclepio.Dashboard.dto.DashboardFinanceiroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class DashboardController implements DashboardApi {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<DashboardFinanceiroResponse> financeiro(LocalDate inicio, LocalDate fim, Long lojaId) {
        return ResponseEntity.ok(service.financeiro(inicio, fim, lojaId));
    }
}
