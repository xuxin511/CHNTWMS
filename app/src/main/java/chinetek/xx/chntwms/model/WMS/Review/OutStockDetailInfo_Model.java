package chinetek.xx.chntwms.model.WMS.Review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import chinetek.xx.chntwms.model.Base_Model;
import chinetek.xx.chntwms.model.Material.SerialNo_Model;
import chinetek.xx.chntwms.model.WMS.Stock.StockInfo_Model;

/**
 * Created by GHOST on 2017/6/20.
 */

public class OutStockDetailInfo_Model extends Base_Model implements Parcelable{
    private int OutStockID;
    private String MaterialNo;
    private String MaterialDesc;
    private String RowNo;
    private String RowNoDel;
    private String Plant;
    private String PlantName;
    private String ToStorageLoc;
    private String Unit;
    private String Unitname;
    private Float OutStockQty;
    private Float OldOutStockQty;
    private Float RemainQty;
    private String Costcenter;
    private String Wbselem;
    private String FromStorageLoc;
    private int ReviewStatus;
    private String CloseOweUser;
    private String CloseOweRemark;
    private int IsOweClose;
    private String OweRemark;
    private String OweRemarkUser;
    private Boolean OKSelect;
    private String VoucherNo;
    private Float ScanQty;
    private String FromBatchNo;
    private ArrayList<SerialNo_Model> lstSerialNo;
    private ArrayList<StockInfo_Model> lstStock=new ArrayList<StockInfo_Model>();
    private String BatchNo;
    private int IsSerial;
    private String PartNo;
    private String ToBatchno;
    private String ToErpAreaNo;
    private String ToErpWareHouse;
    private String SourceVoucherNo;
    private String SourceRowNo;
    private int OutstockDetailID;
    private String FromErpAreaNo;
    private String FromErpWarehouse;
    private String ToBatchNo;
    private String ToErpWarehouse;
    private String IsSpcBatch;
    private  Boolean IsReviewFinish=false; //行是否复核完毕
    private int OustockStatus=0;//：0：StockInfo_Model不存在未组托条码  1：StockInfo_Model存在未组托条码
    public String ErpReviceNo;
    public String SubIarrSID;
    public String WareHouseNo;
    public Float CanReviewQty;

    public String PostWareHouseNo;   // 过账仓库


    public String CustomerCode;

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String CustomerName;

    public String getPostWareHouseNo() {
        return PostWareHouseNo;
    }

    public void setPostWareHouseNo(String postWareHouseNo) {
        PostWareHouseNo = postWareHouseNo;
    }

    public Float getCanReviewQty() {
        return CanReviewQty;
    }

    public void setCanReviewQty(Float canReviewQty) {
        CanReviewQty = canReviewQty;
    }

