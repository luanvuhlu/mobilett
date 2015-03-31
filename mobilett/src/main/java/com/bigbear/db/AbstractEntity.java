package com.bigbear.db;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class AbstractEntity {
	protected long id;
	protected String whereQuery;
	protected String order;

	public abstract ContentValues toValue(boolean hasId) throws Exception;

	public String getKeyIDName() {
		return "ID";
	}

	public abstract  String getTableName();

	public abstract String getCreateTableQuery();

	public abstract String[] getColumnNames();

	public long getId() {
		return this.id;
	}

	public AbstractEntity() {

	}

	public AbstractEntity(long id) {
		this.id = id;
	}

	public String getWhereQuery() {
		return this.whereQuery;
	}

	public String getOrder() {
		return this.order;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setWhereQuery(String whereQuery) {
		this.whereQuery = whereQuery;
	}

	public abstract void setValue(Cursor rs);

	public AbstractEntity setDetail(TimeTableDBAdapter adapter) throws Exception {
		return adapter.getEntryById(this);
	}
}
