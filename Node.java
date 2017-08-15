package com.ifeng.dmp.ctrp.ml;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

/**
 * Created by zhaoyy on 2017/8/14.
 */
@Immutable
public final class Node implements Serializable {

    private static final long serialVersionUID = 273479971015393598L;

    private final Node left;
    private final Node right;

    private final boolean leaf;
    private final int featureIndex;
    private final double featureValue;

    Node(Node left, Node right, boolean leaf, int featureIndex, double featureValue) {
        if((!leaf) ? (left == null && right ==null):(left != null && right != null))
            throw new IllegalArgumentException("illegal leaf:"+ leaf);
        this.left = left;
        this.right = right;
        this.leaf = leaf;
        this.featureIndex = featureIndex;
        this.featureValue = featureValue;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public double getFeatureValue() {
        return featureValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder,"");
        return builder.toString();
    }

    private void toString(StringBuilder builder,String prefix){
        if(isLeaf()){
            builder.append(prefix)
                    .append("Predict: ")
                    .append(featureValue);
            return;
        }
        builder.append(prefix)
                .append("If (feature ")
                .append(featureIndex)
                .append(" <= ")
                .append(featureValue)
                .append(")\n");
        String tab = " " + prefix;
        left.toString(builder, tab);
        builder
                .append('\n')
                .append(prefix)
                .append("Else (feature ")
                .append(featureIndex)
                .append(" > ")
                .append(featureValue)
                .append(")\n");
        right.toString(builder, tab);
    }
}
