package kp.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * The box with the numbers.
 */
//SonarQube signals 'duplicated blocks of code'.  But this rule is deprecated in Sonar! 
public class Box implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * The 'send' number.
     */
    private int sndNum = 0;
    /**
     * The 'process' number.
     */
    private int prcNum = 0;
    /**
     * The 'receive' number.
     */
    private int recNum = 0;
    /**
     * The milliseconds.
     */
    private long milliseconds = 0;

    /**
     * Constructor.
     */
    public Box() {
    }

    /**
     * Constructor.
     *
     * @param sndNum       the 'send' number
     * @param milliseconds the milliseconds
     */
    public Box(int sndNum, long milliseconds) {
        this.sndNum = sndNum;
        this.milliseconds = milliseconds;
    }

    /**
     * Gets the 'send' number.
     *
     * @return the 'send' number
     */
    public int getSndNum() {
        return sndNum;
    }

    /**
     * Sets the 'send' number.
     *
     * @param sndNum the 'send' number to set
     */
    public void setSndNum(int sndNum) {
        this.sndNum = sndNum;
    }

    /**
     * Gets the 'process' number.
     *
     * @return the 'process' number
     */
    public int getPrcNum() {
        return prcNum;
    }

    /**
     * Sets the 'process' number.
     *
     * @param prcNum the 'process' number to set
     */
    public void setPrcNum(int prcNum) {
        this.prcNum = prcNum;
    }

    /**
     * Gets the 'receive' number.
     *
     * @return the 'receive' number
     */
    public int getRecNum() {
        return recNum;
    }

    /**
     * Sets the 'receive' number.
     *
     * @param recNum the 'receive' number to set
     */
    public void setRecNum(int recNum) {
        this.recNum = recNum;
    }

    /**
     * Gets the milliseconds.
     *
     * @return the milliseconds
     */
    public long getMilliseconds() {
        return milliseconds;
    }

    /**
     * Sets the milliseconds.
     *
     * @param milliseconds the milliseconds to set
     */
    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

}