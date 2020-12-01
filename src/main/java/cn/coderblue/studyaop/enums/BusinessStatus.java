package cn.coderblue.studyaop.enums;

/**
 * 操作状态
 *
 * @author coderblue
 */
public enum BusinessStatus {
    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 失败
     */
    FAIL(500);

    private Integer code;

    BusinessStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
