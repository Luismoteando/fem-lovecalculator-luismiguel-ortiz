package es.upm.miw.lovecalculator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoveCalculator {

    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("sname")
    @Expose
    private String sname;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("result")
    @Expose
    private String result;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
