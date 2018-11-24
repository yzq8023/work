package com.github.hollykunge.security.admin.vo;

import com.github.hollykunge.security.common.vo.TreeNode;

public class OrgTree extends TreeNode {
    String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
