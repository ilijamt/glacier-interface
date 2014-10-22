package com.matoski.glacier.interfaces;

public interface ICommandCallback {

    void setup();

    void completed();

    void progress();

    void exception(Exception e);
}
