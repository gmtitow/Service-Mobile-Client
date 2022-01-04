package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "Category")
public class Category extends Model implements Serializable {

    @Column(unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("categoryId")
    @Expose
    private int categoryId;

    @Column
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}