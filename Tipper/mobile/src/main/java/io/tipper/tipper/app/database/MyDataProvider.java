package io.tipper.tipper.app.database;

import com.gani.lib.database.DatabaseInitializer;
import com.gani.lib.database.GDataProvider;
import com.gani.lib.logging.GLog;

public class MyDataProvider extends GDataProvider {
  @Override
  protected String authorityDomain() {
    return "io.tipper";
  }

  @Override
  protected void registerTables() {
    GLog.i(DatabaseInitializer.class, "Registering tables " );

    register(KeyValue.TABLE_HELPER);
  }
}
