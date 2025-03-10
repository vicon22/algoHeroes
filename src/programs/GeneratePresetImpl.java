package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

// Сортировка списка unitList имеет временную сложность O(n * log(n)), где n - количество юнитов в списке.

// Итерация по listOfUnits:
// Внешний цикл выполняется n раз.
// Внутренний цикл выполняется не более 11 раз для каждой единицы (константное количество итераций).
// Итоговая сложность данной части - O(n), так как внутренний цикл ограничен константой.

// Итоговая сложность алгоритма - O(n * log(n)), где n - количество единиц в listOfUnits
public class GeneratePresetImpl implements GeneratePreset {

    private static final int START_FIELD_WIDTH = 3;
    private static final int START_FIELD_LENGTH = 21;

    private static final Random random = new Random();

    private Set<String> occupiedCoordinates;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        System.out.println("Начало генерации");
        // Создаем новый объект армии
        Army computerArmy = new Army();
        int currentPoints = 0;

        // Множество занятых координат
        occupiedCoordinates = new HashSet<>();

        // Сортируем юнитов по двум критериям эффективность атаки и здоровья к стоимости
        unitList.sort(Comparator.comparingDouble(
                unit -> (unit.getBaseAttack() / (double) unit.getCost()
                        + unit.getHealth() / (double) unit.getCost())));

        for (Unit unit : unitList) {
            int unitCount = 0;

            // Пока возможно добавляем юнитов
            while (unitCount < 11 && currentPoints + unit.getCost() <= maxPoints) {

                int[] coordinates = getCoordinates();

                Unit newUnit = createUnit(unit, unitCount, coordinates);

                computerArmy.getUnits().forEach(unit1 -> System.out.println((unit1.getName() + " x:" + unit1.getxCoordinate() + " y:" +
                        unit1.getyCoordinate())));

                computerArmy.getUnits().add(newUnit);
                currentPoints += newUnit.getCost();
                unitCount++;
            }
        }

        computerArmy.setPoints(currentPoints);
        System.out.println("Конец генерации");
        return computerArmy;
    }

    // Генерация координаты
    private int[] getCoordinates() {
        int[] coordinates = new int[2];

        int x, y;
        String coordinateKey;
        do {
            x = random.nextInt(START_FIELD_WIDTH);
            y = random.nextInt(START_FIELD_LENGTH);
            coordinateKey = x + "_" + y;
        } while (occupiedCoordinates.contains(coordinateKey));

        occupiedCoordinates.add(coordinateKey);

        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
    }

    private Unit createUnit(Unit unit, int unitCount, int[] coordinates) {
        return new Unit(
                unit.getUnitType() + " " + unitCount,
                unit.getUnitType(),
                unit.getHealth(),
                unit.getBaseAttack(),
                unit.getCost(),
                unit.getAttackType(),
                new HashMap<>(unit.getAttackBonuses()),
                new HashMap<>(unit.getDefenceBonuses()),
                coordinates[0],
                coordinates[1]);
    }

}