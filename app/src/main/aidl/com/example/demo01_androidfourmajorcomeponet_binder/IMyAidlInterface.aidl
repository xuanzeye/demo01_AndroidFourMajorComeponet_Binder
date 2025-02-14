// IMyAidlInterface.aidl
package com.example.demo01_androidfourmajorcomeponet_binder;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    int add(int a, int b);

    String getMessage(String name);

}