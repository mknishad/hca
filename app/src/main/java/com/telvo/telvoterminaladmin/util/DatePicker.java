package com.telvo.telvoterminaladmin.util;

/**
 * Created by Invariant on 10/11/17.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePicker extends AppCompatEditText implements DatePickerDialog.OnDateSetListener {
    public interface OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int month, int day);
    }

    protected int year;
    protected int month;
    protected int day;
    protected long maxDate = -1;
    protected long minDate = -1;
    protected OnDateSetListener onDateSetListener = null;
    protected java.text.DateFormat dateFormat;

    public OnDateSetListener getOnDateSetListener() {
        return onDateSetListener;
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setFont();
        dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        setFocusable(false);
        setToday();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto.regular.ttf");
        setTypeface( normal, Typeface.NORMAL );
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        updateText();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        updateText();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        updateText();
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        updateText();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            showDatePicker();

        return super.onTouchEvent(event);
    }

    public java.text.DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(java.text.DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        updateText();
    }

    public void setMaxDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 23, 59, 59);
        this.maxDate = c.getTimeInMillis();
    }

    public void setMinDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        this.minDate = c.getTimeInMillis();
    }

    public void setToday() {
        Calendar c = Calendar.getInstance();
        this.year=c.get(Calendar.YEAR);
        this.month=c.get(Calendar.MONTH);
        this.day=c.get(Calendar.DAY_OF_MONTH);
        //setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    protected void showDatePicker() {
        DatePickerDialog datePickerDialog;

        datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                getYear(),
                getMonth(),
                getDay());

        if (this.maxDate != -1) {
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
        }
        if (this.minDate != -1) {
            datePickerDialog.getDatePicker().setMinDate(minDate);
        }

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year,
                          int month, int day) {
        setDate(year, month, day);
        clearFocus();

        if(onDateSetListener != null)
            onDateSetListener.onDateSet(this, year, month, day);
    }

    public void updateText() {
        Calendar cal = new GregorianCalendar(getYear(), getMonth(), getDay());
        setText(dateFormat.format(cal.getTime()));
    }
}
