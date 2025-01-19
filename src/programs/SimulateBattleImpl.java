package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

// В каждом раунде сортируем n юнитов за O(n * log(n))
// Симуляция раунда: O(n)

// Итоговая сложность в худшем случае O(n^2 * log(n))

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        System.out.println("Симуляция начата");
        // Получаем юниты армий
        List<Unit> playerUnits = playerArmy.getUnits();
        List<Unit> computerUnits = computerArmy.getUnits();

        // Бой продолжается пока есть живые юниты в обеих армиях
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {
            // Сортируем юниты по убыванию атаки
            sortUnits(playerUnits);
            sortUnits(computerUnits);

            // Симулируем раунд
            simulateRound(playerUnits, computerUnits);

            // Удаляем погибших юнитов
            removeDeadUnits(playerUnits);
            removeDeadUnits(computerUnits);
        }

        System.out.println("Симуляция окончена");
    }

    private boolean hasAliveUnits(List<Unit> units) {
        return units.stream().anyMatch(Unit::isAlive);
    }

    private void sortUnits(List<Unit> units) {
        units.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
    }

    private void simulateRound(List<Unit> playerUnits, List<Unit> computerUnits) throws InterruptedException {
        List<Unit> allUnits = new LinkedList<>();

        // Добавляем всех юнитов в очередь ходов
        allUnits.addAll(playerUnits);
        allUnits.addAll(computerUnits);

        for (Unit currentUnit : allUnits) {
            // Если юнит мертв пропускаем ход
            if (!currentUnit.isAlive()) {
                continue;
            }

            // Определяем цель атаки
            Unit target = currentUnit.getProgram().attack();

            // Если цель атаки существует атаку
            if (target != null) {
                attack(currentUnit, target);
            }
        }
    }

    private void attack(Unit attacker, Unit target) {
        // Уменьшаем здоровье атакуемого юнита
        int health = target.getHealth() - attacker.getBaseAttack();
        target.setHealth(health);

        // Помечаем мертвого юнита
        if (target.getHealth() <= 0) {
            target.setAlive(false);
        }

        // Логируем атаку
        printBattleLog.printBattleLog(attacker, target);
    }

    private void removeDeadUnits(List<Unit> units) {
        units.removeIf(unit -> !unit.isAlive());
    }
}