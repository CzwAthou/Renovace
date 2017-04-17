package com.athou.renovace.demo.bean;

import com.athou.renovace.bean.RenovaceBean;

/**
 * Created by athou on 2017/4/7.
 */

public class YuminResultBean extends RenovaceBean {
    /**
     * status : true
     * available : false
     */

    private boolean status;
    private boolean available;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "YuminResultBean{" +
                "status=" + status +
                ", available=" + available +
                '}';
    }
}
