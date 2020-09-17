package com.ssafy.bigdata.dto;

public class ToolsHitter {
    private float power;
    private float speed;
    private float contact;
    private float defense;
    private float shoulder;

    public ToolsHitter() {
    }

    public ToolsHitter(float power, float speed, float contact, float defense, float shoulder) {
        this.power = power;
        this.speed = speed;
        this.contact = contact;
        this.defense = defense;
        this.shoulder = shoulder;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getContact() {
        return contact;
    }

    public void setContact(float contact) {
        this.contact = contact;
    }

    public float getDefense() {
        return defense;
    }

    public void setDefense(float defense) {
        this.defense = defense;
    }

    public float getShoulder() {
        return shoulder;
    }

    public void setShoulder(float shoulder) {
        this.shoulder = shoulder;
    }

    @Override
    public String toString() {
        return "ToolsHitter [contact=" + contact + ", defense=" + defense + ", power=" + power + ", shoulder="
                + shoulder + ", speed=" + speed + "]";
    }

    

   
}
