package io.tipper.tipper.app.database;

import android.database.Cursor;

import com.gani.lib.database.GDbCursor;

public class MyDbCursor extends GDbCursor implements MyDbData {
  public MyDbCursor(Cursor cursor) {
    super(cursor);
  }

  public <T> T executeFirstRow(AutoCleanupCommand<T> command) {
    return super.executeFirstRow(command);
  }

  public <T> T executeFirstRowIfExist(AutoCleanupCommand<T> command) {
    return super.executeFirstRowIfExist(command);
  }



  public interface AutoCleanupCommand<T> extends GDbCursor.AutoCleanupCommand<T, MyDbCursor> {
    T execute(MyDbCursor cursor);
  }
}
