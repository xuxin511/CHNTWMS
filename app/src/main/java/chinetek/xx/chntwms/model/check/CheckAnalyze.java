package chinetek.xx.chntwms.model.check;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by 123 on 2018/6/14.
 */

public class CheckAnalyze implements Parcelable {


    private String Creater;
    private String unit;
    private Date EDATE;

    private Date SEDATE;
    private String SUPCODE;
    private String SUPNAME;
    private Date PRODUCTDATE;
    private String SUPPRDBATCH;
    private Date SUPPRDDATE;
    private String PALLETNO;

    private String BARCODE;
    private String CHECKNO;
    private String AREANO;

    private int AREAID;

    private int houseid;

    private int warehouseid;

    private String houseno;

    private String warehouseno;

    private String swarehouseno;



    private String MATERIALNO;

    private int MATERIALID;
    private String MATERIALDESC;
    private String SERIALNO;
    private String SAREANO;

    private int SAREAID;

    private String SMATERIALDESC;
    private String SMATERIALNO;

    private int SMATERIALNOID;
    private String SSERIALNO;
    private String remark;

    private float QTY;

    private float SQTY;

    //盘赢数量
    private String YQTY;

    //盘亏数量
    private String KQTY;

    private String partno;


    private String BatchNo;

    private String SBatchNo;

    private String STRONGHOLDCODE;


    private String STRONGHOLDNAME;

    private String SSTRONGHOLDCODE;

    private int status;
    private String statusname;
    private int sstatus;
    private String sstatusname;

    private String ena;

    private String dimension;


    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getEDATE() {
        return EDATE;
    }

    public void setEDATE(Date EDATE) {
        this.EDATE = EDATE;
    }

    public Date getSEDATE() {
        return SEDATE;
    }

    public void setSEDATE(Date SEDATE) {
        this.SEDATE = SEDATE;
    }

    public String getSUPCODE() {
        return SUPCODE;
    }

    public void setSUPCODE(String SUPCODE) {
        this.SUPCODE = SUPCODE;
    }

    public String getSUPNAME() {
        return SUPNAME;
    }

    public void setSUPNAME(String SUPNAME) {
        this.SUPNAME = SUPNAME;
    }

    public Date getPRODUCTDATE() {
        return PRODUCTDATE;
    }

    public void setPRODUCTDATE(Date PRODUCTDATE) {
        this.PRODUCTDATE = PRODUCTDATE;
    }

    public String getSUPPRDBATCH() {
        return SUPPRDBATCH;
    }

    public void setSUPPRDBATCH(String SUPPRDBATCH) {
        this.SUPPRDBATCH = SUPPRDBATCH;
    }

    public Date getSUPPRDDATE() {
        return SUPPRDDATE;
    }

    public void setSUPPRDDATE(Date SUPPRDDATE) {
        this.SUPPRDDATE = SUPPRDDATE;
    }

    public String getPALLETNO() {
        return PALLETNO;
    }

