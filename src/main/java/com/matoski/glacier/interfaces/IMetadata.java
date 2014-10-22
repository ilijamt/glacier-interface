package com.matoski.glacier.interfaces;

public interface IMetadata {

    public void parse();

    public void process(String metadata);

    public void store(String filename);

}
