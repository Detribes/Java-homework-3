package xyz.detribes.bekapi.util.factory;

import org.slf4j.*;
import xyz.detribes.bekapi.repository.FSRepository;
import xyz.detribes.bekapi.repository.Repository;
import xyz.detribes.bekapi.util.factory.enums.RepositoryType;
import xyz.detribes.bekapi.util.factory.enums.RestorePointType;
import xyz.nasrally.domashka.repository.*;
import xyz.nasrally.domashka.util.factory.enums.*;

import java.nio.file.*;

/**
 * Фабрика используемая для создания репозиториев.
 * Доступен всего 1 вид. Для создания нужно дополнительно передать в качестве аргумента
 * объект типа Path который хранит путь к корню репозитория.
 * Object... args означает переменной число аргументов, т.е. после второго аргумента (name)
 * можно передать сколько угодно других. Внутри функции args является массивом, т.к.
 * при вызове Java упаковывает все эти аргументы в массив. Т.к. "невозможно" заранее узнать
 * типы передаваемых аргументов - используется тип Object, а от него уже внутри всё дальше
 * кастится обратно к ожидаемым типам. По хорошему тут должны быть проверки на эти типы, но
 * дабы упростить понимание кода я их опустил. Тебе нужны будут тесты, поэтому ты их наверное
 * добавь (т.е. на плюсах, но я хз как там). Я хз насколько это адекватное решение с точки
 * зрения профессиональных программистов, но фабрика с var-args'ами для меня выглядит самым
 * простым решением (но я понима. что оно далеко не самое безопасное). Собственно то же самое
 * относится к другим фабрикам.
 *
 */
public class RepositoryFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryFactory.class);

    public static Repository create(RepositoryType repoType,
                                    RestorePointType restoreType,
                                    String name,
                                    Object... args) {
        Repository result = null;
        switch (repoType) {
            case FS:
                result = new FSRepository(name, (Path) args[0], restoreType);
                break;
        }

        return result;
    }
}