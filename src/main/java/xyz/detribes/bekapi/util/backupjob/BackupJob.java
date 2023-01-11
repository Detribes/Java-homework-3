package xyz.detribes.bekapi.util.backupjob;

import org.slf4j.*;
import xyz.detribes.bekapi.repository.*;
import xyz.detribes.bekapi.restorepoint.*;
import xyz.detribes.bekapi.util.factory.*;

import java.util.*;

/**
 * Собственно виновник торжества. Класс который создаёт на основании добавленных в него
 * JobObject'ов новый рестор поинт, добавляет его в репозиторий и через вызов
 * рестор-поинтовского метода save() сохраняет все данные туда куда умеет сохранять этот рестор-поинт.
 * Ах да, у меня за сохранение данных отвечают исключительно сами рестор-поинты. Не знаю
 * насколько это по заданию, но там вроде нигде не сказано как конкретно это должно происходить:)
 */
public class BackupJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackupJob.class);

    private final String name;
    private final List<JobObject> jobs = new ArrayList<>();
    private final Repository repo;

    /**
     * Бекап джоба при создании принимает в конструктор: имя и репозиторий с которым она работает
     * То, какой тип рестор-поинтов будет создаваться контролируется в репозитории. Да, в
     * задаче сказано, что надо через бекап-джобу, но я посчитал что можно и по другому,
     * особенно если в бекап-джобой никто не будет работать напрямую (хз реализовал ли я это или нет).
     */
    public BackupJob(String name,
                     Repository repo) {
        this.name = name;
        this.repo = repo;
    }

    public void run() {
        /*
         Создаём новый рестор-поинт через фабрику. Какой будет создан рестор-поинт определяется
         на основании значения repo.getRestorePointType()
         */
        RestorePoint restorePoint = RestorePointFactory.create(
                repo,
                repo.getRestorePointType(),
                name);

        //Добавляем рестор-поинт в репозиторий.
        repo.addRestorePoint(restorePoint.getName(),
                restorePoint);

        //Добавляем в рестор-поинт новые стораджи который вытаскиеваем из списка JobObject'ов в переменной jobs
        jobs.forEach(j -> restorePoint.addNewStorage(j.getStorage().getName(), j.getStorage()));

        /*
         Вызывает у рестор-поинта метод save() чтобы сам же рестор-поинт как он умеет записал
         их туда куда ему надо
         */

        restorePoint.save();
    }

    public void addJob(JobObject job) {
        jobs.add(job);
    }

    public void removeJob(JobObject job) {
        jobs.remove(job);
    }
}