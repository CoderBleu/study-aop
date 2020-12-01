package cn.coderblue.studyaop.enums;

/**
 * 操作来源
 *
 * @author coderblue
 */

public enum OperatorType {
    /**
     * 其它
     */
    OTHER(0),

    /**
     * 后台用户
     */
    MANAGE(1),

    /**
     * 手机端用户
     */
    MOBILE(2);

    private Integer value;

    OperatorType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
