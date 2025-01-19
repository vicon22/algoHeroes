package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

// Общая сложность метода оценивается как O(n * m), где n - ширина поля (WIDTH), а m - высота поля (LENGTH).
// В обычном случае это лучше, чем требуемая сложность O((n * m) * log(n * m)) для задачи поиска пути.
// в худшем случае O((n * m) * log(n * m)).

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int LENGTH = 21;
    private static final int[][] DIRECTIONS = {
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        // Инициализация матрицы поля
        int[][] field = new int[WIDTH][LENGTH];

        // Заполнение матрицы препятствий:
        // Итерация по существующим юнитам для отметки препятствий
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                field[unit.getxCoordinate()][unit.getyCoordinate()] = -1;
            }
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        Queue<Edge> queue = new LinkedList<>();
        queue.offer(new Edge(startX, startY));
        field[startX][startY] = 1;

        // Поиск пути, проходим по всем доступным клеткам поля, при этом каждая клетка проверяется один раз.
        // Это дает сложность O(n * m), где nm - количество клеток на поле.
        boolean isTargetFound = false;
        while (!queue.isEmpty() && !isTargetFound) {
            Edge current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY) && field[newX][newY] == 0) {
                    field[newX][newY] = field[x][y] + 1;
                    queue.offer(new Edge(newX, newY));

                    if (newX == targetX && newY == targetY) {
                        isTargetFound = true;
                        break;
                    }
                }
            }
        }

        if (field[targetX][targetY] == 0) {
            return new ArrayList<>();
        }

        return restorePath(field, startX, startY, targetX, targetY);
    }

    private boolean isValid(int x, int y) { return x >= 0 && x < WIDTH && y >= 0 && y < LENGTH; }

    private List<Edge> restorePath(int[][] field, int startX, int startY, int targetX, int targetY) {
        List<Edge> path = new ArrayList<>();
        int x = targetX;
        int y = targetY;

        while (!(x == startX && y == startY)) {
            path.addFirst(new Edge(x, y));

            int curValue = field[x][y];
            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY) && field[newX][newY] > 0 && field[newX][newY] < curValue) {
                    x = newX;
                    y = newY;
                    break;
                }
            }
        }

        path.addFirst(new Edge(startX, startY));
        return path;
    }
}