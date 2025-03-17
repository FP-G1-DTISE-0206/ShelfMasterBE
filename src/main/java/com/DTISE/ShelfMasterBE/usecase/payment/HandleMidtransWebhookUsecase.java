package com.DTISE.ShelfMasterBE.usecase.payment;

import java.util.Map;

public interface HandleMidtransWebhookUsecase {
    void execute(Map<String, Object> payload);
}
