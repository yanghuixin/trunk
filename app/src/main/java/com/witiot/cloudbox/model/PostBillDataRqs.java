package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class PostBillDataRqs extends BaseRqs{

    private PostBillDataRqs.DatBean dat;

    public PostBillDataRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(PostBillDataRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        public String customerId;  public String  deviceId;  public String  billNo;  public String  uploadPath ;  public String  patientName;  public String  inspectionFee;  public String  hospitalFee;  public String  materialFee;  public String  drugFee;  public String  fee1;  public String  fee2;  public String  fee3;  public String  fee4;  public String  fee5;  public String  totalFee ;  public String status;  public String msg;
        public String treatmentFee;

        public String getTreatmentFee() {
            return treatmentFee;
        }

        public void setTreatmentFee(String treatmentFee) {
            this.treatmentFee = treatmentFee;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public String getUploadPath() {
            return uploadPath;
        }

        public void setUploadPath(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getInspectionFee() {
            return inspectionFee;
        }

        public void setInspectionFee(String inspectionFee) {
            this.inspectionFee = inspectionFee;
        }

        public String getHospitalFee() {
            return hospitalFee;
        }

        public void setHospitalFee(String hospitalFee) {
            this.hospitalFee = hospitalFee;
        }

        public String getMaterialFee() {
            return materialFee;
        }

        public void setMaterialFee(String materialFee) {
            this.materialFee = materialFee;
        }

        public String getDrugFee() {
            return drugFee;
        }

        public void setDrugFee(String drugFee) {
            this.drugFee = drugFee;
        }

        public String getFee1() {
            return fee1;
        }

        public void setFee1(String fee1) {
            this.fee1 = fee1;
        }

        public String getFee2() {
            return fee2;
        }

        public void setFee2(String fee2) {
            this.fee2 = fee2;
        }

        public String getFee3() {
            return fee3;
        }

        public void setFee3(String fee3) {
            this.fee3 = fee3;
        }

        public String getFee4() {
            return fee4;
        }

        public void setFee4(String fee4) {
            this.fee4 = fee4;
        }

        public String getFee5() {
            return fee5;
        }

        public void setFee5(String fee5) {
            this.fee5 = fee5;
        }

        public String getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(String totalFee) {
            this.totalFee = totalFee;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
//    }

}
