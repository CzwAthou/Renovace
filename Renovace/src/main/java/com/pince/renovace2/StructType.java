package com.pince.renovace2;

/**
 * the date struct type, has 3 kinds
 *
 * @author athou
 * @date 2017/12/13
 */
public enum StructType {
    /**
     * struct is:{"code":0,"error":"","result":{"name":"renovace"}}
     */
    Result,
    /**
     * custom struct is: {"code":0,"name":"renovace"}
     */
    Bean,
    /**
     * full custom struct is:{"xxx":"xxx","xxx":"xxx"}
     */
    Direct
}