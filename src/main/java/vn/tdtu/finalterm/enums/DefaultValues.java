package vn.tdtu.finalterm.enums;

public enum DefaultValues {
    // Use for change giaNhap to giaBan to get profit
    HESOGIATANG(1.5f),
    // Use for change ngayNhap to hanTon to get obsolete inventory
    KYHAN(30.0f);

    private float value;

    private DefaultValues(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
