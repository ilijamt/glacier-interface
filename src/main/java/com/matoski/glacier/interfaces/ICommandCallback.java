package com.matoski.glacier.interfaces;

public interface ICommandCallback {

    void completed();

    void exception(Exception e);

    void progress();

    void setup();
}
