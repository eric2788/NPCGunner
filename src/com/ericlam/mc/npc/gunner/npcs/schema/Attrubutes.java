package com.ericlam.mc.npc.gunner.npcs.schema;

public class Attrubutes {
    private int atkcount;
    private int vision;
    public Attrubutes(){
        atkcount = 0;
        vision = 0;
    }
    public void addAtkCount(){
        ++atkcount;
    }

    public boolean reduceAtkCount(){
        boolean zero = atkcount < 1;
        if (!zero) --atkcount;
        return zero;
    }

    public boolean addVision(){
        boolean canAdd = vision < 15;
        if (canAdd) ++vision;
        return canAdd;
    }

    public boolean reduceVision(){
        boolean zero = vision < 1;
        if (!zero) --vision;
        return zero;
    }

    public int getAtkcount() {
        return atkcount;
    }

    public int getVision() {
        return vision;
    }

    public void setAtkcount(int atkcount) {
        this.atkcount = atkcount;
    }

    public void setVision(int vision) {
        this.vision = vision;
    }
}
