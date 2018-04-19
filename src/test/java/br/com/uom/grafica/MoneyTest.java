package br.com.uom.grafica;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.junit.Test;

import javax.money.*;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MoneyTest {

    private static final CurrencyUnit REAL = Monetary.getCurrency("BRL");
    private static final CurrencyUnit DOLAR = Monetary.getCurrency("USD");
    private static final CurrencyUnit IENE = Monetary.getCurrency(Locale.JAPAN);

    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    @Test
    public void should_declare_money() {
        System.out.println(Money.of(10, "BRL"));
        System.out.println(Money.of(20, REAL));
        System.out.println(Money.of(30.78292332432, DOLAR));
        System.out.println(Money.of(30.78292332432, Monetary.getCurrency(Locale.US)));
        System.out.println(Money.of(30.78292332432, Monetary.getCurrency(Locale.JAPAN)));
    }

    @Test
    public void should_format() {

        MonetaryAmountFormat formatter = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder
                        .of(Locale.US)
                        .set(CurrencyStyle.SYMBOL)
                        .build());

        System.out.println(formatter.format(Money.of(30.78292332432, DOLAR)));

        MonetaryAmountFormat realFormatter = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder
                        .of(LOCALE_PT_BR)
                        .set(CurrencyStyle.SYMBOL)
                        .build());

        System.out.println(realFormatter.format(Money.of(30.78292332432, REAL)));
    }

    @Test
    public void should_declare_FastMoney() {
        System.out.println(FastMoney.of(30.7829, DOLAR));
    }

    @Test
    public void should_declare_money_with_context() {
        MonetaryContext context = MonetaryContextBuilder.of()
                .setFixedScale(true)
                .setMaxScale(3)
                .build();
        System.out.println(Money.of(30.78292, DOLAR, context));

        MonetaryContextBuilder.of(context);
        System.out.println(Money.of(30.78292, DOLAR));
    }

    @Test
    public void should_arithmetic() {

        Money value = Money.of(20, REAL);
        Money another = Money.of(30, REAL);

        System.out.println(value.add(another));
        System.out.println(value.divide(2));
        System.out.println(value.multiply(2));
    }

    @Test(expected = MonetaryException.class)
    public void should_not_calc_with_different_currencies() {
        Money value = Money.of(20, REAL);
        Money another = Money.of(30, DOLAR);

        System.out.println(value.add(another));

    }

    @Test
    public void should_round() {
        Money gasValue = Money.of(4.389999, REAL);

        System.out.println(gasValue);
        System.out.println(gasValue.with(Monetary.getDefaultRounding()));
//        System.out.println(gasValue.with(Monetary.getRounding()));
    }

    @Test
    public void should_convert() {
        Money value = Money.of(1, DOLAR);

        CurrencyConversion toReal = MonetaryConversions.getConversion(REAL);
        System.out.println(value.with(toReal));
    }

    @Test
    public void should_use_different_providers() {
        // International Monetary Found
        ExchangeRateProvider imfRateProvider = MonetaryConversions
                .getExchangeRateProvider("IMF");
        // European Central Bank
        ExchangeRateProvider ecbRateProvider = MonetaryConversions
                .getExchangeRateProvider("ECB");

        Money value = Money.of(1, DOLAR);
        System.out.println(value.with(imfRateProvider.getCurrencyConversion(REAL)));
        System.out.println(value.with(ecbRateProvider.getCurrencyConversion(REAL)));
    }

    @Test
    public void should_use_utilities_convert() {
        List<MonetaryAmount> amounts = new ArrayList<>();
        amounts.add(Money.of(2, REAL));
        amounts.add(Money.of(42, DOLAR));
        amounts.add(Money.of(7, DOLAR));
        amounts.add(Money.of(13.37, IENE));
        amounts.add(Money.of(18, DOLAR));

        CurrencyConversion toDolar = MonetaryConversions.getConversion(DOLAR);
        amounts.stream()
                .map(v -> v.with(toDolar))
                .forEach(System.out::println);
    }

    @Test
    public void should_use_utilities() {
        List<MonetaryAmount> amounts = new ArrayList<>();
        amounts.add(Money.of(2, REAL));
        amounts.add(Money.of(42, DOLAR));
        amounts.add(Money.of(7, DOLAR));
        amounts.add(Money.of(13.37, IENE));
        amounts.add(Money.of(18, DOLAR));

        System.out.println(amounts.stream()
                .filter(MonetaryFunctions.isCurrency(DOLAR))
                .reduce(Money.of(0, DOLAR), MonetaryAmount::add));
    }


    @Test
    public void should_group() {
        List<MonetaryAmount> amounts = new ArrayList<>();
        amounts.add(Money.of(2, REAL));
        amounts.add(Money.of(42, DOLAR));
        amounts.add(Money.of(7, DOLAR));
        amounts.add(Money.of(13.37, IENE));
        amounts.add(Money.of(18, DOLAR));

        System.out.println(amounts.stream()
                .collect(MonetaryFunctions.groupByCurrencyUnit()));
    }

    @Test
    public void should_summarize() {
        List<MonetaryAmount> amounts = new ArrayList<>();
        amounts.add(Money.of(42, DOLAR));
        amounts.add(Money.of(7, DOLAR));
        amounts.add(Money.of(18, DOLAR));

        System.out.println(amounts.stream()
                .collect(MonetaryFunctions.summarizingMonetary(DOLAR)));
    }
}
