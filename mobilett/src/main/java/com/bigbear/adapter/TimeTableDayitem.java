package com.bigbear.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class TimeTableDayitem implements AsymmetricItem {
	public static final int HOURS=0;
	public static final int SUB_NAME=1;
	public static final int LOCAL=2;
	private int columnSpan;
    private int rowSpan;
    private int position;
    private String text;
    private int type;
    private String dayId;
    private boolean isStudy;
    private boolean isSeminar;
    

    public TimeTableDayitem(final int columnSpan, final int rowSpan,
                            int position, String text,
                            int type,
                            boolean isStudy,
                            boolean isSeminar,
                            String dayId) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
        this.text=text;
        this.type=type;
        this.isSeminar=isSeminar;
        this.isStudy=isStudy;
        this.dayId=dayId;
    }
    public TimeTableDayitem(final int columnSpan,
                            final int rowSpan,
                            int position,
                            String text,
                            int type,
                            boolean isStudy,
                            boolean isSeminar) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
        this.text=text;
        this.type=type;
        this.dayId=null;
        this.isSeminar=isSeminar;
        this.isStudy=isStudy;
    }

    public TimeTableDayitem(final Parcel in) {
        readFromParcel(in);
    }
    public String getDayId() {
		return dayId;
	}
    public void setDayId(String dayId) {
		this.dayId = dayId;
	}

    public boolean isSeminar() {
        return isSeminar;
    }

    public boolean isStudy() {
        return isStudy;
    }

    private void readFromParcel(final Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }
    public int getPosition() {
		return position;
	}
    public int getType() {
		return type;
	}
    public void setType(int type) {
		this.type = type;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
		
	}

	@Override
	public int getColumnSpan() {
		return columnSpan;
	}

	@Override
	public int getRowSpan() {
		return rowSpan;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }
	
	public static final Parcelable.Creator<TimeTableDayitem> CREATOR = new Parcelable.Creator<TimeTableDayitem>() {

        @Override
        public TimeTableDayitem createFromParcel(final Parcel in) {
            return new TimeTableDayitem(in);
        }

        @Override
        public TimeTableDayitem[] newArray(final int size) {
            return new TimeTableDayitem[size];
        }
    };

}
