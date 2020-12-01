package com.highershine.portal.common.entity.vo;

import java.io.Serializable;

public class DnaPersonResultVo implements Serializable {

    //id
    private String id;
    //相片
    private String xp;
    //姓名
    private String xm;
    //性别
    private String xb;
    private String xbValue;
    //身份证号
    private String sfzh;
    //民族
    private String mz;
    private String mzValue;
    //户口所在地
    private String hkszd;
    private String hkszdValue;
    //出生日期
    private String csrq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXp() {
        return xp;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getXbValue() {
        return xbValue;
    }

    public void setXbValue(String xbValue) {
        this.xbValue = xbValue;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getMzValue() {
        return mzValue;
    }

    public void setMzValue(String mzValue) {
        this.mzValue = mzValue;
    }

    public String getHkszd() {
        return hkszd;
    }

    public void setHkszd(String hkszd) {
        this.hkszd = hkszd;
    }

    public String getHkszdValue() {
        return hkszdValue;
    }

    public void setHkszdValue(String hkszdValue) {
        this.hkszdValue = hkszdValue;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    @Override
    public String toString() {
        return "DnaPersonResultVO{" +
                "id='" + id + '\'' +
                ", xp='" + xp + '\'' +
                ", xm='" + xm + '\'' +
                ", xb='" + xb + '\'' +
                ", xbValue='" + xbValue + '\'' +
                ", sfzh='" + sfzh + '\'' +
                ", mz='" + mz + '\'' +
                ", mzValue='" + mzValue + '\'' +
                ", hkszd='" + hkszd + '\'' +
                ", hkszdValue='" + hkszdValue + '\'' +
                ", csrq='" + csrq + '\'' +
                '}';
    }
}
