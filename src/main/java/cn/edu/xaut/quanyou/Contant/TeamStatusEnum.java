package cn.edu.xaut.quanyou.Contant;

public enum TeamStatusEnum {
    PUBLIC(0, "公开"),
    PRIVATE(2, "私有,不可见"),
    PROTECTED(1, "受保护的");
    private int vaule;
    private String text;
    TeamStatusEnum(int vaule, String text) {
        this.vaule = vaule;
        this.text = text;
    }
    public static TeamStatusEnum getEnum(Integer vaule){
        if (vaule == null) {
            return null;
        }

        for (TeamStatusEnum teamStatusEnum : TeamStatusEnum.values()) {
            if (teamStatusEnum.vaule == vaule)
                return teamStatusEnum;
        }
        return null;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVaule() {
        return vaule;
    }

    public void setVaule(int vaule) {
        this.vaule = vaule;
    }
}
