/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.renovace.demo.bean;

import com.athou.frame.bean.FinalBean;

public class AreaItemBean extends FinalBean implements Comparable<AreaItemBean> {
	private static final long serialVersionUID = 6664904570619893557L;

	private String area_id = null;
	private String area_fid = null;
	private String area_name = null;
	private String area_index = null;
	private String area_pin = null;
	private String area_en = null;
	private String area_level = null;
	private String area_show = null;
	private String area_timezone = null;
	private long select_time = 0;
	private String area_currency; // 币种
	private float tp_run = 0; // 徒步导游
	private float tp_car = 0;// 带车
	private float pp_run = 0;// 徒步接机
	private float pp_car = 0;// 带车接机
	private String area_note; // 附加费

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getArea_fid() {
		return area_fid;
	}

	public void setArea_fid(String area_fid) {
		this.area_fid = area_fid;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getArea_index() {
		return area_index;
	}

	public void setArea_index(String area_index) {
		this.area_index = area_index;
	}

	public String getArea_pin() {
		return area_pin;
	}

	public void setArea_pin(String area_pin) {
		this.area_pin = area_pin;
	}

	public String getArea_en() {
		return area_en;
	}

	public void setArea_en(String area_en) {
		this.area_en = area_en;
	}

	public String getArea_level() {
		return area_level;
	}

	public void setArea_level(String area_level) {
		this.area_level = area_level;
	}

	public String getArea_show() {
		return area_show;
	}

	public void setArea_show(String area_show) {
		this.area_show = area_show;
	}

	public String getArea_timezone() {
		return area_timezone;
	}

	public void setArea_timezone(String area_timezone) {
		this.area_timezone = area_timezone;
	}

	public long getSelect_time() {
		return select_time;
	}

	public void setSelect_time(long select_time) {
		this.select_time = select_time;
	}

	public String getArea_currency() {
		return area_currency;
	}

	public void setArea_currency(String area_currency) {
		this.area_currency = area_currency;
	}

	public float getTp_run() {
		return tp_run;
	}

	public void setTp_run(float tp_run) {
		this.tp_run = tp_run;
	}

	public float getTp_car() {
		return tp_car;
	}

	public void setTp_car(float tp_car) {
		this.tp_car = tp_car;
	}

	public float getPp_run() {
		return pp_run;
	}

	public void setPp_run(float pp_run) {
		this.pp_run = pp_run;
	}

	public float getPp_car() {
		return pp_car;
	}

	public void setPp_car(float pp_car) {
		this.pp_car = pp_car;
	}

	@Override
	public int compareTo(AreaItemBean another) {
		int m = getArea_fid().compareTo(another.getArea_fid());
		if (m == 0) {
			int n = (int) (getSelect_time() - another.getSelect_time());
			if (n == 0) {
				return getArea_pin().compareTo(another.getArea_pin());
			} else if (n > 0) {
				return -1;
			} else {
				return 1;
			}
		}
		return m;
	}

	public String getArea_note() {
		return area_note;
	}

	public void setArea_note(String area_note) {
		this.area_note = area_note;
	}
}
