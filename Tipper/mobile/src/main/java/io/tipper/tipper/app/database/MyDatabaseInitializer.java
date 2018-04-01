package io.tipper.tipper.app.database;

import android.database.sqlite.SQLiteDatabase;

import com.gani.lib.database.DatabaseInitializer;
import com.gani.lib.database.GDataProvider;
import com.gani.lib.database.GDbTable;

public class MyDatabaseInitializer extends DatabaseInitializer {
  private static final int DATABASE_VERSION = 1;

  public MyDatabaseInitializer() {
    super(DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    for (GDbTable table : GDataProvider.registeredTables()) {
      createTables(database, table);
    }
  }
}
