package ldzero.ai.simpleweather.db.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * database migration from version 1 to version 2
 * Created on 2018/4/17.
 *
 * @author ldzero
 */
public class AppMigration_1_2 extends Migration {

    public AppMigration_1_2() {
        this(1, 2);
    }

    private AppMigration_1_2(int startVersion, int endVersion) {
        super(startVersion, endVersion);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE 'now_weather' ADD COLUMN 'lastUpdateDbTime' INTEGER DEFAULT 0 NOT NULL");
    }
}