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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return length
     */
    public long getLength() {
        return length;
    }

    /**
     * 设置
     * @param length
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * 获取
     * @return lastModified
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * 设置
     * @param lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * 获取
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置
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
        return length == that.length && lastModified == that.lastModified && Objects.equals(name, that.name) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length, lastModified, path);
    }
}
