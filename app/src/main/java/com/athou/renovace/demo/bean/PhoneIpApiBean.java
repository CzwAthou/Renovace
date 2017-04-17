package com.athou.renovace.demo.bean;

import com.athou.renovace.bean.RenovaceBean;

/**
 * Created by athou on 2017/4/17.
 */

public class PhoneIpApiBean<T> extends RenovaceBean<T>{
//    int resultcode;
    String reason;
    int error_code;

    @Override
    public int getCode() {
        return error_code;
    }

    @Override
    public void setCode(int code) {
        error_code = code;
    }

    @Override
    public String getError() {
        return reason;
    }

    @Override
    public void setError(String error) {
        reason = error;
    }

    public class PhoneIpBean{

        /**
         * province : 云南
         * city : 昆明
         * areacode : 0871
         * zip : 650000
         * company : 移动
         * card :
         */

        private String province;
        private String city;
        private String areacode;
        private String zip;
        private String company;
        private String card;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAreacode() {
            return areacode;
        }

        public void setAreacode(String areacode) {
            this.areacode = areacode;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        @Override
        public String toString() {
            return "PhoneIpBean{" +
                    "province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", areacode='" + areacode + '\'' +
                    ", zip='" + zip + '\'' +
                    ", company='" + company + '\'' +
                    ", card='" + card + '\'' +
                    '}';
        }
    }
}
