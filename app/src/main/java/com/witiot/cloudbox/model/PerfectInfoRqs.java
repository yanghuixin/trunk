package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class PerfectInfoRqs extends BaseRqs{

    private PerfectInfoRqs.DatBean dat;

    public PerfectInfoRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(PerfectInfoRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {
        String  customerId;
        String  customerName;
        String  birth;
        String  gendar; //性别: 1男, 2女
        String  nation;//民族
        String  profession;//职业
        String  marriage;//婚姻
        String  insurance;//保险类型
        String  department;
        String  pastIllness;//既往史
        String  presentIllness;//现病史
        String  complaint;//主诉
        String  diagnosis;//诊断
        String  surgery;//手术
        String  surgeryTime;//手术时间
        String  hospitalDate;//入院日期
        String  hospitalDays;//住院天数

        public String getCustomerId() {
            return customerId;
        }
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getBirth() {
            return birth;
        }
        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getGendar() {
            return gendar;
        }
        public void setGendar(String gendar) {
            this.gendar = gendar;
        }

        public String getNation() {
            return nation;
        }
        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getProfession() {
            return profession;
        }
        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getMarriage() {
            return marriage;
        }
        public void setMarriage(String marriage) {
            this.marriage = marriage;
        }

        public String getInsurance() {
            return insurance;
        }
        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getDepartment() {
            return department;
        }
        public void setDepartment(String department) {
            this.department = department;
        }

        public String getSurgery() {
            return surgery;
        }
        public void setSurgery(String surgery) {
            this.surgery = surgery;
        }

        public String getSurgeryTime() {
            return surgeryTime;
        }
        public void setSurgeryTime(String surgeryTime) {
            this.surgeryTime = surgeryTime;
        }

        public String getHospitalDate() {
            return hospitalDate;
        }
        public void setHospitalDate(String hospitalDate) {
            this.hospitalDate = hospitalDate;
        }

        public String getHospitalDays() {
            return hospitalDays;
        }
        public void setHospitalDays(String hospitalDays) {
            this.hospitalDays = hospitalDays;
        }

        public String getPastIllness() {
            return pastIllness;
        }
        public void setPastIllness(String pastIllness) {
            this.pastIllness = pastIllness;
        }

        public String getPresentIllness() {
            return presentIllness;
        }
        public void setPresentIllness(String presentIllness) {
            this.presentIllness = presentIllness;
        }

        public String getComplaint() {
            return complaint;
        }
        public void setComplaint(String complaint) {
            this.complaint = complaint;
        }

        public String getDiagnosis() {
            return diagnosis;
        }
        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }


    }

}
