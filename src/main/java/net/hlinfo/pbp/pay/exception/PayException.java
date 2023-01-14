package net.hlinfo.pbp.pay.exception;

public class PayException extends Exception {
	/**
	 * 扩展数据,用于额外返回的数据
	 */
	private Object extendData;
	private static final long serialVersionUID = 1L;
	public PayException() {
        super();
    }

    /**
     * Constructs an {@code IOException} with the specified detail message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public PayException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code IOException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code IOException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for IO exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public PayException(Throwable cause) {
        super(cause);
    }
    /**
     * 使用指定的详细信息构造异常以及设置扩展数据。
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     * @param extendData 扩展数据
     */
	public PayException(String message,Object extendData) {
		super(message);
		this.extendData = extendData;
	}

	/**
	 *  扩展数据,用于额外返回的数据
	 * @return property value of extendData
	 */
	public Object getExtendData() {
		return extendData;
	}

	/**
	 * 扩展数据,用于额外返回的数据
	 *
	 * @param extendData value to be assigned to property extendData
	 */
	public void setExtendData(Object extendData) {
		this.extendData = extendData;
	}
}
