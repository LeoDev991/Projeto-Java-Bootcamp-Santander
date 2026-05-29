package com.portfolio.commerce.singleton;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java puro: Singleton lazy, thread-safe e sem depender do container Spring.
 *
 * Uso real: cache leve de flags/configurações imutáveis ou raramente alteradas.
 * Vantagem: criação sob demanda e acesso centralizado.
 * Desvantagem: estado global dificulta testes se usado para regra mutável.
 * Erro comum: usar synchronized em todo acesso ou double-check sem volatile.
 */
public final class CheckoutConfigurationRegistry {

    private final Map<String, BigDecimal> values = new ConcurrentHashMap<>();

    private CheckoutConfigurationRegistry() {
        values.put("fraud.max-order-without-review", new BigDecimal("10000.00"));
        values.put("shipping.free-threshold", new BigDecimal("300.00"));
    }

    public static CheckoutConfigurationRegistry getInstance() {
        return Holder.INSTANCE;
    }

    public BigDecimal getDecimal(String key) {
        return values.get(key);
    }

    public void override(String key, BigDecimal value) {
        values.put(key, value);
    }

    private static final class Holder {
        private static final CheckoutConfigurationRegistry INSTANCE = new CheckoutConfigurationRegistry();
    }
}
