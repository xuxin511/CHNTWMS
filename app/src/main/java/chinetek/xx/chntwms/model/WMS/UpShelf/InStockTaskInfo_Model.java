package chinetek.xx.chntwms.model.WMS.UpShelf;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

import chinetek.xx.chntwms.model.Base_Model;

/**
 * Created by GHOST on 2017/1/13.
 */

public class InStockTaskInfo_Model extends Base_Model implements Parcelable {

    public InStockTaskInfo_Model(){

    }

    public InStockTaskInfo_Model(String ErpVoucherNo){
        this.ErpVoucherNo=ErpVoucherNo;
    }

    private int TaskType;
    private String TaskNo;
    private String SupcusName;
    private int TaskStatus;
    private String AuditUserNo;
    private Date AuditDateTime;
  //  private Date TaskIssued;
    private String ReceiveUserNo;
    private String Remark;
    private String Reason;
    private String SupcusNo;
    private Float IsShelvePost;
    private Float IsQuality;
    private Float IsReceivePost;
    private String Plant;
    private String PlanName;
    private String MoveType;
    private Float IsOutStockPost;
    private Float IsUnderShelvePost;
    private int ReviewStatus;
    private String MoveReasonCode;
    private String MoveReasonDesc;
    private Float PrintQty;
    private Date PrintTime;
    private Date CloseDateTime;
    private String CloseUserNo;
    private String CloseReason;
    private int IsOwe;
    private int IsUrgent;
    private Date OutStockDate;
    private String TaskIsSuedUser;
    private String MaterialNo ;
    private int InStockID ;
    private String StrTaskType;
    private String StrTaskIsSuedUser;
    private int WareHouseID;

    public int getWareHouseID() {
        return WareHouseID;
    }

    public void setWareHouseID(int wareHouseID) {
        WareHouseID = wareHouseID;
    }

    public Date getAuditDateTime() {
        return AuditDateTime;
    }

    public void setAuditDateTime(Date auditDateTime) {
        AuditDateTime = auditDateTime;
    }

    public String getAuditUserNo() {
        return AuditUserNo;
    }

    public void setAuditUserNo(String auditUserNo) {
        AuditUserNo = auditUserNo;
    }

    public Date getCloseDateTime() {
        return CloseDateTime;
    }

    public void setCloseDateTime(Date closeDateTime) {
        CloseDateTime = closeDateTime;
    }

    public String getCloseReason() {
        return CloseReason;
    }

    public void setCloseReason(String closeReason) {
        CloseReason = closeReason;
    }

    public String getCloseUserNo() {
        return CloseUserNo;
    }

    public void setCloseUserNo(String closeUserNo) {
        CloseUserNo = closeUserNo;
    }


    public int getInStockID() {
        return InStockID;
    }

    public void setInStockID(int inStockID) {
        InStockID = inStockID;
    }

    public Float getIsOutStockPost() {
        return IsOutStockPost;
    }

    public void setIsOutStockPost(Float isOutStockPost) {
        IsOutStockPost = isOutStockPost;
    }



    public Float getIsQuality() {
        return IsQuality;
    }

    public void setIsQuality(Float isQuality) {
        IsQuality = isQuality;
    }

    public Float getIsReceivePost() {
        return IsReceivePost;
    }

    public void setIsReceivePost(Float isReceivePost) {
        IsReceivePost = isReceivePost;
    }

    public Float getIsShelvePost() {
        return IsShelvePost;
    }

    public void setIsShelvePost(Float isShelvePost) {
        IsShelvePost = isShelvePost;
    }

    public Float getIsUnderShelvePost() {
        return IsUnderShelvePost;
    }

    public void setIsUnderShelvePost(Float isUnderShelvePost) {
        IsUnderShelvePost = isUnderShelvePost;
    }



    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getMoveReasonCode() {
        return MoveReasonCode;
    }

    public void setMoveReasonCode(String moveReasonCode) {
        MoveReasonCode = moveReasonCode;
    }

    public String getMoveReasonDesc() {
        return MoveReasonDesc;
    }

    public void setMoveReasonDesc(String moveReasonDesc) {
        MoveReasonDesc = moveReasonDesc;
    }

    public String getMoveType() {
        return MoveType;
    }

    public void setMoveType(String moveType) {
        MoveType = moveType;
    }

    public Date getOutStockDate() {
        return OutStockDate;
    }

    public void setOutStockDate(Date outStockDate) {
        OutStockDate = outStockDate;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }


    public Float getPrintQty() {
        return PrintQty;
    }

    public void setPrintQty(Float printQty) {
        PrintQty = printQty;
    }

    public Date getPrintTime() {
        return PrintTime;
    }

    public void setPrintTime(Date printTime) {
        PrintTime = printTime;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getReceiveUserNo() {
        return ReceiveUserNo;
    }

    public void setReceiveUserNo(String receiveUserNo) {
        ReceiveUserNo = receiveUserNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }



    public String getStrTaskIsSuedUser() {
        return StrTaskIsSuedUser;
    }

    public void setStrTaskIsSuedUser(String strTaskIsSuedUser) {
        StrTaskIsSuedUser = strTaskIsSuedUser;
    }

    public String getStrTaskType() {
        return StrTaskType;
    }

    public void setStrTaskType(String strTaskType) {
        StrTaskType = strTaskType;
    }

    public String getSupcusName() {
        return SupcusName;
    }

    public void setSupcusName(String supcusName) {
        SupcusName = supcusName;
    }

    public String getSupcusNo() {
        return SupcusNo;
    }

    public void setSupcusNo(String supcusNo) {
        SupcusNo = supcusNo;
    }


    public String getTaskIsSuedUser() {
        return TaskIsSuedUser;
    }

    public void setTaskIsSuedUser(String taskIsSuedUser) {
        TaskIsSuedUser = taskIsSuedUser;
    }

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }


