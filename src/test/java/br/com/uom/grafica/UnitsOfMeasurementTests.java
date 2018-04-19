package br.com.uom.grafica;

import org.junit.Test;
import tec.uom.se.quantity.Quantities;
import tec.uom.se.unit.MetricPrefix;
import tec.uom.se.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.*;

import static org.assertj.core.api.Assertions.assertThat;
import static tec.uom.se.unit.MetricPrefix.KILO;
import static tec.uom.se.unit.Units.GRAM;
import static tec.uom.se.unit.Units.KILOGRAM;
import static tec.uom.se.unit.Units.METRE;

public class UnitsOfMeasurementTests {

    @Test
    public void should_create_units() {
        Quantity<Length> width = Quantities.getQuantity(0.21, Units.METRE);

        System.out.println(width);
        System.out.println(width.to(MetricPrefix.CENTI(Units.METRE)));
        System.out.println(width.to(MetricPrefix.MILLI(Units.METRE)));
    }

    @Test
    public void should_calc_units() {
        Quantity<Length> width = Quantities.getQuantity(0.21, Units.METRE);
        Quantity<Length> height = Quantities.getQuantity(0.297, Units.METRE);

        Quantity<Area> area = width.multiply(height).asType(Area.class);
        System.out.println(area);

        Unit<Length> centimetre = MetricPrefix.CENTI(Units.METRE);
        System.out.println(width.to(centimetre).multiply(height).asType(Area.class));
        System.out.println(width.to(centimetre).multiply(height.to(centimetre)).asType(Area.class));
    }

    @Test
    public void should_calc_price_by_weight() {
        int kilogramValue = 4;

        Quantity<Mass> grammage = Quantities.getQuantity(200, Units.GRAM);
        Quantity<Length> width = Quantities.getQuantity(0.21, Units.METRE);
        Quantity<Length> height = Quantities.getQuantity(0.297, Units.METRE);
        Quantity<Area> area = width.multiply(height).asType(Area.class);

        Quantity<Mass> areaWeight = grammage.multiply(area.getValue()).to(KILOGRAM);

        System.out.println(areaWeight);
        System.out.println(areaWeight.to(GRAM));
        System.out.println(areaWeight.getValue().doubleValue() * kilogramValue);
    }

    @Test
    public void calc_speed() {
        Quantity<Length> distance = Quantities.getQuantity(100, KILO(METRE));
        Quantity<Time> time = Quantities.getQuantity(1, Units.HOUR);

        Quantity<Speed> result = distance.divide(time).asType(Speed.class);
        System.out.println(result);
    }

    @Test
    public void calc_distance() {
        Quantity<Speed> speed = Quantities.getQuantity(100, Units.KILOMETRE_PER_HOUR);
        Quantity<Time> time = Quantities.getQuantity(2, Units.HOUR);

        Quantity<Length> result = speed.multiply(time).asType(Length.class).to(KILO(METRE));
        System.out.println(result);
    }

    @Test
    public void should_calc_fuel_consumption() {
        Quantity<Volume> fuel = Quantities.getQuantity(70, Units.LITRE);
        Quantity<Length> distance = Quantities.getQuantity(1000, KILO(METRE));

        Quantity<?> result = distance.divide(fuel);
        assertThat(result.getUnit().toString()).isEqualTo("km/l");
        assertThat(result.getValue().intValue()).isEqualTo(14);
        System.out.println(result);

    }

}
