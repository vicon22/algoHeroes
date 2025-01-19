package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Для каждой из строк мы совершаем один проход O(m * n) в худшем случае,
// где n - количество ячеек. Общее время O(m * n)
// Обычно m = 3 и сложность сводится к O(n).

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        for (List<Unit> row : unitsByRow) {
            if (isLeftArmyTarget) {
                // Если атакуется левая армия (компьютер), ищем "самый правый" живой юнит
                row.stream()
                        .filter(Unit::isAlive)
                        .min(Comparator.comparingInt(Unit::getyCoordinate))
                        .ifPresent(suitableUnits::add);
            } else {
                // Если атакуется правая армия (игрок), ищем "самый левый" живой юнит
                row.stream()
                        .filter(Unit::isAlive)
                        .max(Comparator.comparingInt(Unit::getyCoordinate))
                        .ifPresent(suitableUnits::add);
            }
        }

        return suitableUnits;
    }
}


