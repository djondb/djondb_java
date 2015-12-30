/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package djondb;

public class BSONParseException {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected BSONParseException(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(BSONParseException obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        djonwrapperJNI.delete_BSONParseException(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public BSONParseException(int code, String error) {
    this(djonwrapperJNI.new_BSONParseException__SWIG_0(code, error), true);
  }

  public BSONParseException(BSONParseException orig) {
    this(djonwrapperJNI.new_BSONParseException__SWIG_1(BSONParseException.getCPtr(orig), orig), true);
  }

  public String what() {
    return djonwrapperJNI.BSONParseException_what(swigCPtr, this);
  }

  public int errorCode() {
    return djonwrapperJNI.BSONParseException_errorCode(swigCPtr, this);
  }

}
