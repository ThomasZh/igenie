package com.oct.ga.comm.tlv;

/**
 * Created by liwenzhi on 14-10-30.
 */
public class TlvObjectExt extends TlvObject {

    public TlvObjectExt(short tag) {
        super(tag);
    }

    /**
     * append the child in root and return himself
     *
     * @param tlv
     * @return
     */
    public TlvObjectExt plus(TlvObject tlv){
        super.push(tlv);
        return this;
    }

}
