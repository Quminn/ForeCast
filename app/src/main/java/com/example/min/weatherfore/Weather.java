package com.example.min.weatherfore;
import java.io.Serializable;
public class Weather implements Serializable {
    private static final long serialVersionUID = 5403208217014231126L;
    public String getmWin() {
        return mWin;
    }
    public void setmWin(String mWin) {
        this.mWin = mWin;
    }
    private String mDate;
    private String mL;
    public String getmCode() {
        return mCode;
    }
    public void setmCode(String mCode) {
        this.mCode = mCode;
    }
    private String mCode;
    public String getmPre() {
        return mPre;
    }
    public void setmPre(String mPre) {
        this.mPre = mPre;
    }
    private String mH;
    private String mState;
    public String getmHum() {
        return mHum;
    }
    public void setmHum(String mHum) {
        this.mHum = mHum;
    }
    private String mHum;
    private String mPre;
    private String mWin;
    public String getmState() {
        return mState;
    }
    public String getmH() {
        return mH;
    }
    public String getmDate() {
        return mDate;
    }
    public String getmL() {
        return mL;
    }
    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
    public void setmH(String mH) {
        this.mH = mH;
    }
    public void setmState(String mState) {
        this.mState = mState;
    }
    public void setmL(String mL) {
        this.mL = mL;
    }
}
