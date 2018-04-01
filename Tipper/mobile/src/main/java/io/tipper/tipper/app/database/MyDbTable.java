package io.tipper.tipper.app.database;

import android.database.Cursor;

import com.gani.lib.database.GDbTable;

public abstract class MyDbTable extends GDbTable<MyDbCursor> {
  @Override
  protected MyDbCursor createCursor(Cursor cursor) {
    return new MyDbCursor(cursor);
  }
}
