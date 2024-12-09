package com.ev.ocpp16.domain.transaction.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev.ocpp16.domain.transaction.dto.api.ChgrErrorHstQueryDTO;
import com.ev.ocpp16.domain.transaction.dto.api.ChgrHstQueryDTO;
import com.ev.ocpp16.domain.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * GET /api/v1/transactions
     * 충전 이력 조회
     */
    @GetMapping("")
    public ResponseEntity<List<ChgrHstQueryDTO.Response>> getTransactions(
            @Validated @ModelAttribute ChgrHstQueryDTO.Request request) {
        return ResponseEntity.ok(transactionService.getTransactions(request));
    }

    /**
     * GET /api/v1/transactions/errors
     * 오류 이력 조회
     */
    @GetMapping("/errors")
    public ResponseEntity<List<ChgrErrorHstQueryDTO.Response>> getChargerErrors(
            @ModelAttribute ChgrErrorHstQueryDTO.Request request) {
        return null;
    }
}
