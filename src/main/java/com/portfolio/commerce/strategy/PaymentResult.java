package com.portfolio.commerce.strategy;

public record PaymentResult(boolean approved, String providerReference, String reason) {

    public static PaymentResult approved(String providerReference) {
        return new PaymentResult(true, providerReference, null);
    }

    public static PaymentResult refused(String reason) {
        return new PaymentResult(false, null, reason);
    }
}