    public void setPALLETNO(String PALLETNO) {
        this.PALLETNO = PALLETNO;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getCHECKNO() {
        return CHECKNO;
    }

    public void setCHECKNO(String CHECKNO) {
        this.CHECKNO = CHECKNO;
    }

    public String getAREANO() {
        return AREANO;
    }

    public void setAREANO(String AREANO) {
        this.AREANO = AREANO;
    }

    public int getAREAID() {
        return AREAID;
    }

    public void setAREAID(int AREAID) {
        this.AREAID = AREAID;
    }

    public int getHouseid() {
        return houseid;
    }

    public void setHouseid(int houseid) {
        this.houseid = houseid;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getWarehouseno() {
        return warehouseno;
    }

    public void setWarehouseno(String warehouseno) {
        this.warehouseno = warehouseno;
    }

    public String getSwarehouseno() {
        return swarehouseno;
    }

    public void setSwarehouseno(String swarehouseno) {
        this.swarehouseno = swarehouseno;
    }

    public String getMATERIALNO() {
        return MATERIALNO;
    }

    public void setMATERIALNO(String MATERIALNO) {
        this.MATERIALNO = MATERIALNO;
    }

    public int getMATERIALID() {
        return MATERIALID;
    }

    public void setMATERIALID(int MATERIALID) {
        this.MATERIALID = MATERIALID;
    }

    public String getMATERIALDESC() {
        return MATERIALDESC;
    }

    public void setMATERIALDESC(String MATERIALDESC) {
        this.MATERIALDESC = MATERIALDESC;
    }

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO;
    }

    public String getSAREANO() {
        return SAREANO;
    }

    public void setSAREANO(String SAREANO) {
        this.SAREANO = SAREANO;
    }

    public int getSAREAID() {
        return SAREAID;
    }

    public void setSAREAID(int SAREAID) {
        this.SAREAID = SAREAID;
    }

    public String getSMATERIALDESC() {
        return SMATERIALDESC;
    }

    public void setSMATERIALDESC(String SMATERIALDESC) {
        this.SMATERIALDESC = SMATERIALDESC;
    }

    public String getSMATERIALNO() {
        return SMATERIALNO;
    }

    public void setSMATERIALNO(String SMATERIALNO) {
        this.SMATERIALNO = SMATERIALNO;
    }

    public int getSMATERIALNOID() {
        return SMATERIALNOID;
    }

    public void setSMATERIALNOID(int SMATERIALNOID) {
        this.SMATERIALNOID = SMATERIALNOID;
    }

    public String getSSERIALNO() {
        return SSERIALNO;
    }

    public void setSSERIALNO(String SSERIALNO) {
        this.SSERIALNO = SSERIALNO;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getQTY() {
        return QTY;
    }

    public void setQTY(float QTY) {
        this.QTY = QTY;
    }

    public float getSQTY() {
        return SQTY;
    }

    public void setSQTY(float SQTY) {
        this.SQTY = SQTY;
    }

    public String getYQTY() {
        return YQTY;
    }

    public void setYQTY(String YQTY) {
        this.YQTY = YQTY;
    }

    public String getKQTY() {
        return KQTY;
    }

    public void setKQTY(String KQTY) {
        this.KQTY = KQTY;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getSBatchNo() {
        return SBatchNo;
    }

    public void setSBatchNo(String SBatchNo) {
        this.SBatchNo = SBatchNo;
    }

    public String getSTRONGHOLDCODE() {
        return STRONGHOLDCODE;
    }

    public void setSTRONGHOLDCODE(String STRONGHOLDCODE) {
        this.STRONGHOLDCODE = STRONGHOLDCODE;
    }

    public String getSTRONGHOLDNAME() {
        return STRONGHOLDNAME;
    }

    public void setSTRONGHOLDNAME(String STRONGHOLDNAME) {
        this.STRONGHOLDNAME = STRONGHOLDNAME;
    }

    public String getSSTRONGHOLDCODE() {
        return SSTRONGHOLDCODE;
    }

    public void setSSTRONGHOLDCODE(String SSTRONGHOLDCODE) {
        this.SSTRONGHOLDCODE = SSTRONGHOLDCODE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public int getSstatus() {
        return sstatus;
    }

    public void setSstatus(int sstatus) {
        this.sstatus = sstatus;
    }

    public String getSstatusname() {
        return sstatusname;
    }

    public void setSstatusname(String sstatusname) {
        this.sstatusname = sstatusname;
    }

    public String getEna() {
        return ena;
    }

    public void setEna(String ena) {
        this.ena = ena;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public CheckAnalyze() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Creater);
        dest.writeString(this.unit);
        dest.writeLong(this.EDATE != null ? this.EDATE.getTime() : -1);
        dest.writeLong(this.SEDATE != null ? this.SEDATE.getTime() : -1);
        dest.writeString(this.SUPCODE);
        dest.writeString(this.SUPNAME);
        dest.writeLong(this.PRODUCTDATE != null ? this.PRODUCTDATE.getTime() : -1);
        dest.writeString(this.SUPPRDBATCH);
        dest.writeLong(this.SUPPRDDATE != null ? this.SUPPRDDATE.getTime() : -1);
        dest.writeString(this.PALLETNO);
        dest.writeString(this.BARCODE);
        dest.writeString(this.CHECKNO);
        dest.writeString(this.AREANO);
        dest.writeInt(this.AREAID);
        dest.writeInt(this.houseid);
        dest.writeInt(this.warehouseid);
        dest.writeString(this.houseno);
        dest.writeString(this.warehouseno);
        dest.writeString(this.swarehouseno);
        dest.writeString(this.MATERIALNO);
        dest.writeInt(this.MATERIALID);
        dest.writeString(this.MATERIALDESC);
        dest.writeString(this.SERIALNO);
        dest.writeString(this.SAREANO);
        dest.writeInt(this.SAREAID);
        dest.writeString(this.SMATERIALDESC);
        dest.writeString(this.SMATERIALNO);
        dest.writeInt(this.SMATERIALNOID);
        dest.writeString(this.SSERIALNO);
        dest.writeString(this.remark);
        dest.writeFloat(this.QTY);
        dest.writeFloat(this.SQTY);
        dest.writeString(this.YQTY);
        dest.writeString(this.KQTY);
        dest.writeString(this.partno);
        dest.writeString(this.BatchNo);
        dest.writeString(this.SBatchNo);
        dest.writeString(this.STRONGHOLDCODE);
        dest.writeString(this.STRONGHOLDNAME);
        dest.writeString(this.SSTRONGHOLDCODE);
        dest.writeInt(this.status);
        dest.writeString(this.statusname);
        dest.writeInt(this.sstatus);
        dest.writeString(this.sstatusname);
        dest.writeString(this.ena);
        dest.writeString(this.dimension);
    }

    protected CheckAnalyze(Parcel in) {
        this.Creater = in.readString();
        this.unit = in.readString();
        long tmpEDATE = in.readLong();
        this.EDATE = tmpEDATE == -1 ? null : new Date(tmpEDATE);
        long tmpSEDATE = in.readLong();
        this.SEDATE = tmpSEDATE == -1 ? null : new Date(tmpSEDATE);
        this.SUPCODE = in.readString();
        this.SUPNAME = in.readString();
        long tmpPRODUCTDATE = in.readLong();
        this.PRODUCTDATE = tmpPRODUCTDATE == -1 ? null : new Date(tmpPRODUCTDATE);
        this.SUPPRDBATCH = in.readString();
        long tmpSUPPRDDATE = in.readLong();
        this.SUPPRDDATE = tmpSUPPRDDATE == -1 ? null : new Date(tmpSUPPRDDATE);
        this.PALLETNO = in.readString();
        this.BARCODE = in.readString();
        this.CHECKNO = in.readString();
        this.AREANO = in.readString();
        this.AREAID = in.readInt();
        this.houseid = in.readInt();
        this.warehouseid = in.readInt();
        this.houseno = in.readString();
        this.warehouseno = in.readString();
        this.swarehouseno = in.readString();
        this.MATERIALNO = in.readString();
        this.MATERIALID = in.readInt();
        this.MATERIALDESC = in.readString();
        this.SERIALNO = in.readString();
        this.SAREANO = in.readString();
        this.SAREAID = in.readInt();
        this.SMATERIALDESC = in.readString();
        this.SMATERIALNO = in.readString();
        this.SMATERIALNOID = in.readInt();
        this.SSERIALNO = in.readString();
        this.remark = in.readString();
        this.QTY = in.readFloat();
        this.SQTY = in.readFloat();
        this.YQTY = in.readString();
        this.KQTY = in.readString();
        this.partno = in.readString();
        this.BatchNo = in.readString();
        this.SBatchNo = in.readString();
        this.STRONGHOLDCODE = in.readString();
        this.STRONGHOLDNAME = in.readString();
        this.SSTRONGHOLDCODE = in.readString();
        this.status = in.readInt();
        this.statusname = in.readString();
        this.sstatus = in.readInt();
        this.sstatusname = in.readString();
        this.ena = in.readString();
        this.dimension = in.readString();
    }

    public static final Creator<CheckAnalyze> CREATOR = new Creator<CheckAnalyze>() {
        @Override
        public CheckAnalyze createFromParcel(Parcel source) {
            return new CheckAnalyze(source);
        }

        @Override
        public CheckAnalyze[] newArray(int size) {
            return new CheckAnalyze[size];
        }
    };
}