    public String getWareHouseNo() {
        return WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    private int WareHouseID;

    public String getErpReviceNo() {
        return ErpReviceNo;
    }

    public void setErpReviceNo(String erpReviceNo) {
        ErpReviceNo = erpReviceNo;
    }

    public int getOutStockID() {
        return OutStockID;
    }

    public int getReviewStatus() {
        return ReviewStatus;
    }

    public int getIsOweClose() {
        return IsOweClose;
    }

    public int getWareHouseID() {
        return WareHouseID;
    }

    public void setWareHouseID(int warehouseid) {
        this.WareHouseID = warehouseid;
    }

    public void setOutStockID(int outStockID) {
        OutStockID = outStockID;
    }

    public void setReviewStatus(int reviewStatus) {
        ReviewStatus = reviewStatus;
    }

    public void setIsOweClose(int isOweClose) {
        IsOweClose = isOweClose;
    }

    public String getSubIarrSID() {
        return SubIarrSID;
    }

    public void setSubIarrSID(String subIarrSID) {
        SubIarrSID = subIarrSID;
    }

    public String getFromErpAreaNo() {
        return FromErpAreaNo;
    }

    public void setFromErpAreaNo(String fromErpAreaNo) {
        FromErpAreaNo = fromErpAreaNo;
    }

    public String getFromErpWarehouse() {
        return FromErpWarehouse;
    }

    public void setFromErpWarehouse(String fromErpWarehouse) {
        FromErpWarehouse = fromErpWarehouse;
    }

    public String getIsSpcBatch() {
        return IsSpcBatch;
    }

    public void setIsSpcBatch(String isSpcBatch) {
        IsSpcBatch = isSpcBatch;
    }

    public Boolean getReviewFinish() {
        return IsReviewFinish;
    }

    public void setReviewFinish(Boolean reviewFinish) {
        IsReviewFinish = reviewFinish;
    }

    public String getToBatchNo() {
        return ToBatchNo;
    }

    public void setToBatchNo(String toBatchNo) {
        ToBatchNo = toBatchNo;
    }

    public String getToErpWarehouse() {
        return ToErpWarehouse;
    }

    public void setToErpWarehouse(String toErpWarehouse) {
        ToErpWarehouse = toErpWarehouse;
    }

    public String getFromBatchNo() {
        return FromBatchNo;
    }

    public void setFromBatchNo(String fromBatchNo) {
        FromBatchNo = fromBatchNo;
    }

    public int getOutstockDetailID() {
        return OutstockDetailID;
    }

    public void setOutstockDetailID(int outstockDetailID) {
        OutstockDetailID = outstockDetailID;
    }

    public String getSourceVoucherNo() {
        return SourceVoucherNo;
    }

    public void setSourceVoucherNo(String sourceVoucherNo) {
        SourceVoucherNo = sourceVoucherNo;
    }

    public String getSourceRowNo() {
        return SourceRowNo;
    }

    public void setSourceRowNo(String sourceRowNo) {
        SourceRowNo = sourceRowNo;
    }

    public String getToBatchno() {
        return ToBatchno;
    }

    public void setToBatchno(String toBatchno) {
        ToBatchno = toBatchno;
    }

    public String getToErpAreaNo() {
        return ToErpAreaNo;
    }

    public void setToErpAreaNo(String toErpAreaNo) {
        ToErpAreaNo = toErpAreaNo;
    }

    public String getToErpWareHouse() {
        return ToErpWareHouse;
    }

    public void setToErpWareHouse(String toErpWareHouse) {
        ToErpWareHouse = toErpWareHouse;
    }


    public String getRowNoDel() {
        return RowNoDel;
    }

    public void setRowNoDel(String rowNoDel) {
        RowNoDel = rowNoDel;
    }

    public int getOustockStatus() {
        return OustockStatus;
    }

    public void setOustockStatus(int oustockStatus) {
        OustockStatus = oustockStatus;
    }

    public Float getScanQty() {
        return ScanQty;
    }

    public void setScanQty(Float scanQty) {
        ScanQty = scanQty;
    }


    public List<StockInfo_Model> getLstStock() {
        return lstStock;
    }

    public void setLstStock(ArrayList<StockInfo_Model> lstStockInfo) {
        this.lstStock = lstStockInfo;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

//    public Float getOutStockID() {
//        return OutStockID;
//    }
//
//    public void setOutStockID(Float outStockID) {
//        OutStockID = outStockID;
//    }


    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String plantName) {
        PlantName = plantName;
    }

    public String getToStorageLoc() {
        return ToStorageLoc;
    }

    public void setToStorageLoc(String toStorageLoc) {
        ToStorageLoc = toStorageLoc;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitname() {
        return Unitname;
    }

    public void setUnitname(String unitname) {
        Unitname = unitname;
    }

    public Float getOutStockQty() {
        return OutStockQty;
    }

    public void setOutStockQty(Float outStockQty) {
        OutStockQty = outStockQty;
    }

    public Float getOldOutStockQty() {
        return OldOutStockQty;
    }

    public void setOldOutStockQty(Float oldOutStockQty) {
        OldOutStockQty = oldOutStockQty;
    }

    public Float getRemainQty() {
        return RemainQty;
    }

    public void setRemainQty(Float remainQty) {
        RemainQty = remainQty;
    }

    public String getCostcenter() {
        return Costcenter;
    }

    public void setCostcenter(String costcenter) {
        Costcenter = costcenter;
    }

    public String getWbselem() {
        return Wbselem;
    }

    public void setWbselem(String wbselem) {
        Wbselem = wbselem;
    }

    public String getFromStorageLoc() {
        return FromStorageLoc;
    }

    public void setFromStorageLoc(String fromStorageLoc) {
        FromStorageLoc = fromStorageLoc;
    }

//    public Float getReviewStatus() {
//        return ReviewStatus;
//    }
//
//    public void setReviewStatus(Float reviewStatus) {
//        ReviewStatus = reviewStatus;
//    }

    public String getCloseOweUser() {
        return CloseOweUser;
    }

    public void setCloseOweUser(String closeOweUser) {
        CloseOweUser = closeOweUser;
    }

    public String getCloseOweRemark() {
        return CloseOweRemark;
    }

    public void setCloseOweRemark(String closeOweRemark) {
        CloseOweRemark = closeOweRemark;
    }

//    public Float getIsOweClose() {
//        return IsOweClose;
//    }
//
//    public void setIsOweClose(Float isOweClose) {
//        IsOweClose = isOweClose;
//    }

    public String getOweRemark() {
        return OweRemark;
    }

    public void setOweRemark(String oweRemark) {
        OweRemark = oweRemark;
    }

    public String getOweRemarkUser() {
        return OweRemarkUser;
    }

    public void setOweRemarkUser(String oweRemarkUser) {
        OweRemarkUser = oweRemarkUser;
    }

    public Boolean getOKSelect() {
        return OKSelect;
    }

    public void setOKSelect(Boolean OKSelect) {
        this.OKSelect = OKSelect;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public List<SerialNo_Model> getLstSerialNo() {
        return lstSerialNo;
    }

    public void setLstSerialNo(ArrayList<SerialNo_Model> lstSerialNo) {
        this.lstSerialNo = lstSerialNo;
    }

    public int getIsSerial() {
        return IsSerial;
    }

    public void setIsSerial(int isSerial) {
        IsSerial = isSerial;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public OutStockDetailInfo_Model() {
    }

    public OutStockDetailInfo_Model(String MaterialNo,String StrongHoldCode){
        this.MaterialNo=MaterialNo;
        this.StrongHoldCode=StrongHoldCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutStockDetailInfo_Model that = (OutStockDetailInfo_Model) o;

        return MaterialNo.equals(that.MaterialNo) && StrongHoldCode.equals(that.StrongHoldCode);
        // return MaterialNo.equals(that.MaterialNo) && RowNo.equals(that.RowNo);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.OutStockID);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeString(this.RowNo);
        dest.writeString(this.RowNoDel);
        dest.writeString(this.Plant);
        dest.writeString(this.PlantName);
        dest.writeString(this.ToStorageLoc);
        dest.writeString(this.Unit);
        dest.writeString(this.Unitname);
        dest.writeValue(this.OutStockQty);
        dest.writeValue(this.OldOutStockQty);
        dest.writeValue(this.RemainQty);
        dest.writeString(this.Costcenter);
        dest.writeString(this.Wbselem);
        dest.writeString(this.FromStorageLoc);
        dest.writeInt(this.ReviewStatus);
        dest.writeString(this.CloseOweUser);
        dest.writeString(this.CloseOweRemark);
        dest.writeInt(this.IsOweClose);
        dest.writeString(this.OweRemark);
        dest.writeString(this.OweRemarkUser);
        dest.writeValue(this.OKSelect);
        dest.writeString(this.VoucherNo);
        dest.writeValue(this.ScanQty);
        dest.writeString(this.FromBatchNo);
        dest.writeTypedList(this.lstSerialNo);
        dest.writeTypedList(this.lstStock);
        dest.writeString(this.BatchNo);
        dest.writeInt(this.IsSerial);
        dest.writeString(this.PartNo);
        dest.writeString(this.ToBatchno);
        dest.writeString(this.ToErpAreaNo);
        dest.writeString(this.ToErpWareHouse);
        dest.writeString(this.SourceVoucherNo);
        dest.writeString(this.SourceRowNo);
        dest.writeInt(this.OutstockDetailID);
        dest.writeString(this.FromErpAreaNo);
        dest.writeString(this.FromErpWarehouse);
        dest.writeString(this.ToBatchNo);
        dest.writeString(this.ToErpWarehouse);
        dest.writeString(this.IsSpcBatch);
        dest.writeValue(this.IsReviewFinish);
        dest.writeInt(this.OustockStatus);
        dest.writeString(this.ErpReviceNo);
        dest.writeString(this.SubIarrSID);
        dest.writeString(this.WareHouseNo);
        dest.writeValue(this.CanReviewQty);
        dest.writeString(this.PostWareHouseNo);
        dest.writeString(this.CustomerCode);
        dest.writeString(this.CustomerName);
        dest.writeInt(this.WareHouseID);
    }

    protected OutStockDetailInfo_Model(Parcel in) {
        super(in);
        this.OutStockID = in.readInt();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.RowNo = in.readString();
        this.RowNoDel = in.readString();
        this.Plant = in.readString();
        this.PlantName = in.readString();
        this.ToStorageLoc = in.readString();
        this.Unit = in.readString();
        this.Unitname = in.readString();
        this.OutStockQty = (Float) in.readValue(Float.class.getClassLoader());
        this.OldOutStockQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Costcenter = in.readString();
        this.Wbselem = in.readString();
        this.FromStorageLoc = in.readString();
        this.ReviewStatus = in.readInt();
        this.CloseOweUser = in.readString();
        this.CloseOweRemark = in.readString();
        this.IsOweClose = in.readInt();
        this.OweRemark = in.readString();
        this.OweRemarkUser = in.readString();
        this.OKSelect = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.VoucherNo = in.readString();
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.FromBatchNo = in.readString();
        this.lstSerialNo = in.createTypedArrayList(SerialNo_Model.CREATOR);
        this.lstStock = in.createTypedArrayList(StockInfo_Model.CREATOR);
        this.BatchNo = in.readString();
        this.IsSerial = in.readInt();
        this.PartNo = in.readString();
        this.ToBatchno = in.readString();
        this.ToErpAreaNo = in.readString();
        this.ToErpWareHouse = in.readString();
        this.SourceVoucherNo = in.readString();
        this.SourceRowNo = in.readString();
        this.OutstockDetailID = in.readInt();
        this.FromErpAreaNo = in.readString();
        this.FromErpWarehouse = in.readString();
        this.ToBatchNo = in.readString();
        this.ToErpWarehouse = in.readString();
        this.IsSpcBatch = in.readString();
        this.IsReviewFinish = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.OustockStatus = in.readInt();
        this.ErpReviceNo = in.readString();
        this.SubIarrSID = in.readString();
        this.WareHouseNo = in.readString();
        this.CanReviewQty = (Float) in.readValue(Float.class.getClassLoader());
        this.PostWareHouseNo = in.readString();
        this.CustomerCode = in.readString();
        this.CustomerName = in.readString();
        this.WareHouseID = in.readInt();
    }

    public static final Creator<OutStockDetailInfo_Model> CREATOR = new Creator<OutStockDetailInfo_Model>() {
        @Override
        public OutStockDetailInfo_Model createFromParcel(Parcel source) {
            return new OutStockDetailInfo_Model(source);
        }

        @Override
        public OutStockDetailInfo_Model[] newArray(int size) {
            return new OutStockDetailInfo_Model[size];
        }
    };
}
