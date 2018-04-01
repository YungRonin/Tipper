package io.tipper.tipper.app.database;

import com.gani.lib.database.GDbModel;

public class MyDbModel extends GDbModel<MyDbData, MyDbRow> {
  @Override
  protected MyDbRow createRow() {
    return new MyDbRow();
  }
}
