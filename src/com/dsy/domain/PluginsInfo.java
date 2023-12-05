package com.dsy.domain;

import java.util.Objects;

public class PluginsInfo {
    private String name;
    private long length;
    private long lastModified;
    private String path;

    public PluginsInfo() {
    }

    public PluginsInfo(String name, long length, long lastModified, String path) {
        this.name = name;
        this.length = length;
        this.lastModified = lastModified;
        this.path = path;
    }

    /**
     * 获取
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     *
     * @return length
     */
    public long getLength() {
        return length;
    }

    /**
     * 设置
     *
     * @param length
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * 获取
     *
     * @return lastModified
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * 设置
     *
     * @param lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * 获取
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    public String toString() {
        return "name=" + name + "&length=" + length + "&lastModified=" + lastModified + "&path=" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginsInfo that = (PluginsInfo) o;

        String[] strArr1 = path.split("\\\\");
        StringBuilder sb1 = new StringBuilder();
        for (int i = strArr1.length - 1; i >= 0; i--) {
            if ("plugins".equals(strArr1[i])) {
                break;
            } else {
                sb1.insert(0, strArr1[i]);
                sb1.insert(0, "\\");
            }
        }
        String s1 = sb1.toString();
        String[] strArr2 = that.path.split("\\\\");
        StringBuilder sb2 = new StringBuilder();
        for (int i = strArr2.length - 1; i >= 0; i--) {
            if ("plugins".equals(strArr2[i])) {
                break;
            } else {
                sb2.insert(0, strArr2[i]);
                sb2.insert(0, "\\");
            }
        }
        String s2  = sb2.toString();

      /*  System.out.println(s1);
        System.out.println(s2);*/

        return length == that.length && lastModified == that.lastModified && Objects.equals(name, that.name) && Objects.equals(s1, s2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length, lastModified, path);
    }
}
