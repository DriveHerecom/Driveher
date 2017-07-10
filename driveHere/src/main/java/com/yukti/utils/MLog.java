package com.yukti.utils;

import android.util.Log;

public class MLog
{
  private static boolean DEBUG = true;
  private static final String TAG = "mizcoin";
  
  public static void d(Object message)
  {
    if (DEBUG)
    {
      String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
      String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
      String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
      int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
      
      if (message == null)
      {
        Log.d(TAG, className + "." + methodName + "()|" + lineNumber + "|" + "NULL");
      }
      else
      {
        Log.d(TAG, className + "." + methodName + "()|" + lineNumber + "|" + message.toString());
      }
    }
  }
  
  public static void v(Object message)
  {
    if (DEBUG)
    {
      String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
      String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
      String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
      int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
      
      if (message == null)
      {
        Log.v(TAG, className + "." + methodName + "()|" + lineNumber + "|" + "NULL");
      }
      else
      {
        Log.v(TAG, className + "." + methodName + "()|" + lineNumber + "|" + message.toString());
      }
    }
  }
  
  public static void e(Object message)
  {
    if (DEBUG)
    {
      String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
      String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
      String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
      int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
      
      if (message == null)
      {
        Log.e(TAG, className + "." + methodName + "()|" + lineNumber + "|" + "NULL");
      }
      else
      {
        Log.e(TAG, className + "." + methodName + "()|" + lineNumber + "|" + message.toString());
      }
    }
  }
  
  public static void w(Object message)
  {
    if (DEBUG)
    {
      String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
      String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
      String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
      int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
      
      if (message == null)
      {
        Log.w(TAG, className + "." + methodName + "()|" + lineNumber + "|" + "NULL");
      }
      else
      {
        Log.w(TAG, className + "." + methodName + "()|" + lineNumber + "|" + message.toString());
      }
    }
  }
}