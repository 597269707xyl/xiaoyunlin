package com.zdtech.platform.framework.entity;

/**
 * Created by ll007 on 2016/8/11.
 */
public class Leaf {
        //节点属性
        private String xattribute;
        //节点path
        private String xpath;
        //节点值
        private String value;
        public Leaf(String xattribute, String xpath, String value) {
            super();
            this.xattribute = xattribute;
            this.xpath = xpath;
            this.value = value;
        }
        public String getXattribute() {
            return xattribute;
        }
        public void setXattribute(String xattribute) {
            this.xattribute = xattribute;
        }
        public String getXpath() {
            return xpath;
        }
        public void setXpath(String xpath) {
            this.xpath = xpath;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }
