%module djonwrapper

%include "std_string.i"
%include "std_vector.i"
%include "windows.i"

%{
#include <boost/asio.hpp>
#include "includes/bsonarrayobj.h"
#include "includes/bson.h"
#include "includes/bsonobj.h"
#include "includes/bsonparser.h"
#include "includes/filterdefs.h"
#include "includes/bsonutil.h"
#include "includes/util.h"
#include "includes/djondbconnection.h"
#include "includes/djondbcursor.h"
#include "includes/djondbconnectionmanager.h"
#include "includes/djondb_client.h"
%}


namespace std {
   %template(BSONObjVectorPtr) vector<BSONObj*>;
   %template(StringVector) vector<std::string>;
}

%typemap(javabase) ParseException "java.lang.RuntimeException";
%typemap(javacode) ParseException %{
  public String getMessage2() {
     String w =  djonwrapperJNI.ParseException_what(swigCPtr, this);
     System.out.println("GetMessage: " + w);
    return errorCode() + " - " + what();
  }
%}

%typemap(javabase) DjondbException "java.lang.RuntimeException";
%typemap(javacode) DjondbException %{
  public String getMessage() {
     String w = what();
     System.out.println("GetMessage: " + w);
    return errorCode() + " - " + what();
  }
%}

%exception parse {
   try {
      $action
   } catch (BSONParseException &e) {
      jclass clazz = jenv->FindClass("djondb/ParseException");
      jmethodID methodID = jenv->GetMethodID(clazz, "<init>", "(ILjava/lang/String;)V");
      const char* data = e.what();
      jstring s = (*jenv).NewStringUTF(data);
      jint i = e.errorCode();
      jthrowable p = (jthrowable)jenv->NewObject(clazz, methodID, i, s);
      jenv->Throw(p);
      return 0;
   }
}

%exception insert {
   try {
      $action
   } catch (DjondbException &e) {
      jclass clazz = jenv->FindClass("djondb/DjondbException");
      jenv->ThrowNew(clazz, e.what());
   }
}

%exception remove {
   try {
      $action
   } catch (DjondbException &e) {
      jclass clazz = jenv->FindClass("djondb/DjondbException");
      jenv->ThrowNew(clazz, e.what());
   }
}

%exception update {
   try {
      $action
   } catch (DjondbException &e) {
      jclass clazz = jenv->FindClass("djondb/DjondbException");
      jenv->ThrowNew(clazz, e.what());
   }
}

%ignore getDJString;

/* Let's just grab the original header file here */
%include "native/includes/bsonarrayobj.h"
%include "native/includes/bson.h"
%include "native/includes/bsonobj.h"
%include "native/includes/bsonparser.h"
%include "native/includes/filterdefs.h"
%include "native/includes/bsonutil.h"
%include "native/includes/util.h"
%include "native/includes/djondbconnection.h"
%include "native/includes/djondbcursor.h"
%include "native/includes/djondbconnectionmanager.h"
%include "native/includes/djondb_client.h"