    public int getTaskType() {
        return TaskType;
    }

    public void setTaskType(int taskType) {
        TaskType = taskType;
    }

    public int getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        TaskStatus = taskStatus;
    }

    public int getReviewStatus() {
        return ReviewStatus;
    }

    public void setReviewStatus(int reviewStatus) {
        ReviewStatus = reviewStatus;
    }

    public int getIsOwe() {
        return IsOwe;
    }

    public void setIsOwe(int isOwe) {
        IsOwe = isOwe;
    }

    public int getIsUrgent() {
        return IsUrgent;
    }

    public void setIsUrgent(int isUrgent) {
        IsUrgent = isUrgent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        InStockTaskInfo_Model that = (InStockTaskInfo_Model) obj;

        return ErpVoucherNo.equals(that.getErpVoucherNo());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.TaskType);
        dest.writeString(this.TaskNo);
        dest.writeString(this.SupcusName);
        dest.writeInt(this.TaskStatus);
        dest.writeString(this.AuditUserNo);
        dest.writeLong(this.AuditDateTime != null ? this.AuditDateTime.getTime() : -1);
        dest.writeString(this.ReceiveUserNo);
        dest.writeString(this.Remark);
        dest.writeString(this.Reason);
        dest.writeString(this.SupcusNo);
        dest.writeValue(this.IsShelvePost);
        dest.writeValue(this.IsQuality);
        dest.writeValue(this.IsReceivePost);
        dest.writeString(this.Plant);
        dest.writeString(this.PlanName);
        dest.writeString(this.MoveType);
        dest.writeValue(this.IsOutStockPost);
        dest.writeValue(this.IsUnderShelvePost);
        dest.writeInt(this.ReviewStatus);
        dest.writeString(this.MoveReasonCode);
        dest.writeString(this.MoveReasonDesc);
        dest.writeValue(this.PrintQty);
        dest.writeLong(this.PrintTime != null ? this.PrintTime.getTime() : -1);
        dest.writeLong(this.CloseDateTime != null ? this.CloseDateTime.getTime() : -1);
        dest.writeString(this.CloseUserNo);
        dest.writeString(this.CloseReason);
        dest.writeInt(this.IsOwe);
        dest.writeInt(this.IsUrgent);
        dest.writeLong(this.OutStockDate != null ? this.OutStockDate.getTime() : -1);
        dest.writeString(this.TaskIsSuedUser);
        dest.writeString(this.MaterialNo);
        dest.writeInt(this.InStockID);
        dest.writeString(this.StrTaskType);
        dest.writeString(this.StrTaskIsSuedUser);
        dest.writeInt(this.WareHouseID);
    }

    protected InStockTaskInfo_Model(Parcel in) {
        super(in);
        this.TaskType = in.readInt();
        this.TaskNo = in.readString();
        this.SupcusName = in.readString();
        this.TaskStatus = in.readInt();
        this.AuditUserNo = in.readString();
        long tmpAuditDateTime = in.readLong();
        this.AuditDateTime = tmpAuditDateTime == -1 ? null : new Date(tmpAuditDateTime);
        this.ReceiveUserNo = in.readString();
        this.Remark = in.readString();
        this.Reason = in.readString();
        this.SupcusNo = in.readString();
        this.IsShelvePost = (Float) in.readValue(Float.class.getClassLoader());
        this.IsQuality = (Float) in.readValue(Float.class.getClassLoader());
        this.IsReceivePost = (Float) in.readValue(Float.class.getClassLoader());
        this.Plant = in.readString();
        this.PlanName = in.readString();
        this.MoveType = in.readString();
        this.IsOutStockPost = (Float) in.readValue(Float.class.getClassLoader());
        this.IsUnderShelvePost = (Float) in.readValue(Float.class.getClassLoader());
        this.ReviewStatus = in.readInt();
        this.MoveReasonCode = in.readString();
        this.MoveReasonDesc = in.readString();
        this.PrintQty = (Float) in.readValue(Float.class.getClassLoader());
        long tmpPrintTime = in.readLong();
        this.PrintTime = tmpPrintTime == -1 ? null : new Date(tmpPrintTime);
        long tmpCloseDateTime = in.readLong();
        this.CloseDateTime = tmpCloseDateTime == -1 ? null : new Date(tmpCloseDateTime);
        this.CloseUserNo = in.readString();
        this.CloseReason = in.readString();
        this.IsOwe = in.readInt();
        this.IsUrgent = in.readInt();
        long tmpOutStockDate = in.readLong();
        this.OutStockDate = tmpOutStockDate == -1 ? null : new Date(tmpOutStockDate);
        this.TaskIsSuedUser = in.readString();
        this.MaterialNo = in.readString();
        this.InStockID = in.readInt();
        this.StrTaskType = in.readString();
        this.StrTaskIsSuedUser = in.readString();
        this.WareHouseID = in.readInt();
    }

    public static final Creator<InStockTaskInfo_Model> CREATOR = new Creator<InStockTaskInfo_Model>() {
        @Override
        public InStockTaskInfo_Model createFromParcel(Parcel source) {
            return new InStockTaskInfo_Model(source);
        }

        @Override
        public InStockTaskInfo_Model[] newArray(int size) {
            return new InStockTaskInfo_Model[size];
        }
    };
}
