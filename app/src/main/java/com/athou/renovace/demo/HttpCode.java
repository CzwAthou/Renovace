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

package com.athou.renovace.demo;

/**
 * 2015-3-6 网路请求异常 常量
 * 
 * @author 菜菜
 * 
 */
public class HttpCode {

	public static final int ERR_CODE_NONE = 0;
	public static final int ERR_CODE_UNKOWN = 1;
	public static final int ERR_CODE_FILENOTFOUNDEXCEPTION = 2;
	public static final int ERR_CODE_MALFORMEDURLEXCEPTION = 3;
	public static final int ERR_CODE_HTTPHOSTCONNECTEXCEPTION = 4;
	public static final int ERR_CODE_UNKNOWNHOSTEXCEPTION = 5;
	public static final int ERR_CODE_CONNECTTIMEOUTEXCEPTION = 6;
	public static final int ERR_CODE_CONNECTEXCEPTION = 7;
	public static final int ERR_CODE_SOCKETEXCEPTION = 8;
	public static final int ERR_CODE_SOCKETTIMEOUTEXCEPTION = 9;
	public static final int ERR_CODE_CLIENTPROTOCOLEXCEPTION = 10;
	public static final int ERR_CODE_HTTPRESPONSEEXCEPTION = 11;
	public static final int ERR_CODE_IOEXCEPTION = 12;

	public static final String ERR_MESSAGE_NONE = "";
	public static final String ERR_MESSAGE_UNKOWN = "";
	public static final String ERR_MESSAGE_FILENOTFOUNDEXCEPTION = "读取文件失败";
	public static final String ERR_MESSAGE_MALFORMEDURLEXCEPTION = "内部地址错误";
	public static final String ERR_MESSAGE_HTTPHOSTCONNECTEXCEPTION = "网络好像有点问题哦！请检查网络或稍后重试";
	public static final String ERR_MESSAGE_UNKNOWNHOSTEXCEPTION = "网络好像有点问题哦！请检查网络或稍后重试";
	public static final String ERR_MESSAGE_CONNECTTIMEOUTEXCEPTION = "网络连接超时，请检查网络或稍后重试";
	public static final String ERR_MESSAGE_CONNECTEXCEPTION = "无法连接到网络，请检查网络或稍后重试";
	public static final String ERR_MESSAGE_SOCKETEXCEPTION = "网络连接出错，请检查网络或稍后重试";
	public static final String ERR_MESSAGE_SOCKETTIMEOUTEXCEPTION = "连接超时，请稍后重试";
	public static final String ERR_MESSAGE_CLIENTPROTOCOLEXCEPTION = "Http请求参数错误";
	public static final String ERR_MESSAGE_HTTPRESPONSEEXCEPTION = "服务器无响应";
	public static final String ERR_MESSAGE_IOEXCEPTION = "无法连接到网络";

}
