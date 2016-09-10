package com.supperdrug.customerconsentform.models;

/**
 * Created by Waseem on 01/09/2016.
 */
public class Branch {
    private String id;
    private String branchName;

    public Branch(String id, String branchName) {
        this.id = id;
        this.branchName = branchName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return branchName;
    }
}
