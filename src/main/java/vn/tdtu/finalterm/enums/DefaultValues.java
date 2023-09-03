package vn.tdtu.finalterm.enums;

public enum DefaultValues {
    // Use for change giaNhap to giaBan to get profit
    HESOGIATANG(1.5f),
    // Use for change ngayNhap to hanTon to get obsolete inventory
    KYHAN(30.0f),
    // Expiration Time (Second)
    EXPIRATION_TIME(600.0f),
    // JWT Token Time Expired (Second)
    JWT_TOKEN_TIME(5 * 60 * 60.0f);

    private float value;

    private DefaultValues(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
